package hrv.band.app.view.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

import hrv.band.app.Control.Calculation;
import hrv.band.app.Control.HRVParameters;
import hrv.band.app.Fourier.FastFourierTransform;
import hrv.band.app.Interpolation.CubicSplineInterpolation;
import hrv.band.app.R;
import hrv.band.app.RRInterval.HRVDeviceStatus;
import hrv.band.app.RRInterval.HRVRRDeviceListener;
import hrv.band.app.RRInterval.HRVRRIntervalDevice;
import hrv.band.app.RRInterval.HRVRRIntervalEvent;
import hrv.band.app.RRInterval.HRVRRIntervalListener;
import hrv.band.app.RRInterval.Interval;
import hrv.band.app.RRInterval.antplus.AntPlusRRDataDevice;
import hrv.band.app.RRInterval.msband.MSBandRRIntervalDevice;
import hrv.band.app.view.MeasureDetailsActivity;
import hrv.band.app.view.UiHandlingUtil;

/**
 * Created by thomcz on 23.06.2016.
 */

public class MeasuringFragment extends Fragment implements HRVRRDeviceListener, HRVRRIntervalListener/*, View.OnClickListener */{

    private static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    private static final String SELECTED_DEVICE_ID = "selected_device_id";
    private static final int duration = 10000;

    private enum DeviceID {NONE, MSBAND, ANT}

    private static SharedPreferences sharedPreferences;

    private static HRVRRIntervalDevice hrvRRIntervalDevice;
    private TextView rrStatus;
    private TextView txtStatus;
    private ProgressBar progressBar;
    private static ObjectAnimator animation;

    private com.github.clans.fab.FloatingActionButton connectToBandFAB;
    private com.github.clans.fab.FloatingActionButton connectToAntPlusFAB;
    private com.github.clans.fab.FloatingActionButton disconnectDevices;


    private View view;

    public static MeasuringFragment newInstance() {
        return new MeasuringFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_measure, container, false);
        view = rootView.findViewById(R.id.measure_fragment);

