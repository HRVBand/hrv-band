package hrv.band.app.view.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
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
import hrv.band.app.RRInterval.HRVRRDeviceListener;
import hrv.band.app.RRInterval.HRVRRIntervalDevice;
import hrv.band.app.RRInterval.Interval;
import hrv.band.app.RRInterval.antplus.AntPlusRRDataDevice;
import hrv.band.app.RRInterval.msband.MSBandRRIntervalDevice;
import hrv.band.app.view.MeasureDetailsActivity;
import hrv.band.app.view.UiHandlingUtil;

/**
 * Created by s_czogal on 23.06.2016.
 */

public class MeasuringFragment extends Fragment implements HRVRRDeviceListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    private int duration = 60000;
    private HRVRRIntervalDevice HRVRRIntervalDevice;
    private TextView rrStatus;
    private TextView txtStatus;
    private ProgressBar progressBar;
    private ObjectAnimator animation;
    private com.github.clans.fab.FloatingActionButton connectToBandFAB;
    private com.github.clans.fab.FloatingActionButton connectToAntPlusFAB;


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
        connectToBandFAB = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.connect_band_float_button);
        connectToAntPlusFAB = (com.github.clans.fab.FloatingActionButton) rootView.findViewById(R.id.connect_antplus_float_button);


        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HRVRRIntervalDevice.tryStartRRIntervalMeasuring();
            }
        });

        connectToBandFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HRVRRIntervalDevice = new MSBandRRIntervalDevice(getActivity(), txtStatus, rrStatus);
                HRVRRIntervalDevice.addDeviceListener(MeasuringFragment.this);
                HRVRRIntervalDevice.connect();
            }
        });

        connectToAntPlusFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HRVRRIntervalDevice = new AntPlusRRDataDevice(getContext(), getActivity());
                HRVRRIntervalDevice.addDeviceListener(MeasuringFragment.this);
                HRVRRIntervalDevice.connect();
            }
        });


        setProgressBarSize();

        setupAnimation(new Interval(new Date()));

        return rootView;
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

    public HRVParameters calculate(Interval interval) {
        //start calculation
        CubicSplineInterpolation inter = new CubicSplineInterpolation();
        FastFourierTransform fft = new FastFourierTransform(4096);

        Calculation calc = new Calculation(fft, inter);
        HRVParameters results = calc.Calculate(interval);
        results.setTime(new Date());
        return results;
    }

    public HRVRRIntervalDevice getRRInterval() {
        return HRVRRIntervalDevice;
    }

    public void setupAnimation(final Interval interval) {

        animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 1000); // see this max value coming back here, we animale towards that value
        animation.setDuration (duration); //in milliseconds
        animation.setInterpolator (new LinearInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator a) {
                progressBar.setClickable(false);
                connectToBandFAB.setClickable(false);

               // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                 //       WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                HRVRRIntervalDevice.stopMeasuring();
                if (interval == null) {
                    return;
                }
                UiHandlingUtil.updateTextView(getActivity(), txtStatus, "Calculating");
                interval.SetRRInterval(HRVRRIntervalDevice.getRRIntervals().toArray(new Double[0]));

                Intent intent = new Intent(getContext(), MeasureDetailsActivity.class);
                intent.putExtra(HRV_PARAMETER_ID, calculate(interval));
                startActivity(intent);
                resetProgress();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void stopMeasuring() {
        HRVRRIntervalDevice.stopMeasuring();
    }

    private void resetProgress() {
        progressBar.setClickable(true);
        connectToBandFAB.setClickable(true);
        //view.setOnTouchListener(null);
        //touchListener.clearTouchable();
        //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        UiHandlingUtil.updateTextView(getActivity(), rrStatus, "0,00");
        UiHandlingUtil.updateTextView(getActivity(), txtStatus, getResources().getString(R.string.measure_fragment_press_to_start));
    }

    @Override
    public void deviceStartedMeasurement() {
        txtStatus.setText(R.string.msg_hold_still);

        if (animation != null) {
            animation.start();
        }
    }
}
