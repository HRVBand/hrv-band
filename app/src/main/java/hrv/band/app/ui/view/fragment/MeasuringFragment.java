package hrv.band.app.ui.view.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import hrv.band.app.R;
import hrv.band.app.device.ConnectionManager;
import hrv.band.app.device.HRVDeviceStatus;
import hrv.band.app.device.HRVRRDeviceListener;
import hrv.band.app.device.HRVRRIntervalDevice;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.IMeasuringPresenter;
import hrv.band.app.ui.presenter.MeasuringPresenter;
import hrv.band.app.ui.view.activity.SavableMeasurementActivity;
import hrv.band.app.ui.view.fragment.measuring.listener.ConnectionClickListener;
import hrv.band.app.ui.view.fragment.measuring.listener.MeasurementClickListener;
import hrv.band.app.ui.view.fragment.measuring.listener.ProgressBarAnimatorListener;
import hrv.calc.continous.HRVContinousHeartRate;
import hrv.calc.continous.HRVParameterChangedListener;
import hrv.calc.continous.HRVRRIntervalEvent;
import hrv.calc.continous.HRVRRIntervalListener;
import hrv.calc.parameter.HRVParameter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Fragment allowing user to start measurement.
 */
public class MeasuringFragment extends Fragment implements HRVRRDeviceListener, HRVRRIntervalListener, HRVParameterChangedListener, IMeasuringView {

    /**
     * Key value for the calculated hrv parameter.
     **/
    private static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    /**
     * Device to measure rr interval.
     **/
    private HRVRRIntervalDevice hrvRRIntervalDevice;
    /**
     * Text view showing the current rr interval.
     **/
    private TextView rrStatus;
    /**
     * Text view showing the current pulse
     **/
    private TextView pulse;
    /**
     * Text view showing the time left of the measurement.
     **/
    private TextView txtMeasureTime;
    /**
     * Text view showing the status of the measurement.
     **/
    private TextView txtStatus;
    /**
     * Indicates the progress of the measurement.
     **/
    private ProgressBar progressBar;
    /**
     * The animation of the progress bar.
     **/
    private ObjectAnimator animation;
    /**
     * continously calculates the pulse
     **/
    private HRVContinousHeartRate pulseCalculator;
    private com.github.clans.fab.FloatingActionMenu menuDown;
    /**
     * Button to connect with the ms band.
     **/
    private com.github.clans.fab.FloatingActionButton connectToBandFAB;
    /**
     * Button to connect with ant+ device.
     **/
    private com.github.clans.fab.FloatingActionButton connectToAntPlusFAB;
    /**
     * Button to disconnect with devices.
     **/
    private com.github.clans.fab.FloatingActionButton disconnectDevices;
    /**
     * The timer that shows the time to go for the measurement.
     **/
    private CountDownTimer countDownTimer;
    /**
     * Root view of this fragment.
     **/
    private View view;

    private IMeasuringPresenter presenter;


    /**
     * Returns a new instance of this fragment.
     *
     * @return a new instance of this fragment.
     */
    public static MeasuringFragment newInstance() {
        return new MeasuringFragment();
    }

