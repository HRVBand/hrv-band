package hrv.band.app.ui.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import hrv.band.app.R;
import hrv.band.app.model.HRVParameterUnitAdapter;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.activity.EditableMeasurementActivity;
import hrv.band.app.ui.view.activity.IHistoryView;
import hrv.band.app.ui.view.activity.MainActivity;
import hrv.band.app.ui.view.adapter.StatisticValueAdapter;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.view.ColumnChartView;

import static hrv.band.app.ui.view.activity.HistoryActivity.RESULT_DELETED;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Fragment allowing user to start measurement.
 */
public class HistoryFragment extends Fragment implements Observer {

    /**
     * Key value for the hrv type of this fragment.
     **/
    private static final String ARG_SECTION_VALUE = "sectionValue";
    /**
     * The adapter holding the parameters to show.
     **/
    private StatisticValueAdapter adapter;
    /**
     * The chart showing the parameters in this fragment.
     **/
    private ColumnChartView mChart;
    /**
     * The hrv type that this fragment is showing.
     **/
    private HRVParameterEnum hrvType;
    /**
     * The parameters this fragment should show.
     **/
    private List<Measurement> parameters;

    private IHistoryView historyView;

    /**
     * Returns a new instance of this fragment.
     *
     * @return a new instance of this fragment.
     */
    public static HistoryFragment newInstance(HRVParameterEnum type) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SECTION_VALUE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.statistic_fragment, container, false);
        ListView listView = rootView.findViewById(R.id.stats_measure_history);

        if (historyView != null) {
            parameters = historyView.getMeasurements();
        }
        hrvType = (HRVParameterEnum) getArguments().getSerializable(ARG_SECTION_VALUE);

        TextView desc = rootView.findViewById(R.id.stats_value_desc);
        TextView type = rootView.findViewById(R.id.stats_type);

        desc.setText(hrvType.toString());
        type.setText(getUnit());


        adapter = new StatisticValueAdapter(getActivity().getApplicationContext(),
                hrvType, parameters);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getContext(), EditableMeasurementActivity.class);
                intent.putExtra(MainActivity.HRV_PARAMETER_ID, parameters.get(position));
                startActivityForResult(intent, 1);
            }

        });

        mChart = rootView.findViewById(R.id.stats_chart);

        if (historyView != null) {
            historyView.drawChart(mChart, hrvType, getActivity());
        }
        return rootView;
    }

    private String getUnit() {
        HRVParameterUnitAdapter unitAdapter = new HRVParameterUnitAdapter();
        return unitAdapter.getUnitOfParameter(hrvType);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IHistoryView) {
            historyView = (IHistoryView) context;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_DELETED) {
            historyView.updateFragments();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        parameters = historyView.getMeasurements();
        if (adapter != null) {
            adapter.setDataset(parameters);
            adapter.notifyDataSetChanged();
            historyView.drawChart(mChart, hrvType, getActivity());
        }
    }
}
