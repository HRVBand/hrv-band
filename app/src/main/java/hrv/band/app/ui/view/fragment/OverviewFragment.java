package hrv.band.app.ui.view.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Date;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HistoryViewModel;
import hrv.band.app.ui.view.activity.HistoryActivity;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawDayStrategy;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawMonthStrategy;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawWeekStrategy;
import hrv.band.app.ui.view.util.DateUtil;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Fragment allowing user to start measurement.
 */
public class OverviewFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private ColumnChartView todayChart;
    private ColumnChartView weekChart;
    private ColumnChartView monthChart;

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
        View rootView = inflater.inflate(R.layout.main_fragment_overview, container, false);

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        todayChart = rootView.findViewById(R.id.overview_chart_today);
        weekChart = rootView.findViewById(R.id.overview_chart_week);
        monthChart = rootView.findViewById(R.id.overview_chart_month);
        CardView todayCard = rootView.findViewById(R.id.overview_card_today);
        CardView weekCard = rootView.findViewById(R.id.overview_card_week);
        CardView monthCard = rootView.findViewById(R.id.overview_card_month);
        todayCard.setOnClickListener(new OnClickChartListener(getActivity(), HistoryActivity.HistoryTodayActivity.class));
        weekCard.setOnClickListener(new OnClickChartListener(getActivity(), HistoryActivity.HistoryWeekActivity.class));
        monthCard.setOnClickListener(new OnClickChartListener(getActivity(), HistoryActivity.HistoryMonthActivity.class));

        getMeasurements(new Date());

        return rootView;
    }

    private void getMeasurements(final Date date) {
        historyViewModel.getMeasurements(DateUtil.getStartOfDay(date), DateUtil.getEndOfDay(date)).observe(getActivity(), new android.arch.lifecycle.Observer<List<Measurement>>() {
            @Override
            public void onChanged(@Nullable List<Measurement> measurements) {
                historyViewModel.drawChart(new ChartDrawDayStrategy(), todayChart, measurements, HRVParameterEnum.BAEVSKY ,getActivity());
            }
        });
        historyViewModel.getMeasurements(DateUtil.getStartOfWeek(date), DateUtil.getEndOfWeek(date)).observe(getActivity(), new android.arch.lifecycle.Observer<List<Measurement>>() {
            @Override
            public void onChanged(@Nullable List<Measurement> measurements) {
                historyViewModel.drawChart(new ChartDrawWeekStrategy(), weekChart, measurements, HRVParameterEnum.BAEVSKY ,getActivity());
            }
        });
        historyViewModel.getMeasurements(DateUtil.getStartOfMonth(date), DateUtil.getStartOfMonth(date)).observe(getActivity(), new android.arch.lifecycle.Observer<List<Measurement>>() {
            @Override
            public void onChanged(@Nullable List<Measurement> measurements) {
                historyViewModel.drawChart(new ChartDrawMonthStrategy(date), monthChart, measurements, HRVParameterEnum.BAEVSKY ,getActivity());
            }
        });
    }



    private static class OnClickChartListener implements View.OnClickListener {
        private Class<? extends HistoryActivity> historyActivity;
        private Activity activity;

        OnClickChartListener(Activity activity, Class<? extends HistoryActivity> historyActivity) {
            this.activity = activity;
            this.historyActivity = historyActivity;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, historyActivity);
            activity.startActivity(intent);
        }

    }
}
