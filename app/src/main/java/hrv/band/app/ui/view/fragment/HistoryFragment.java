package hrv.band.app.ui.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HistoryViewModel;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawDayStrategy;
import hrv.band.app.ui.view.adapter.HistoryViewAdapter;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 03.11.2017
 */


public class HistoryFragment extends Fragment {
    private HistoryViewModel historyViewModel;
    private ColumnChartView chart;
    private HistoryViewAdapter adapter;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);

        historyViewModel = ViewModelProviders.of(HistoryFragment.this).get(HistoryViewModel.class);

        chart = rootView.findViewById(R.id.history_chart);

        RecyclerView recyclerView = rootView.findViewById(R.id.history_values);


        adapter = new HistoryViewAdapter(HRVParameterEnum.BAEVSKY, new ArrayList<Measurement>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        historyViewModel = ViewModelProviders.of(HistoryFragment.this).get(HistoryViewModel.class);

        historyViewModel.getMeasurements(new Date()).observe(getActivity(), new android.arch.lifecycle.Observer<List<Measurement>>() {
            @Override
            public void onChanged(@Nullable List<Measurement> measurements) {
                adapter.addItems(measurements);
                historyViewModel.drawChart(new ChartDrawDayStrategy(), chart, measurements, HRVParameterEnum.BAEVSKY, getActivity());
            }
        });

        return rootView;
    }
}
