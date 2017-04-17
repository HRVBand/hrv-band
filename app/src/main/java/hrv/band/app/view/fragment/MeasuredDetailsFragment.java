package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hrv.band.app.control.Measurement;
import hrv.band.app.R;
import hrv.band.app.view.activity.MainActivity;
import hrv.band.app.view.presenter.IMeasurementDetailsPresenter;
import hrv.band.app.view.presenter.MeasurementDetailsPresenter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment showing the details of a measurement.
 */
public class MeasuredDetailsFragment extends Fragment {

    /**
     * Returns a new instance of this fragment.
     *
     * @param parameter the hrv parameter to get details from.
     * @return a new instance of this fragment.
     */
    public static MeasuredDetailsFragment newInstance(Measurement parameter) {
        MeasuredDetailsFragment fragment = new MeasuredDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.HRV_VALUE, parameter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hrv_value_fragment_details, container, false);

        IMeasurementDetailsPresenter presenter = new MeasurementDetailsPresenter(
                (Measurement) getArguments().getParcelable(MainActivity.HRV_VALUE), getActivity());

        TextView dateTxt = (TextView) rootView.findViewById(R.id.hrv_date);
        TextView ratingTxt = (TextView) rootView.findViewById(R.id.hrv_rating);
        TextView categoryTxt = (TextView) rootView.findViewById(R.id.hrv_category);
        ImageView categoryIcon = (ImageView) rootView.findViewById(R.id.hrv_category_icon);
        TextView commentTxt = (TextView) rootView.findViewById(R.id.hrv_comment);

        dateTxt.setText(presenter.getDate());
        ratingTxt.setText(presenter.getRating());
        categoryTxt.setText(presenter.getCategory());
        categoryIcon.setImageDrawable(presenter.getCategoryIcon());
        commentTxt.setText(presenter.getNote());

        return rootView;
    }
}
