package hrv.band.aurora.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

public class MeasureActivity extends AppCompatActivity {
    private int duration = 7000;
    public static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    private IRRInterval rrInterval;
    private Interval ival;
    private TextView rrStatus;
    private TextView txtStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rrStatus = (TextView) findViewById(R.id.rrStatus);
        txtStatus = (TextView) findViewById(R.id.measure_status);

        rrInterval = new MSBandRRInterval(this, txtStatus, rrStatus);
    }

    public void startMeasuring(View view) {
        startAnimation();
        ival = new Interval(new Date());
    }

    public void getDevicePermission(View view) {
        rrInterval.getDevicePermission();
    }

    private void startAnimation() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                ival.SetRRInterval(rrInterval.getRRIntervals());


                Intent intent = new Intent(getApplicationContext(), SaveValuesActivity.class);
                intent.putExtra(HRV_PARAMETER_ID, calculate());
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

    private HRVParameters calculate() {
        //start calculation
        CubicSplineInterpolation inter = new CubicSplineInterpolation();
        FastFourierTransform fft = new FastFourierTransform(4096);

        Calculation calc = new Calculation(fft, inter);
        HRVParameters results = calc.Calculate(ival);
        results.setTime(new Date());
        IStorage storage = new SharedPreferencesController();
        storage.saveData(getApplicationContext(), results);
        return results;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //reset to default
    }

    @Override
    protected void onPause() {
        super.onPause();
        rrInterval.pauseMeasuring();
    }

    @Override
    protected void onDestroy() {
        rrInterval.destroy();
        super.onDestroy();
    }

    /**
     * write data to UI-thread
     * @param string the text to write
     */
    public void appendToUI(final String string, final TextView txt) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt.setText(string);
            }
        });
    }

}
