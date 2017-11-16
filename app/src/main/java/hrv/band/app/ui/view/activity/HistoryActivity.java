package hrv.band.app.ui.view.activity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HistoryPresenter;
import hrv.band.app.ui.presenter.HistoryViewModel;
import hrv.band.app.ui.presenter.IHistoryPresenter;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawDayStrategy;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawMonthStrategy;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawWeekStrategy;
import hrv.band.app.ui.view.adapter.HistoryViewAdapter;
import hrv.band.app.ui.view.adapter.SectionPagerAdapter;
import hrv.band.app.ui.view.fragment.CalenderPickerFragment;
import hrv.band.app.ui.view.fragment.HistoryFragment;
import hrv.band.app.ui.view.util.DateUtil;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.view.ColumnChartView;

import static hrv.band.app.ui.view.util.DateUtil.formatDate;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This Activity holds the fragments which each shows a HRV value.
 */
public abstract class HistoryActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    protected HistoryViewModel historyViewModel;

    protected ColumnChartView chart;
    protected TextView dateView;
    protected HistoryViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setupToolbar();

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        chart = findViewById(R.id.history_chart);
        dateView = findViewById(R.id.history_date);

        RecyclerView recyclerView = findViewById(R.id.history_values);

        adapter = new HistoryViewAdapter(new ArrayList<Measurement>(), shouldDateBeDisplayedInList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setMeasurementsObserver(new Date());
    }

    public abstract void setMeasurementsObserver(Date date);
    public abstract boolean shouldDateBeDisplayedInList();

    //What should happen after Date is selected.
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        setMeasurementsObserver(c.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_ic_calender:
                new CalenderPickerFragment().show(getSupportFragmentManager(), "datePicker");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public static class HistoryTodayActivity extends HistoryActivity {

        @Override
        public void setMeasurementsObserver(final Date date) {
            historyViewModel.getTodayMeasurements(date).observe(this, new android.arch.lifecycle.Observer<List<Measurement>>() {
                @Override
                public void onChanged(@Nullable List<Measurement> measurements) {
                    adapter.addItems(measurements);
                    historyViewModel.drawChart(new ChartDrawDayStrategy(), chart, measurements, HRVParameterEnum.BAEVSKY, getApplicationContext());
                    dateView.setText(formatDate(getApplicationContext(), date, "dd.MM.yyyy"));
                }
            });
        }

        @Override
        public boolean shouldDateBeDisplayedInList() {
            return false;
        }
    }
    public static class HistoryWeekActivity extends HistoryActivity {

        @Override
        public void setMeasurementsObserver(final Date date) {
            historyViewModel.getWeekMeasurements(date).observe(this, new android.arch.lifecycle.Observer<List<Measurement>>() {
                @Override
                public void onChanged(@Nullable List<Measurement> measurements) {
                    adapter.addItems(measurements);
                    historyViewModel.drawChart(new ChartDrawWeekStrategy(), chart, measurements, HRVParameterEnum.BAEVSKY, getApplicationContext());
                    Date startOfWeek = DateUtil.getStartOfWeek(date);
                    Date endOfWeek = DateUtil.getEndOfWeek(date);
                    dateView.setText(DateUtil.formatDate(getApplicationContext(), startOfWeek, "dd.MM") + " - " + DateUtil.formatDate(getApplicationContext(), endOfWeek, "dd.MM.yyyy"));
                }
            });
        }
        @Override
        public boolean shouldDateBeDisplayedInList() {
            return true;
        }
    }
    public static class HistoryMonthActivity extends HistoryActivity {

        @Override
        public void setMeasurementsObserver(final Date date) {
            historyViewModel.getMonthMeasurements(date).observe(this, new android.arch.lifecycle.Observer<List<Measurement>>() {
                @Override
                public void onChanged(@Nullable List<Measurement> measurements) {
                    adapter.addItems(measurements);
                    historyViewModel.drawChart(new ChartDrawMonthStrategy(date), chart, measurements, HRVParameterEnum.BAEVSKY, getApplicationContext());
                    dateView.setText(DateUtil.formatDate(getApplicationContext(), date, "MM.yyyy"));
                }
            });
        }
        @Override
        public boolean shouldDateBeDisplayedInList() {
            return true;
        }
    }
}
