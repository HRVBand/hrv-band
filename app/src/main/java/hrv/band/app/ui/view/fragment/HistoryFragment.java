package hrv.band.app.ui.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HistoryViewModel;
import hrv.band.app.ui.view.activity.history.chartstrategy.AbstractChartDrawStrategy;
import hrv.band.app.ui.view.adapter.HistoryViewAdapter;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 03.11.2017
 */


public abstract class HistoryFragment extends Fragment {
    protected HistoryViewModel historyViewModel;
    protected ColumnChartView chart;
    protected HistoryViewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);

        historyViewModel = ViewModelProviders.of(getActivity()).get(HistoryViewModel.class);

        chart = rootView.findViewById(R.id.history_chart);

        RecyclerView recyclerView = rootView.findViewById(R.id.history_values);


        adapter = new HistoryViewAdapter(new ArrayList<Measurement>(), false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        getMeasurements();

        return rootView;
    }

    protected abstract AbstractChartDrawStrategy getChartStrategy();

    protected abstract void getMeasurements();

    /*public static class HistoryTodayFragment extends HistoryFragment {
        public static HistoryFragment newInstance() {
            return new HistoryTodayFragment();
        }
        @Override
        public AbstractChartDrawStrategy getChartStrategy() {
            return new ChartDrawDayStrategy();
        }

        @Override
        protected void getMeasurements() {
            historyViewModel.getTodayMeasurements().observe(getActivity(), new android.arch.lifecycle.Observer<List<Measurement>>() {
                @Override
                public void onChanged(@Nullable List<Measurement> measurements) {
                    adapter.addItems(measurements);
                    historyViewModel.drawChart(getChartStrategy(), chart, measurements, HRVParameterEnum.BAEVSKY, getActivity());
                }
            });
        }
    }
    public static class HistoryWeekFragment extends HistoryFragment {
        public static HistoryFragment newInstance() {
            return new HistoryWeekFragment();
        }
        @Override
        public AbstractChartDrawStrategy getChartStrategy() {
            return new ChartDrawWeekStrategy();
        }
        @Override
        protected void getMeasurements() {
            historyViewModel.getWeekMeasurements().observe(getActivity(), new android.arch.lifecycle.Observer<List<Measurement>>() {
                @Override
                public void onChanged(@Nullable List<Measurement> measurements) {
                    adapter.addItems(measurements);
                    historyViewModel.drawChart(getChartStrategy(), chart, measurements, HRVParameterEnum.BAEVSKY, getActivity());
                }
            });
        }
    }
    public static class HistoryMonthFragment extends HistoryFragment {
        public static HistoryFragment newInstance() {
            return new HistoryMonthFragment();
        }
        @Override
        public AbstractChartDrawStrategy getChartStrategy() {
            return new ChartDrawMonthStrategy(new Date());
        }
        @Override
        protected void getMeasurements() {
            historyViewModel.getMonthMeasurements().observe(getActivity(), new android.arch.lifecycle.Observer<List<Measurement>>() {
                @Override
                public void onChanged(@Nullable List<Measurement> measurements) {
                    adapter.addItems(measurements);
                    historyViewModel.drawChart(getChartStrategy(), chart, measurements, HRVParameterEnum.BAEVSKY, getActivity());
                }
            });
        }
    }*/
}
