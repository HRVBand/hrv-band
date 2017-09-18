package hrv.band.app.ui.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.model.HRVParameterUnitAdapter;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HistoryViewModel;
import hrv.band.app.ui.view.activity.IHistoryView;
import hrv.band.app.ui.view.adapter.HistoryViewAdapter;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.view.ColumnChartView;

import static hrv.band.app.ui.view.activity.HistoryActivity.RESULT_DELETED;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Fragment allowing user to start measurement.
 */
public class HistoryFragment extends Fragment {

    /**
     * Key value for the hrv type of this fragment.
     **/
    private static final String ARG_SECTION_VALUE = "sectionValue";
    /**
     * The adapter holding the parameters to show.
     **/
    private HistoryViewAdapter adapter;
    /**
     * The chart showing the parameters in this fragment.
     **/
    private ColumnChartView mChart;
    /**
     * The hrv type that this fragment is showing.
     **/
    private HRVParameterEnum hrvType;

    private IHistoryView historyView;

    private HistoryViewModel historyViewModel;

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
        RecyclerView recyclerView = rootView.findViewById(R.id.stats_measure_history);
        hrvType = (HRVParameterEnum) getArguments().getSerializable(ARG_SECTION_VALUE);

        TextView desc = rootView.findViewById(R.id.stats_value_desc);
        TextView type = rootView.findViewById(R.id.stats_type);

        desc.setText(hrvType.toString());
        type.setText(getUnit());


        adapter = new HistoryViewAdapter(hrvType, new ArrayList<Measurement>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        historyViewModel = ViewModelProviders.of(HistoryFragment.this).get(HistoryViewModel.class);

        historyViewModel.getMeasurements(new Date()).observe(getActivity(), new android.arch.lifecycle.Observer<List<Measurement>>() {
            @Override
            public void onChanged(@Nullable List<Measurement> measurements) {
                adapter.addItems(measurements);
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

    /*@Override
    public void update(Observable observable, Object o) {
        parameters = historyView.getMeasurements();
        if (adapter != null) {
            adapter.setDataset(parameters);
            adapter.notifyDataSetChanged();
            historyView.drawChart(mChart, hrvType, getActivity());
        }
    }*/
}
