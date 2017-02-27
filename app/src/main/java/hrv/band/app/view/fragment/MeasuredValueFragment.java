package hrv.band.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import hrv.band.app.control.Measurement;
import hrv.band.app.R;
import hrv.band.app.view.MainActivity;
import hrv.band.app.view.WebActivity;
import hrv.band.app.view.adapter.ValueAdapter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment showing the hrv values from a measurement.
 */
public class MeasuredValueFragment extends Fragment {

    /**
     * Returns a new instance of this fragment.
     * @param parameter the hrv parameter to get rr intervals from.
     * @return a new instance of this fragment.
     */
    public static MeasuredValueFragment newInstance(Measurement parameter) {
        MeasuredValueFragment fragment = new MeasuredValueFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.HRV_VALUE, parameter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.abstract_hrv_fragment_values, container, false);

        Measurement parameter = getArguments().getParcelable(MainActivity.HRV_VALUE);

        ListView listview = (ListView) rootView.findViewById(R.id.hrv_value_list);

        ValueAdapter adapter = new ValueAdapter(this.getActivity(),
                parameter);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra(WebActivity.WEBSITE_URL_ID, WebActivity.WEBSITE_PARAMETER_URL);
                startActivity(intent);
            }

        });

        return rootView;
    }
}
