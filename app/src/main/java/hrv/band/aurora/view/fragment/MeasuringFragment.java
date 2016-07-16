package hrv.band.aurora.view.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

import hrv.band.aurora.Control.Calculation;
import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.Fourier.FastFourierTransform;
import hrv.band.aurora.Interpolation.CubicSplineInterpolation;
import hrv.band.aurora.R;
import hrv.band.aurora.RRInterval.IRRInterval;
import hrv.band.aurora.RRInterval.Interval;
import hrv.band.aurora.RRInterval.msband.MSBandRRInterval;
import hrv.band.aurora.view.MeasureDetailsActivity;

/**
 * Created by s_czogal on 23.06.2016.
 */

public class MeasuringFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    private int duration = 9000;
    private IRRInterval rrInterval;
    private TextView rrStatus;
    private TextView txtStatus;
    private ProgressBar progressBar;

    public MeasuringFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_measure, container, false);
        rrStatus = (TextView) rootView.findViewById(R.id.rrStatus);
        txtStatus = (TextView)  rootView.findViewById(R.id.measure_status);
        progressBar = (ProgressBar)  rootView.findViewById(R.id.progressBar);

        rrInterval = new MSBandRRInterval(getActivity(), txtStatus, rrStatus);
        return rootView;
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

    public IRRInterval getRRInterval() {
        return rrInterval;
    }

    public void startAnimation(final Interval interval) {

        final ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 1000); // see this max value coming back here, we animale towards that value
        animation.setDuration (duration); //in milliseconds
        animation.setInterpolator (new LinearInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator a) {
                //rrInterval.startRRIntervalMeasuring(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rrInterval.stopMeasuring();
                if (interval == null) {
                    return;
                }
                interval.SetRRInterval(rrInterval.getRRIntervals());


                Intent intent = new Intent(getContext(), MeasureDetailsActivity.class);
                intent.putExtra(HRV_PARAMETER_ID, calculate(interval));
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        rrInterval.startRRIntervalMeasuring(animation);
        //animation.start();
    }
}
