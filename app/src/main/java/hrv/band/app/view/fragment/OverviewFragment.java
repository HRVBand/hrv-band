package hrv.band.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import hrv.band.app.R;
import hrv.band.app.view.StatisticActivity;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.adapter.OverviewValueAdapter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment showing available HRV parameters.
 */
public class OverviewFragment extends Fragment {

    public static final String VALUE_TYPE = "value_type";

    /**
     * Returns a new instance of this fragment.
     * @return a new instance of this fragment.
     */
    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_fragment_overview, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.overview_value_list);
        final OverviewValueAdapter adapter = new OverviewValueAdapter(getActivity()
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getContext(), StatisticActivity.class);
                intent.putExtra(VALUE_TYPE, HRVValue.values()[position]);
                startActivity(intent);
            }

        });

        return rootView;
    }
}
