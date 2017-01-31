package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.view.MainActivity;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment showing the details of a measurement.
 */
public class MeasuredDetailsFragment extends Fragment {

    /**
     * Returns a new instance of this fragment.
     * @param parameter the hrv parameter to get details from.
     * @return a new instance of this fragment.
     */
    public static MeasuredDetailsFragment newInstance(HRVParameters parameter) {
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

        HRVParameters parameter = getArguments().getParcelable(MainActivity.HRV_VALUE);

        TextView dateTxt =(TextView) rootView.findViewById(R.id.hrv_date);
        TextView ratingTxt =(TextView) rootView.findViewById(R.id.hrv_rating);
        TextView categoryTxt =(TextView) rootView.findViewById(R.id.hrv_category);
        ImageView categoryIcon =(ImageView) rootView.findViewById(R.id.hrv_category_icon);
        TextView commentTxt =(TextView) rootView.findViewById(R.id.hrv_comment);

        //dateTxt.setText(parameter.getTime().toString());
        dateTxt.setText(formatDateTime(parameter != null ? parameter.getTime() : null));
        ratingTxt.setText(new DecimalFormat("#.#").format(parameter != null ? parameter.getRating() : 0).concat("/5"));
        categoryTxt.setText(parameter != null ? parameter.getCategory().getText(getResources()) : null);
        categoryIcon.setImageDrawable(parameter != null ? parameter.getCategory().getIcon(getResources()) : null);
        commentTxt.setText(parameter != null ? parameter.getNote() : null);

        return rootView;
    }

    /**
     * Formats the date and time for the measurement depending on user local.
     * @param date the date to format.
     * @return the formatted date and time for the measurement depending on user local.
     */
    private String formatDateTime(Date date) {
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(getContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        return dateFormat.format(date) + ", " + timeFormat.format(date);
    }
}
