package hrv.band.app.view.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import hrv.RRData;
import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.RRInterval.HRVDeviceStatus;
import hrv.band.app.RRInterval.HRVRRDeviceListener;
import hrv.band.app.RRInterval.HRVRRIntervalDevice;
import hrv.band.app.RRInterval.HRVRRIntervalEvent;
import hrv.band.app.RRInterval.HRVRRIntervalListener;
import hrv.band.app.RRInterval.Interval;
import hrv.band.app.RRInterval.antplus.AntPlusRRDataDevice;
import hrv.band.app.RRInterval.msband.MSBandRRIntervalDevice;
import hrv.band.app.view.HRVMeasurementActivity;
import hrv.calc.AllHRVIndiceCalculator;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment allowing user to start measurement.
 */
public class MeasuringFragment extends Fragment implements HRVRRDeviceListener, HRVRRIntervalListener {

    /** Key value for the calculated hrv parameter. **/
    private static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    /** Key value for the selected measure device. **/
    private static final String SELECTED_DEVICE_ID = "selected_device_id";
    /** The duration of the measurement. **/
    private static int duration = 90000;

    /** Possible devices the user can select. **/
    private enum DeviceID {NONE, MSBAND, ANT}

    /** The shared preferences. **/
    private static SharedPreferences sharedPreferences;

    /** Device to measure rr interval. **/
    private static HRVRRIntervalDevice hrvRRIntervalDevice;

    /** Text view showing the actual rr interval. **/
    private TextView rrStatus;
    /** Text view showing the status of the measurement. **/
    private TextView txtStatus;
    /** Indicates the progress of the measurement. **/
    private ProgressBar progressBar;
    /** The animation of the progress bar. **/
    private static ObjectAnimator animation;

    /** Button to connect with the ms band. **/
    private com.github.clans.fab.FloatingActionButton connectToBandFAB;
    /** Button to connect with ant+ device. **/
    private com.github.clans.fab.FloatingActionButton connectToAntPlusFAB;
    /** Button to disconnect with devices. **/
    private com.github.clans.fab.FloatingActionButton disconnectDevices;

    /** Root view of this fragment. **/
    private View view;

    /**
     * Returns a new instance of this fragment.
     * @return a new instance of this fragment.
     */
    public static MeasuringFragment newInstance() {
        return new MeasuringFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment_measure, container, false);
        view = rootView.findViewById(R.id.measure_fragment);