    /**
     * Shows a snack bar with the given message in the given view.
     *
     * @param view the view the snack bar should appear.
     * @param msg  the message the snackbar should show.
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
     *
     * @param activity the activity in which the thread should run.
     * @param txt      the text view to be set.
     * @param string   the string the txt should show.
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment_measure, container, false);
        view = rootView.findViewById(R.id.measure_fragment);

        presenter = new MeasuringPresenter(this);
        ConnectionManager connectionManager = new ConnectionManager(getActivity());

        rrStatus = (TextView) rootView.findViewById(R.id.rrStatus);
        pulse = (TextView) rootView.findViewById(R.id.pulseValue);
        txtStatus = (TextView) rootView.findViewById(R.id.measure_status);
        txtMeasureTime = (TextView) rootView.findViewById(R.id.measureTime);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        menuDown = (com.github.clans.fab.FloatingActionMenu) getActivity().findViewById(R.id.menu_down);
        connectToBandFAB = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.connect_band_float_button);
        connectToAntPlusFAB = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.connect_antplus_float_button);
        disconnectDevices = (com.github.clans.fab.FloatingActionButton) getActivity().findViewById(R.id.disconnect_devices);

        txtMeasureTime.setText(String.valueOf(presenter.getDuration() / 1000));

        pulseCalculator = new HRVContinousHeartRate(10);
        pulseCalculator.addHRVParameterChangedListener(this);

        hrvRRIntervalDevice = connectionManager.getDevice();
        if (hrvRRIntervalDevice != null) {
            addDeviceListeners();
        }

        MeasurementClickListener clickListener = new MeasurementClickListener(this);

        progressBar.setOnClickListener(clickListener);

        ConnectionClickListener connectClickListener = new ConnectionClickListener(connectionManager, this);

        if (connectToBandFAB != null) {
            connectToBandFAB.setOnClickListener(connectClickListener);
        }
        if (connectToAntPlusFAB != null) {
            connectToAntPlusFAB.setOnClickListener(connectClickListener);
        }
        if (disconnectDevices != null) {
            disconnectDevices.setOnClickListener(connectClickListener);
        }

        setProgressBarSize();

        initAnimation();

        return rootView;
    }

    /**
     * Starts a countdown that shows how long the measurement will go on.
     */
    private void startCountdown() {
        countDownTimer = new CountDownTimer(presenter.getDuration(), 90) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtMeasureTime.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                txtMeasureTime.setText(String.valueOf(presenter.getDuration()));
            }
        }.start();
    }

    /**
     * Adds listeners to the measurement device.
     */
    @Override
    public void addDeviceListeners() {
        hrvRRIntervalDevice.addDeviceListener(this);
        hrvRRIntervalDevice.addRRIntervalListener(this);
        hrvRRIntervalDevice.addRRIntervalListener(pulseCalculator);
    }

    @Override
    public HRVRRIntervalDevice getHrvRrIntervalDevice() {
        return hrvRRIntervalDevice;
    }

    @Override
    public void setHrvRrIntervalDevice(HRVRRIntervalDevice hrvRRIntervalDevice) {
        this.hrvRRIntervalDevice = hrvRRIntervalDevice;
    }

    @Override
    public void toggleDeviceMenuButton(boolean toggle) {
        menuDown.toggle(toggle);
    }

    @Override
    public void showCancelDialog() {
        CancelMeasuringDialogFragment.newInstance().show(getActivity().getFragmentManager(), "dialog");
    }

    @Override
    public boolean isAnimationRunning() {
        return animation.isRunning();
    }

    @Override
    public void makeToast(int messageId) {
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.msg_select_device), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnackbar(String message) {
        showSnackbar(view, message);
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

        setStatusTextSize(width);

        progressBar.setLayoutParams(params);
    }

    /**
     * Sets the font size of the RR status text view.
     *
     * @param width the width of the phone screen.
     */
    private void setStatusTextSize(int width) {
        float density = getResources().getDisplayMetrics().density;
        rrStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, (width) / (density * 5));
    }

    /**
     * Initialize the animation of the progress bar.
     */
    private void initAnimation() {
        animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.addListener(new ProgressBarAnimatorListener(this));
    }

    /**
     * Stops the measuring of the device and resets the ui.
     */
    @Override
    public void stopMeasuring() {
        if (hrvRRIntervalDevice != null && animation != null) {
            hrvRRIntervalDevice.stopMeasuring();
            hrvRRIntervalDevice.clearRRIntervals();
            animation.cancel();
            resetProgress();
        }
    }

    /**
     * Resets the ui.
     */
    @Override
    public void resetProgress() {
        progressBar.setProgress(progressBar.getMax());
        setConnectionButtonClickable(true);

        initAnimation();

        updateTextView(getActivity(), rrStatus, "0,00");
        updateTextView(getActivity(), txtStatus, getResources().getString(R.string.measure_fragment_press_to_start));
        updateTextView(getActivity(), pulse, getResources().getString(R.string.measure_fragment_standard_pulse_value));

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        updateTextView(getActivity(), txtMeasureTime, String.valueOf(presenter.getDuration() / 1000));

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Sets the clickable of (dis)connect buttons.
     *
     * @param clickable true if buttons should be clickable, false otherwise.
     */
    @Override
    public void setConnectionButtonClickable(boolean clickable) {
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
                    animation.setDuration(presenter.getDuration());
                    animation.start();
                    startCountdown();
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        String format = ((Integer) ((int) eventArgs.getValue())).toString();
        updateTextView(getActivity(), pulse, format);
    }

    @Override
    public Activity getParentActivity() {
        return getActivity();
    }

    @Override
    public void startMeasurementActivity() {
        Measurement measurement = presenter.createMeasurement(hrvRRIntervalDevice.getRRIntervals(), new Date());
        if (measurement != null) {
            startMeasurementActivity(measurement);
        } else {
            showAlertDialog(R.string.error, R.string.error_defective_rr_data);
        }
    }

    private void startMeasurementActivity(Measurement measurement) {
        Intent intent = new Intent(getContext(), SavableMeasurementActivity.class);
        intent.putExtra(MeasuringFragment.HRV_PARAMETER_ID, measurement);
        startActivity(intent);
    }

    private void showAlertDialog(int title, int message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
