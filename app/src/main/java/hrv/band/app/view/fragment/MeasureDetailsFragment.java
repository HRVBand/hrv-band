package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.view.MainActivity;

/**
 * Created by Thomas on 10.08.2016.
 */
public class MeasureDetailsFragment extends Fragment {
    private HRVParameters parameter;

    public MeasureDetailsFragment() {
    }

    public static MeasureDetailsFragment newInstance(HRVParameters parameter) {
        MeasureDetailsFragment fragment = new MeasureDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.HRV_VALUE, parameter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_measure_details, container, false);

        parameter = getArguments().getParcelable(MainActivity.HRV_VALUE);

        TextView dateTxt =(TextView) rootView.findViewById(R.id.hrv_date);
        TextView ratingTxt =(TextView) rootView.findViewById(R.id.hrv_rating);
        TextView categoryTxt =(TextView) rootView.findViewById(R.id.hrv_category);
        ImageView categoryIcon =(ImageView) rootView.findViewById(R.id.hrv_category_icon);
        TextView commentTxt =(TextView) rootView.findViewById(R.id.hrv_comment);

        dateTxt.setText(parameter.getTime().toString());
        ratingTxt.setText(parameter.getRating() + "/5");
        categoryTxt.setText(parameter.getCategory().getText(getResources()));
        categoryIcon.setImageDrawable(parameter.getCategory().getIcon(getResources()));
        commentTxt.setText(parameter.getNote());

        return rootView;
    }
}