        rrStatus = (TextView) rootView.findViewById(R.id.rrStatus);
        txtStatus = (TextView) rootView.findViewById(R.id.measure_status);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        connectToBandFAB = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.connect_band_float_button);
        connectToAntPlusFAB = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.connect_antplus_float_button);
        disconnectDevices = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.disconnect_devices);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        hrvRRIntervalDevice = getDevice(DeviceID.values()[sharedPreferences.getInt(SELECTED_DEVICE_ID, 0)]);
        if (hrvRRIntervalDevice != null) {
            addDeviceListeners(this);
        }

        MeasurementClickListener clickListener = new MeasurementClickListener(this, view);

        progressBar.setOnClickListener(clickListener);
        connectToBandFAB.setOnClickListener(clickListener);
        connectToAntPlusFAB.setOnClickListener(clickListener);
        disconnectDevices.setOnClickListener(clickListener);

        setProgressBarSize();

        initAnimation();

        return rootView;
    }

    private static void addDeviceListeners(MeasuringFragment measuringFragment) {
        hrvRRIntervalDevice.addDeviceListener(measuringFragment);
        hrvRRIntervalDevice.addRRIntervalListener(measuringFragment);
    }

    private HRVRRIntervalDevice getDevice(DeviceID id) {
        switch (id) {
            case MSBAND: return new MSBandRRIntervalDevice(getActivity());
            case ANT: return new AntPlusRRDataDevice(getContext(), getActivity());
        }
        return null;
    }

    private static class MeasurementClickListener implements View.OnClickListener {

        private MeasuringFragment measuringFragment;
        private Activity activity;
        private View view;
        private com.github.clans.fab.FloatingActionMenu menuDown;

        MeasurementClickListener(MeasuringFragment measuringFragment, View view) {
            this.measuringFragment = measuringFragment;
            this.activity = measuringFragment.getActivity();
            this.view = view;
            menuDown = (com.github.clans.fab.FloatingActionMenu) activity.findViewById(R.id.menu_down);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.progressBar:
                    startMeasurement();
                    break;
                case R.id.connect_antplus_float_button:
                    connectToAnt();
                    break;
                case R.id.connect_band_float_button:
                    connectToMSBand();
                    break;
                case R.id.disconnect_devices:
                    disconnectDevices();
                    break;
            }
        }

        private void startMeasurement() {
            if (animation.isRunning()) {
                CancelMeasuringDialogFragment.newInstance().show(activity.getFragmentManager(), "dialog");
            } else {
                if (hrvRRIntervalDevice != null) {
                    hrvRRIntervalDevice.tryStartRRIntervalMeasuring();
                } else {
                    menuDown.open(true);
                }
            }
        }

        private void connectToMSBand() {
            setDevice(DeviceID.MSBAND);

            hrvRRIntervalDevice = new MSBandRRIntervalDevice(activity);
            initDevice();
        }

        private void connectToAnt() {
            setDevice(DeviceID.ANT);

            hrvRRIntervalDevice = new AntPlusRRDataDevice(activity.getApplicationContext(), activity);
            initDevice();
        }

        private void disconnectDevices() {
            setDevice(DeviceID.NONE);
            hrvRRIntervalDevice.notifyDeviceStatusChanged(HRVDeviceStatus.Disconnected);
            hrvRRIntervalDevice = null;
            menuDown.toggle(true);

            UiHandlingUtil.showSnackbar(view, activity.getResources().getString(R.string.msg_disconnecting));
        }
        private void initDevice() {
            addDeviceListeners(measuringFragment);
            hrvRRIntervalDevice.connect();
            menuDown.toggle(true);
        }

        private void setDevice(DeviceID device) {
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putInt(SELECTED_DEVICE_ID, device.ordinal());
            prefsEditor.apply();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopMeasuring();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopMeasuring();
    }


    private void setProgressBarSize() {
        Display mDisplay = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mDisplay.getSize(size);

        ViewGroup.LayoutParams params = progressBar.getLayoutParams();
        int width = size.x;
        if (size.x > size.y) {
            width = size.y;
        }
        params.height = width;
        params.width = width;

        progressBar.setLayoutParams(params);
    }

    private HRVParameters calculate(Interval interval) {
        //start calculation
        CubicSplineInterpolation inter = new CubicSplineInterpolation();
        FastFourierTransform fft = new FastFourierTransform(4096);

        Calculation calc = new Calculation(fft, inter);
        HRVParameters results = calc.Calculate(interval);
        results.setTime(interval.GetStartTime());
        return results;
    }

    private void initAnimation() {
        // see this max value coming back here, we animale towards that value
        animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 1000);
        animation.setDuration (duration); //in milliseconds
        animation.setInterpolator (new LinearInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator a) {
                //progressBar.setClickable(false);
                setConnectionButtonClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (hrvRRIntervalDevice == null) {
                    UiHandlingUtil.showSnackbar(view, "This should not happen...");
                }
                hrvRRIntervalDevice.stopMeasuring();

                Interval interval = new Interval(new Date());
                interval.SetRRInterval(hrvRRIntervalDevice.getRRIntervals().toArray(new Double[0]));

                Intent intent = new Intent(getContext(), MeasureDetailsActivity.class);
                intent.putExtra(HRV_PARAMETER_ID, calculate(interval));
                startActivity(intent);

                resetProgress();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animation.removeAllListeners();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void stopMeasuring() {
        if (hrvRRIntervalDevice != null && animation != null) {
            hrvRRIntervalDevice.stopMeasuring();
            animation.cancel();
            resetProgress();
        }
    }

    private void resetProgress() {
       // progressBar.setClickable(true);
        progressBar.setProgress(progressBar.getMax());
        setConnectionButtonClickable(true);

        initAnimation();

        UiHandlingUtil.updateTextView(getActivity(), rrStatus, "0,00");
        UiHandlingUtil.updateTextView(getActivity(), txtStatus, getResources().getString(R.string.measure_fragment_press_to_start));
    }

    private void setConnectionButtonClickable(boolean clickable) {
        connectToAntPlusFAB.setClickable(clickable);
        connectToBandFAB.setClickable(clickable);
        disconnectDevices.setClickable(clickable);
    }

    @Override
    public void deviceStartedMeasurement() {
        String msg = getResources().getString(R.string.msg_hold_still);
        UiHandlingUtil.updateTextView(getActivity(), txtStatus, msg);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (animation != null) {
                    animation.start();
                }
            }
        });
    }

    @Override
    public void deviceError(String error) {
        UiHandlingUtil.showSnackbar(view, error);
    }

    @Override
    public void deviceStatusChanged(HRVDeviceStatus status) {
        switch (status) {
            case Connecting:
                UiHandlingUtil.updateTextView(getActivity(), txtStatus, getResources().getString(R.string.msg_connecting));
                break;
            case Connected:
                UiHandlingUtil.updateTextView(getActivity(), txtStatus, getResources().getString(R.string.title_activity_start_measuring));
                break;
            case Disconnected:
                UiHandlingUtil.updateTextView(getActivity(), txtStatus, getResources().getString(R.string.error_band_not_paired));
                UiHandlingUtil.showSnackbar(view, getString(R.string.error_device_not_connected_help));
        }
    }

    @Override
    public void newRRInterval(HRVRRIntervalEvent event) {
        //UiHandlingUtil.updateTextView(getActivity(), rrStatus, new DecimalFormat("#,##").format(event.getRr()));
        UiHandlingUtil.updateTextView(getActivity(), rrStatus, String.format("%.2f", event.getRr()));
    }
}
