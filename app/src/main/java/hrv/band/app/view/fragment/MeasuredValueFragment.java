package hrv.band.app.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import hrv.HRVLibFacade;
import hrv.RRData;
import hrv.band.app.R;
import hrv.band.app.control.HRVParameterSettings;
import hrv.band.app.control.HRVParameterUnitAdapter;
import hrv.band.app.control.Measurement;
import hrv.band.app.view.activity.MainActivity;
import hrv.band.app.view.activity.WebActivity;
import hrv.band.app.view.adapter.ValueAdapter;
import hrv.calc.parameter.HRVParameter;
import units.TimeUnit;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment showing the hrv values from a measurement.
 */
public class MeasuredValueFragment extends Fragment {

    /**
     * Returns a new instance of this fragment.
     * @param measurement the hrv parameter to get rr intervals from.
     * @return a new instance of this fragment.
     */
    public static MeasuredValueFragment newInstance(Measurement measurement) {
        MeasuredValueFragment fragment = new MeasuredValueFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.HRV_VALUE, measurement);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.abstract_hrv_fragment_values, container, false);

        Measurement measurement = getArguments().getParcelable(MainActivity.HRV_VALUE);

        ListView listview = (ListView) rootView.findViewById(R.id.hrv_value_list);

        HRVLibFacade hrvCalc = new HRVLibFacade(RRData.createFromRRInterval(measurement.getRRIntervals(), TimeUnit.SECOND));
        hrvCalc.setParameters(HRVParameterSettings.DefaultSettings.visibleHRVParameters);
        List<HRVParameter> params = hrvCalc.calculateParameters();
        HRVParameterUnitAdapter unitAdapter = new HRVParameterUnitAdapter();
        unitAdapter.adaptParameters(params);

        ValueAdapter adapter = new ValueAdapter(this.getActivity(),
                params);
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
