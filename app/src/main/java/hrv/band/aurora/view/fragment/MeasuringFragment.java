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
import hrv.band.aurora.storage.IStorage;
import hrv.band.aurora.storage.SharedPreferencesController;
import hrv.band.aurora.view.MainActivity;
import hrv.band.aurora.view.SaveValuesActivity;

/**
 * Created by s_czogal on 23.06.2016.
 */

public class MeasuringFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    private int duration = 9000;
    //private IRRInterval rrInterval;
    private TextView rrStatus;
    private TextView txtStatus;

    public MeasuringFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    /*public static MeasuringFragment newInstance(int sectionNumber) {
        MeasuringFragment fragment = new MeasuringFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_measure, container, false);
        /*rrStatus = (TextView) getActivity().findViewById(R.id.rrStatus);
        txtStatus = (TextView) getActivity().findViewById(R.id.measure_status);*/
        //rrInterval = new MSBandRRInterval(getActivity(), txtStatus, rrStatus);
        return rootView;
    }

    public HRVParameters calculate(Interval interval) {
        //start calculation
        CubicSplineInterpolation inter = new CubicSplineInterpolation();
        FastFourierTransform fft = new FastFourierTransform(4096);

        Calculation calc = new Calculation(fft, inter);
        HRVParameters results = calc.Calculate(interval);
        results.setTime(new Date());
        IStorage storage = new SharedPreferencesController();
        storage.saveData(getContext(), results);
        return results;
    }

    public void startAnimation(final Interval interval, final IRRInterval rrInterval, ProgressBar progressBar) {

        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 1000); // see this max value coming back here, we animale towards that value
        animation.setDuration (duration); //in milliseconds
        animation.setInterpolator (new LinearInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                rrInterval.startRRIntervalMeasuring();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rrInterval.stopMeasuring();
                interval.SetRRInterval(rrInterval.getRRIntervals());


                Intent intent = new Intent(getContext(), SaveValuesActivity.class);
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
        animation.start ();
    }
}
