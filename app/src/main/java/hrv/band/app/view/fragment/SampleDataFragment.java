package hrv.band.app.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import hrv.band.app.control.Measurement;
import hrv.band.app.R;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.sqlite.HRVSQLController;
import hrv.band.app.view.adapter.MeasurementCategoryAdapter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Dialog asking user to create sample data.
 */
public class SampleDataFragment extends DialogFragment {

    /** Count of rr intervals to create. **/
    private static final int RR_COUNT = 50;
    /** Count of samples to create. **/
    private static final int SAMPLE_COUNT = 20;
    /** Key value that indicates if the activity calling this dialog should be closed. **/
    private static final String ARG_CLOSE_VALUE = "arg_close_value";

    /**
     * Returns a new instance of this fragment.
     * @param close true if calling activity should be closed after dialog closes, false otherwise.
     * @return a new instance of this fragment.
     */
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

        final View view =  View.inflate(getActivity(), R.layout.dialog_simple_text, null);

        TextView textView = (TextView) view.findViewById(R.id.dialog_textview);
        textView.setText(getResources().getString(R.string.sample_text));

        builder.setView(view)
                .setPositiveButton(R.string.common_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i = 0; i < SAMPLE_COUNT; i++) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date());
                            cal.add(Calendar.DAY_OF_MONTH, i);

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
        return builder.create();
    }

    /**
     * Creates a hrv sample.
     * @param date of the hrv sample.
     */
    private void createSampleData(Date date) {
        double[] rrValues = new double[RR_COUNT];

        for (int i = 0; i < rrValues.length; i++) {
            rrValues[i] = getRandomDouble(0.5, 1.5);
        }

        Measurement.MeasurementBuilder measurementBuilder = new Measurement.MeasurementBuilder(date, rrValues);
        Measurement hrv = measurementBuilder
                .rating(4.4)
                .category(MeasurementCategoryAdapter.MeasureCategory.SPORT)
                .note("This is a sample data")
                .build();

        IStorage storage = new HRVSQLController();
        storage.saveData(getActivity(), hrv);
    }

    /**
     * Returns a random double within the giving range.
     * @param min value of the random value.
     * @param max value of the random value.
     * @return a random double within the giving range.
     */
    private double getRandomDouble(double min, double max) {
        Random r = new Random();
        return max + (min - max) * r.nextDouble();
    }

    /**
     * Closes the calling activity if the context fits.
     */
    private void closeCallingActivity() {
        if (getArguments().getBoolean(ARG_CLOSE_VALUE)) {
            getActivity().finish();
        }
    }
}
