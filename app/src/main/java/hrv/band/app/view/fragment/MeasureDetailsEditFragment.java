package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.view.adapter.AbstractCategoryAdapter;
import hrv.band.app.view.adapter.MeasurementCategoryAdapter;

/**
 * Created by Thomas on 10.08.2016.
 */
public class MeasureDetailsEditFragment extends Fragment {

    private View rootView;

    public MeasureDetailsEditFragment() {
    }

    public static MeasureDetailsEditFragment newInstance(HRVParameters parameter) {
        return new MeasureDetailsEditFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_measure_details_edit, container, false);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.measure_categories);
        AbstractCategoryAdapter spinnerArrayAdapter = new MeasurementCategoryAdapter(getContext());

        spinner.setAdapter(spinnerArrayAdapter);

        return rootView;
    }

    public float getRating() {
        if (rootView == null) {
            return 0;
        }
        RatingBar ratingbar = (RatingBar) rootView.findViewById(R.id.measure_rating);
        return ratingbar.getRating();
    }

    public MeasurementCategoryAdapter.MeasureCategory getCategory() {
        if (rootView == null) {
            return MeasurementCategoryAdapter.MeasureCategory.GENERAL;
        }
        Spinner spinner = (Spinner) rootView.findViewById(R.id.measure_categories);
        int position = spinner.getSelectedItemPosition();
        return MeasurementCategoryAdapter.MeasureCategory.values()[position];
    }

    public String getNote() {
        if (rootView == null) {
            return "";
        }
        TextView note = (TextView) rootView.findViewById(R.id.measure_note);
        return note.getText().toString();
    }
}
