package hrv.band.app.ui.view.activity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
import hrv.band.app.model.HRVParameterSettings;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HistoryViewModel;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawDayStrategy;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawMonthStrategy;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawWeekStrategy;
import hrv.band.app.ui.view.adapter.HistoryViewAdapter;
import hrv.band.app.ui.view.fragment.CalenderPickerFragment;
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

    public static final String PARAMETER_VALUE = "parameter_value";
    public static final String DATE_VALUE = "date_value";
    public static final String SELECTION_VALUE = "selection_value";
    public static final int PARAMETER_VALUE_REQUEST = 1;
    protected HistoryViewModel historyViewModel;

    protected ColumnChartView chart;
    protected TextView dateView;
    protected TextView parameterTextView;
    protected HistoryViewAdapter adapter;

    protected HRVParameterEnum hrvParameter;
    protected Date date;




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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        hrvParameter = new ArrayList<>(HRVParameterSettings.DefaultSettings.visibleHRVParameters).get(sharedPreferences.getInt(PARAMETER_VALUE, 0));

        parameterTextView = findViewById(R.id.history_parameter);
        parameterTextView.setText(hrvParameter.toString());
        date = new Date();
        setMeasurementsObserver(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PARAMETER_VALUE_REQUEST && resultCode == RESULT_OK) {
            long longDate = data.getLongExtra(DATE_VALUE, 0);
            hrvParameter = new ArrayList<>(HRVParameterSettings.DefaultSettings.visibleHRVParameters).get(data.getIntExtra(SELECTION_VALUE, 0));
            if (longDate != 0) {
                date = new Date(longDate);
            } else {
                date = new Date();
            }
            setMeasurementsObserver(date);
        }
    }

    public abstract void setMeasurementsObserver(Date date);
    public abstract boolean shouldDateBeDisplayedInList();

    //What should happen after Date is selected.
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        date = c.getTime();
        setMeasurementsObserver(date);
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
            case R.id.menu_ic_value_settings:
                Intent intent = new Intent(this, ParameterSelectionActivity.class);
                intent.putExtra(DATE_VALUE, date.getTime());
                startActivityForResult(intent, PARAMETER_VALUE_REQUEST);
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
            historyViewModel.getMeasurements(DateUtil.getStartOfDay(date), DateUtil.getEndOfDay(date)).observe(this, new android.arch.lifecycle.Observer<List<Measurement>>() {

                @Override
                public void onChanged(@Nullable List<Measurement> measurements) {
                    adapter.addItems(measurements);
                    historyViewModel.drawChart(new ChartDrawDayStrategy(), chart, measurements, hrvParameter, getApplicationContext());
                    dateView.setText(formatDate(getApplicationContext(), date, "dd.MM.yyyy"));
                    parameterTextView.setText(hrvParameter.toString());
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
            historyViewModel.getMeasurements(DateUtil.getStartOfWeek(date), DateUtil.getEndOfWeek(date)).observe(this, new android.arch.lifecycle.Observer<List<Measurement>>() {
                @Override
                public void onChanged(@Nullable List<Measurement> measurements) {
                    adapter.addItems(measurements);
                    historyViewModel.drawChart(new ChartDrawWeekStrategy(), chart, measurements, hrvParameter, getApplicationContext());
                    Date startOfWeek = DateUtil.getStartOfWeek(date);
                    Date endOfWeek = DateUtil.getEndOfWeek(date);
                    dateView.setText(String.format("%s - %s", DateUtil.formatDate(getApplicationContext(), startOfWeek, "dd.MM"), DateUtil.formatDate(getApplicationContext(), endOfWeek, "dd.MM.yyyy")));
                    parameterTextView.setText(hrvParameter.toString());
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
            historyViewModel.getMeasurements(DateUtil.getStartOfMonth(date), DateUtil.getEndOfMonth(date)).observe(this, new android.arch.lifecycle.Observer<List<Measurement>>() {
                @Override
                public void onChanged(@Nullable List<Measurement> measurements) {
                    adapter.addItems(measurements);
                    historyViewModel.drawChart(new ChartDrawMonthStrategy(date), chart, measurements, hrvParameter, getApplicationContext());
                    dateView.setText(DateUtil.formatDate(getApplicationContext(), date, "MM.yyyy"));
                    parameterTextView.setText(hrvParameter.toString());
                }
            });
        }
        @Override
        public boolean shouldDateBeDisplayedInList() {
            return true;
        }
    }
}
