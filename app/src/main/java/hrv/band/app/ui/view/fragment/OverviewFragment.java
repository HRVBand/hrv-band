package hrv.band.app.ui.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.model.HRVParameterSettings;
import hrv.band.app.ui.view.activity.HistoryActivity;
import hrv.band.app.ui.view.adapter.OverviewValueAdapter;
import hrv.calc.parameter.HRVParameterEnum;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Fragment showing available HRV parameters.
 */
public class OverviewFragment extends Fragment {

    public static final String VALUE_TYPE = "value_type";

    /**
     * Returns a new instance of this fragment.
     *
     * @return a new instance of this fragment.
     */
    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.main_fragment_overview, container, false);
        ListView listView = rootView.findViewById(R.id.overview_value_list);
        final OverviewValueAdapter adapter = new OverviewValueAdapter(getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                List<HRVParameterEnum> parameterEnumList = new ArrayList<>(HRVParameterSettings.DefaultSettings.visibleHRVParameters);

                intent.putExtra(VALUE_TYPE, parameterEnumList.get(position));
                startActivity(intent);
            }

        });

        return rootView;
    }
}
