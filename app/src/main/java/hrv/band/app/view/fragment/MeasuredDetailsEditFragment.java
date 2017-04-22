package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import hrv.band.app.R;
import hrv.band.app.view.adapter.AbstractCategoryAdapter;
import hrv.band.app.view.adapter.MeasurementCategoryAdapter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment allowing the user to set the details of a measurement.
 */
public class MeasuredDetailsEditFragment extends Fragment implements IMeasuredDetails{

    /** rootView of this Fragment. **/
    private View rootView;

    /**
     * Returns a new instance of this fragment.
     * @return a new instance of this fragment.
     */
    public static MeasuredDetailsEditFragment newInstance() {
        return new MeasuredDetailsEditFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.hrv_measurement_fragment_details, container, false);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.measure_categories);
        AbstractCategoryAdapter spinnerArrayAdapter = new MeasurementCategoryAdapter(getContext());

        spinner.setAdapter(spinnerArrayAdapter);

        return rootView;
    }

    /**
     * Returns the rating of the user for the actual measurement.
     * @return the rating of the user for the actual measurement.
     */
    @Override
    public float getRating() {
        if (rootView == null) {
            return 0;
        }
        RatingBar ratingbar = (RatingBar) rootView.findViewById(R.id.measure_rating);
        return ratingbar.getRating();
    }

    /**
     * Returns the chosen category.
     * @return the chosen category.
     */
    @Override
    public MeasurementCategoryAdapter.MeasureCategory getCategory() {
        if (rootView == null) {
            return MeasurementCategoryAdapter.MeasureCategory.GENERAL;
        }
        Spinner spinner = (Spinner) rootView.findViewById(R.id.measure_categories);
        int position = spinner.getSelectedItemPosition();
        return MeasurementCategoryAdapter.MeasureCategory.values()[position];
    }

    /**
     * Returns the note the user entered.
     * @return the note the user entered.
     */
    @Override
    public String getNote() {
        if (rootView == null) {
            return "";
        }
        TextView note = (TextView) rootView.findViewById(R.id.measure_note);
        return note.getText().toString();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
