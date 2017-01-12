package hrv.band.app.view.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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

import java.text.DecimalFormat;
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

public class MeasuringFragment extends Fragment implements HRVRRDeviceListener, HRVRRIntervalListener {

    private static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    private static final String SELECTED_DEVICE_ID = "selected_device_id";

    private enum DeviceID {NONE, MSBAND, ANT}

    private SharedPreferences sharedPreferences;

    private int duration = 10000;
    private HRVRRIntervalDevice hrvRRIntervalDevice;
    private TextView rrStatus;
    private TextView txtStatus;
    private ProgressBar progressBar;
    private ObjectAnimator animation;

    private com.github.clans.fab.FloatingActionButton connectToBandFAB;
    private com.github.clans.fab.FloatingActionButton connectToAntPlusFAB;
    private com.github.clans.fab.FloatingActionButton disconnectDevices;
    private com.github.clans.fab.FloatingActionMenu menuDown;


    public static View view;

    public MeasuringFragment() {
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
        menuDown = (com.github.clans.fab.FloatingActionMenu) getActivity().findViewById(R.id.menu_down);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        hrvRRIntervalDevice = getDevice(DeviceID.values()[sharedPreferences.getInt(SELECTED_DEVICE_ID, 0)]);
        if (hrvRRIntervalDevice != null) {
            addDeviceListeners();
        }

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hrvRRIntervalDevice != null) {
                    hrvRRIntervalDevice.tryStartRRIntervalMeasuring();
                } else {
                    menuDown.open(true);
                }
            }
        });

        connectToBandFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDevice(DeviceID.MSBAND);

                hrvRRIntervalDevice = new MSBandRRIntervalDevice(getActivity());
                initDevice();
            }
        });

        connectToAntPlusFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDevice(DeviceID.ANT);

                hrvRRIntervalDevice = new AntPlusRRDataDevice(getContext(), getActivity());
                initDevice();
            }
        });

        disconnectDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDevice(DeviceID.NONE);
                hrvRRIntervalDevice = null;
                menuDown.toggle(true);

                UiHandlingUtil.showSnackbar(getResources().getString(R.string.msg_disconnecting));
            }
        });

        setProgressBarSize();

        initAnimation(new Date());

        return rootView;
    }

    private void addDeviceListeners() {
        hrvRRIntervalDevice.addDeviceListener(MeasuringFragment.this);
        hrvRRIntervalDevice.addRRIntervalListener(MeasuringFragment.this);
    }

    private void initDevice() {
        addDeviceListeners();
        hrvRRIntervalDevice.connect();
        menuDown.toggle(true);
    }

    private void setDevice(DeviceID device) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(SELECTED_DEVICE_ID, device.ordinal());
        prefsEditor.apply();
    }

    private HRVRRIntervalDevice getDevice(DeviceID id) {
        switch (id) {
            case MSBAND: return new MSBandRRIntervalDevice(getActivity());
            case ANT: return new AntPlusRRDataDevice(getContext(), getActivity());
        }
        return null;
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

    public HRVRRIntervalDevice getHRVDevice() {
        return hrvRRIntervalDevice;
    }

    private void initAnimation(final Date date) {

        animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 1000); // see this max value coming back here, we animale towards that value
        animation.setDuration (duration); //in milliseconds
        animation.setInterpolator (new LinearInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator a) {
                progressBar.setClickable(false);
                connectToBandFAB.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (hrvRRIntervalDevice == null) {
                    UiHandlingUtil.showSnackbar("This should not happen...");
                }
                hrvRRIntervalDevice.stopMeasuring();

                Interval interval = new Interval(date);
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

    private void stopMeasuring() {
        if (hrvRRIntervalDevice != null) {
            hrvRRIntervalDevice.stopMeasuring();
            animation.cancel();
            resetProgress();
        }
    }

    private void resetProgress() {
        progressBar.setClickable(true);
        progressBar.setProgress(progressBar.getMax());
        connectToBandFAB.setClickable(true);

        UiHandlingUtil.updateTextView(getActivity(), rrStatus, "0,00");
        UiHandlingUtil.updateTextView(getActivity(), txtStatus, getResources().getString(R.string.measure_fragment_press_to_start));
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
        UiHandlingUtil.updateTextView(getActivity(), txtStatus, error);
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
                UiHandlingUtil.showSnackbar(getString(R.string.error_device_not_connected_help));
        }
    }

    @Override
    public void newRRInterval(HRVRRIntervalEvent event) {
        UiHandlingUtil.updateTextView(getActivity(), rrStatus, new DecimalFormat("#,##").format(event.getRr()));
        //UiHandlingUtil.updateTextView(getActivity(), rrStatus, String.format("%.2f", event.getRr()));
    }
}
