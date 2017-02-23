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

import org.apache.commons.lang3.ArrayUtils;

import java.util.Date;

import hrv.HRVParameter;
import hrv.RRData;
import hrv.band.app.R;
import hrv.band.app.control.Measurement;
import hrv.band.app.devices.HRVDeviceStatus;
import hrv.band.app.devices.HRVRRDeviceListener;
import hrv.band.app.devices.HRVRRIntervalDevice;
import hrv.band.app.devices.antplus.AntPlusRRDataDevice;
import hrv.band.app.devices.msband.MSBandRRIntervalDevice;
import hrv.band.app.view.HRVMeasurementActivity;
import hrv.calc.continous.HRVContinousPulse;
import hrv.calc.continous.HRVParameterChangedListener;
import hrv.calc.continous.HRVRRIntervalEvent;
import hrv.calc.continous.HRVRRIntervalListener;
import units.TimeUnitConverter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment allowing user to start measurement.
 */
public class MeasuringFragment extends Fragment implements HRVRRDeviceListener, HRVRRIntervalListener, HRVParameterChangedListener {

    /** Key value for the calculated hrv parameter. **/
    private static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    /** Key value for the selected measure device. **/
    private static final String SELECTED_DEVICE_ID = "selected_device_id";

    /** Possible devices the user can select. **/
    private enum DeviceID {NONE, MSBAND, ANT}

    /** The shared preferences. **/
    private SharedPreferences sharedPreferences;

    /** Device to measure rr interval. **/
    private HRVRRIntervalDevice hrvRRIntervalDevice;

    /** Text view showing the current rr interval. **/
    private TextView rrStatus;
    /** Text view showing the current pulse **/
    private TextView pulse;
    /** Text view showing the status of the measurement. **/
    private TextView txtStatus;
    /** Indicates the progress of the measurement. **/
    private ProgressBar progressBar;
    /** The animation of the progress bar. **/
    private ObjectAnimator animation;
    /** continously calculates the pulse **/
    private HRVContinousPulse pulseCalculator;

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
        pulse = (TextView) rootView.findViewById(R.id.pulseValue);
        txtStatus = (TextView) rootView.findViewById(R.id.measure_status);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        connectToBandFAB = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.connect_band_float_button);
        connectToAntPlusFAB = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.connect_antplus_float_button);
        disconnectDevices = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.disconnect_devices);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        pulseCalculator = new HRVContinousPulse(10);
        pulseCalculator.addHRVParameterChangedListener(this);
        hrvRRIntervalDevice = getDevice(DeviceID.values()[sharedPreferences.getInt(SELECTED_DEVICE_ID, 0)]);
        if (hrvRRIntervalDevice != null) {
            addDeviceListeners(this);
        }

        MeasurementClickListener clickListener = new MeasurementClickListener(this);

        progressBar.setOnClickListener(clickListener);
        if (connectToBandFAB != null) {
            connectToBandFAB.setOnClickListener(clickListener);
        }
        if (connectToAntPlusFAB != null) {
            connectToAntPlusFAB.setOnClickListener(clickListener);
        }
        if (disconnectDevices != null) {
            disconnectDevices.setOnClickListener(clickListener);
        }


        setProgressBarSize();

        initAnimation();

        return rootView;
    }

    /**
     * Returns the measurement duration in ms.
     * @return measurement duration in ms.
     */
    private int getDuration() {
        String durationPrefVal = sharedPreferences.getString("recording_length", "128");
        return Integer.parseInt(durationPrefVal) * 1000;
    }

    /**
     * Adds listeners to the measurement device.
     * @param measuringFragment the measure fragment.
     */
    private void addDeviceListeners(MeasuringFragment measuringFragment) {
        hrvRRIntervalDevice.addDeviceListener(measuringFragment);
        hrvRRIntervalDevice.addRRIntervalListener(measuringFragment);
        hrvRRIntervalDevice.addRRIntervalListener(pulseCalculator);
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
            default: return null;
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
     * Initialize the animation of the progress bar.
     */
    private void initAnimation() {
        animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 1000);
        animation.setInterpolator (new LinearInterpolator());
        animation.addListener(new ProgressBarAnimatorListener());
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
        progressBar.setProgress(progressBar.getMax());
        setConnectionButtonClickable(true);

        initAnimation();

        updateTextView(getActivity(), rrStatus, "0,00");
        updateTextView(getActivity(), txtStatus, getResources().getString(R.string.measure_fragment_press_to_start));
        updateTextView(getActivity(), pulse, getResources().getString(R.string.measure_fragment_standard_pulse_value));
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
                    animation.setDuration(getDuration());
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
            case CONNECTING:
                updateTextView(getActivity(), txtStatus, getResources().getString(R.string.msg_connecting));
                break;
            case CONNECTED:
                updateTextView(getActivity(), txtStatus, getResources().getString(R.string.title_activity_start_measuring));
                break;
            case DISCONNECTED:
                updateTextView(getActivity(), txtStatus, getResources().getString(R.string.error_band_not_paired));
        }
    }

    @Override
    public void newRRInterval(HRVRRIntervalEvent event) {
        String format = String.format("%.2f", event.getRr());
        updateTextView(getActivity(), rrStatus, format);
    }

    @Override
    public void parameterChanged(HRVParameter eventArgs) {
        String format = ((Integer)((int)eventArgs.getValue())).toString();
        updateTextView(getActivity(), pulse, format);
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
     * Animator Listener for the progress bar.
     */
    private class ProgressBarAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator a) {
            setConnectionButtonClickable(false);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (hrvRRIntervalDevice == null) {
                showSnackbar(view, "This should not happen...");
            }
            hrvRRIntervalDevice.stopMeasuring();

            double[] rrIntervals = ArrayUtils.toPrimitive(hrvRRIntervalDevice.getRRIntervals().toArray(new Double[0]));

            Intent intent = new Intent(getContext(), HRVMeasurementActivity.class);
            intent.putExtra(HRV_PARAMETER_ID, createMeasurement(rrIntervals, new Date()));
            startActivity(intent);

            hrvRRIntervalDevice.clearRRIntervals();
            resetProgress();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            animation.removeAllListeners();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            //is not used at the moment
        }

        /**
         * Calculates hrv parameter from given rr interval.
         * @param rrInterval measured rr interval.
         * @param time time the measurement was created.
         * @return calculated hrv parameter from given rr interval.
         */
        private Measurement createMeasurement(double[] rrInterval, Date time) {
            //start calculation
            RRData.createFromRRInterval(rrInterval, TimeUnitConverter.TimeUnit.SECOND);

            Measurement.MeasurementBuilder measurementBuilder = Measurement.from(time, rrInterval);
            return measurementBuilder.build();
        }
    }

    /**
     * Click listener handling the clicks in this fragment.
     */
    private class MeasurementClickListener implements View.OnClickListener {

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
                default: break;
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
                hrvRRIntervalDevice.notifyDeviceStatusChanged(HRVDeviceStatus.DISCONNECTED);
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
