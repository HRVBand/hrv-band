package hrv.band.app.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import hrv.band.app.Control.Calculation;
import hrv.band.app.Control.HRVParameters;
import hrv.band.app.Fourier.FastFourierTransform;
import hrv.band.app.Interpolation.CubicSplineInterpolation;
import hrv.band.app.R;
import hrv.band.app.RRInterval.Interval;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.SQLite.SQLController;

/**
 * Created by thomcz on 06.01.2017.
 */

public class SampleDataFragment extends DialogFragment {

    private static final int rrCount = 50;
    private static final int sampleCount = 4;

    private static final String ARG_CLOSE_VALUE = "arg_close_value";

    public static SampleDataFragment newInstance(boolean close) {
        SampleDataFragment fragment = new SampleDataFragment();

        Bundle args = new Bundle();
        args.putBoolean(ARG_CLOSE_VALUE, close);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_text_dialog, null);

        TextView textView = (TextView) view.findViewById(R.id.dialog_textview);
        textView.setText(getResources().getString(R.string.sample_text));

        builder.setView(view)
                .setPositiveButton(R.string.common_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i = 0; i < sampleCount; i++) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date());
                            cal.add(Calendar.HOUR_OF_DAY, i);

                            createSampleData(cal.getTime());

                            closeCallingActivity();
                        }
                    }
                })
                .setNegativeButton(R.string.common_no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SampleDataFragment.this.getDialog().cancel();
                        closeCallingActivity();
                    }
                });
        builder.setTitle(getResources().getString(R.string.sample_title));
        //builder.setTitle(getResources().getString(R.string.common_export));
        return builder.create();
    }

    private void createSampleData(Date date) {
        double[] rrValues = new double[rrCount];

        for (int i = 0; i < rrValues.length; i++) {
            rrValues[i] = getRandomDouble(0.5, 1.5);
        }

        HRVParameters hrv = calculate(new Interval(date, rrValues));
        hrv.setRating(getRandomDouble(0.0, 5.0));
        hrv.setNote("This is a sample data");

        IStorage storage = new SQLController();
        storage.saveData(getActivity(), hrv);
    }

    private double getRandomDouble(double min, double max) {
        Random r = new Random();
        return max + (min - max) * r.nextDouble();
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

    private void closeCallingActivity() {
        if (getArguments().getBoolean(ARG_CLOSE_VALUE)) {
            getActivity().finish();
        }
    }
}