        rrStatus = (TextView) rootView.findViewById(R.id.rrStatus);
        txtStatus = (TextView) rootView.findViewById(R.id.measure_status);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        connectToBandFAB = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.connect_band_float_button);
        connectToAntPlusFAB = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.connect_antplus_float_button);
        disconnectDevices = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.disconnect_devices);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String durationPrefVal = sharedPreferences.getString("recording_length", "90000");
        duration = Integer.parseInt(durationPrefVal);

        hrvRRIntervalDevice = getDevice(DeviceID.values()[sharedPreferences.getInt(SELECTED_DEVICE_ID, 0)]);
        if (hrvRRIntervalDevice != null) {
            addDeviceListeners(this);
        }

        MeasurementClickListener clickListener = new MeasurementClickListener(this);

        progressBar.setOnClickListener(clickListener);
        connectToBandFAB.setOnClickListener(clickListener);
        connectToAntPlusFAB.setOnClickListener(clickListener);
        disconnectDevices.setOnClickListener(clickListener);

        setProgressBarSize();

        initAnimation();

        return rootView;
    }

    /**
     * Adds listeners to the measurement device.
     * @param measuringFragment the measure fragment.
     */
    private static void addDeviceListeners(MeasuringFragment measuringFragment) {
        hrvRRIntervalDevice.addDeviceListener(measuringFragment);
        hrvRRIntervalDevice.addRRIntervalListener(measuringFragment);
    }

    /**
     * Returns the measurement device from the given id.
     * @param id the device id from the actual measurement device.
     * @return the measurement device from the given id.
     */
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

    /**
     * Sets the progress bar size depending of the screen size.
     */
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

    /**
     * Calculates hrv parameter from given rr interval.
     * @param interval measured rr interval.
     * @return calculated hrv parameter from given rr interval.
     */
    private HRVParameters calculate(Interval interval) {
        //start calculation
        AllHRVIndiceCalculator calc = new AllHRVIndiceCalculator();
        calc.calculateAll(RRData.createFromRRInterval(interval.GetRRInterval(), RRData.RRDataUnit.s));

        HRVParameters results = HRVParameters.from(calc, interval.GetStartTime(), interval.GetRRInterval());
        results.setTime(interval.GetStartTime());
        return results;
    }

    /**
     * Initialize the animation of the progress bar.
     */
    private void initAnimation() {
        // see this max value coming back here, we animate towards that value
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
                    showSnackbar(view, "This should not happen...");
                }
                hrvRRIntervalDevice.stopMeasuring();

                Interval interval = new Interval(new Date());
                interval.SetRRInterval(hrvRRIntervalDevice.getRRIntervals().toArray(new Double[0]));

                Intent intent = new Intent(getContext(), HRVMeasurementActivity.class);
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

    /**
     * Stops the measuring of the device and resets the ui.
     */
    public void stopMeasuring() {
        if (hrvRRIntervalDevice != null && animation != null) {
            hrvRRIntervalDevice.stopMeasuring();
            animation.cancel();
            resetProgress();
        }
    }

    /**
     * Resets the ui.
     */
    private void resetProgress() {
       // progressBar.setClickable(true);
        progressBar.setProgress(progressBar.getMax());
        setConnectionButtonClickable(true);

        initAnimation();

        updateTextView(getActivity(), rrStatus, "0,00");
        updateTextView(getActivity(), txtStatus, getResources().getString(R.string.measure_fragment_press_to_start));
    }

    /**
     * Sets the clickable of (dis)connect buttons.
     * @param clickable true if buttons should be clickable, false otherwise.
     */
    private void setConnectionButtonClickable(boolean clickable) {
        connectToAntPlusFAB.setClickable(clickable);
        connectToBandFAB.setClickable(clickable);
        disconnectDevices.setClickable(clickable);
    }

    @Override
    public void deviceStartedMeasurement() {
        String msg = getResources().getString(R.string.msg_hold_still);
        updateTextView(getActivity(), txtStatus, msg);

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
        showSnackbar(view, error);
    }

    @Override
    public void deviceStatusChanged(HRVDeviceStatus status) {
        switch (status) {
            case Connecting:
                updateTextView(getActivity(), txtStatus, getResources().getString(R.string.msg_connecting));
                break;
            case Connected:
                updateTextView(getActivity(), txtStatus, getResources().getString(R.string.title_activity_start_measuring));
                break;
            case Disconnected:
                updateTextView(getActivity(), txtStatus, getResources().getString(R.string.error_band_not_paired));
               // UiHandlingUtil.showSnackbar(view, getString(R.string.error_device_not_connected_help));
        }
    }

    @Override
    public void newRRInterval(HRVRRIntervalEvent event) {
        //UiHandlingUtil.updateTextView(getActivity(), rrStatus, new DecimalFormat("#,##").format(event.getRr()));
        updateTextView(getActivity(), rrStatus, String.format("%.2f", event.getRr()));
    }

    /**
     * Shows a snack bar with the given message in the given view.
     * @param view the view the snack bar should appear.
     * @param msg the message the snackbar should show.
     */
    private static void showSnackbar(View view, final String msg) {
        final Snackbar snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE);
        String closeStr = snackBar.getView().getResources().getString(R.string.common_close);

        snackBar.setAction(closeStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }

    /**
     * Updates a text view in a ui thread.
     * @param activity the activity in which the thread should run.
     * @param txt the text view to be set.
     * @param string the string the txt should show.
     */
    private static void updateTextView(Activity activity, final TextView txt, final String string) {
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt.setText(string);
            }
        });
    }

    /**
     * Click listener handling the clicks in this fragment.
     */
    private static class MeasurementClickListener implements View.OnClickListener {

        /** The measuring fragment. **/
        private final MeasuringFragment measuringFragment;
        /** The activity holding the fragment. **/
        private final Activity activity;
        /** The connection button containing the other buttons to (dis)connect the devices. **/
        private final com.github.clans.fab.FloatingActionMenu menuDown;

        MeasurementClickListener(MeasuringFragment measuringFragment) {
            this.measuringFragment = measuringFragment;
            this.activity = measuringFragment.getActivity();
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

        /**
         * Starts a measurement or opens a cancel dialog if measurement already in progress.
         */
        private void startMeasurement() {
            if (animation.isRunning()) {
                CancelMeasuringDialogFragment.newInstance().show(activity.getFragmentManager(), "dialog");
            } else {
                if (hrvRRIntervalDevice != null) {
                    hrvRRIntervalDevice.tryStartRRIntervalMeasuring();
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.msg_select_device), Toast.LENGTH_SHORT).show();
                    menuDown.open(true);
                }
            }
        }

        /**
         * Connects to ms band.
         */
        private void connectToMSBand() {
            setDevice(DeviceID.MSBAND);

            hrvRRIntervalDevice = new MSBandRRIntervalDevice(activity);
            initDevice();
        }

        /**
         * Connects to ant+ device.
         */
        private void connectToAnt() {
            setDevice(DeviceID.ANT);

            hrvRRIntervalDevice = new AntPlusRRDataDevice(activity.getApplicationContext(), activity);
            initDevice();
        }

        /**
         * Disconnects connected device.
         */
        private void disconnectDevices() {
            setDevice(DeviceID.NONE);
            if (hrvRRIntervalDevice != null) {
                hrvRRIntervalDevice.notifyDeviceStatusChanged(HRVDeviceStatus.Disconnected);
                hrvRRIntervalDevice = null;
            }
            menuDown.toggle(true);

            Toast.makeText(activity, activity.getResources().getString(R.string.msg_disconnecting), Toast.LENGTH_SHORT).show();
        }

        /**
         * Initialize selected device.
         */
        private void initDevice() {
            addDeviceListeners(measuringFragment);
            hrvRRIntervalDevice.connect();
            menuDown.toggle(true);
        }

        /**
         * Sets the selected device in preferences.
         * @param device the device id.
         */
        private void setDevice(DeviceID device) {
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putInt(SELECTED_DEVICE_ID, device.ordinal());
            prefsEditor.apply();
        }
    }
}
