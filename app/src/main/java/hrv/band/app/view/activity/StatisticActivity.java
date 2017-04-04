package hrv.band.app.view.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hrv.RRData;
import hrv.band.app.R;
import hrv.band.app.control.Measurement;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.adapter.SectionPagerAdapter;
import hrv.band.app.view.control.chart.AbstractChartDrawStrategy;
import hrv.band.app.view.control.parameter.AbstractParameterLoadStrategy;
import hrv.band.app.view.control.chart.ChartDrawDayStrategy;
import hrv.band.app.view.control.chart.ChartDrawMonthStrategy;
import hrv.band.app.view.control.chart.ChartDrawWeekStrategy;
import hrv.band.app.view.control.StatisticListener;
import hrv.band.app.view.control.parameter.ParameterLoadDayStrategy;
import hrv.band.app.view.control.parameter.ParameterLoadMonthStrategy;
import hrv.band.app.view.control.parameter.ParameterLoadWeekStrategy;
import hrv.band.app.view.fragment.CalenderPickerFragment;
import hrv.band.app.view.fragment.OverviewFragment;
import hrv.band.app.view.fragment.StatisticFragment;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This Activity holds the fragments which each shows a HRV value.
 */
public class StatisticActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, StatisticListener {

    /** Code which indicates that a HRV value was deleted. **/
    public static final int RESULT_DELETED = 42;
    /** The Fragments this Activity holds. **/
    private List<Fragment> fragments;
    private SectionPagerAdapter sectionsPagerAdapter;
    /** Strategy how to draw chart. **/
    private AbstractChartDrawStrategy chartStrategy;
    /** Strategy how to select parameter. **/
    private AbstractParameterLoadStrategy parameterStrategy;
    /** Parameters to show in this activity. **/
    private List<Measurement> measurements;
    /** Date that user wants to show measurements. **/
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setDrawStrategy(new ChartDrawDayStrategy(), new ParameterLoadDayStrategy());

        initFragments();

        //set up viewpager
        sectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), fragments, getPageTitles());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.statistic_viewpager);
        mViewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.statistic_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        HRVValue type = (HRVValue)
                getIntent().getSerializableExtra(OverviewFragment.VALUE_TYPE);

        if (type != null) {
            mViewPager.setCurrentItem(getTitlePosition(type));
        }
    }

    /**
     * Creates a fragment for each HRV value.
     */
    private void initFragments() {
        fragments = new ArrayList<>();
        date = new Date();

        measurements = getMeasurements(date);
        for(int i = 0; i < HRVValue.values().length; i++) {
            fragments.add(StatisticFragment.newInstance(HRVValue.values()[i]));
        }
    }

    /**
     * Returns measurements of given date.
     * @param date selected date to get all HRV values.
     * @return measurements of given date
     */
    private List<Measurement> getMeasurements(Date date) {
        List<Measurement> result = new ArrayList<>();

        List<Measurement> params = parameterStrategy.loadParameter(this, date);

        for (Measurement parameter : params) {
            RRData.createFromRRInterval(parameter.getRRIntervals(), units.TimeUnit.SECOND);
            Measurement.MeasurementBuilder measurementBuilder = Measurement.from(parameter.getTime(), parameter.getRRIntervals())
                    .category(parameter.getCategory())
                    .rating(parameter.getRating())
                    .note(parameter.getNote());
            result.add(measurementBuilder.build());
        }

        return result;
    }

    /**
     * Returns the page titles of the fragments.
     * @return the page titles of the fragments.
     */
    private String[] getPageTitles() {
        HRVValue[] values = HRVValue.values();
        String[] titles = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            titles[i] = values[i].toString();
        }
        return titles;
    }

    /**
     * Returns position of the given HRV value.
     * @param value the HRV value to get the position.
     * @return position of the given HRV value.
     */
    private int getTitlePosition(HRVValue value) {
        return value.ordinal();
    }

     //What should happen after Date is selected.
     @Override
     public void onDateSet(DatePicker view, int year, int month, int day) {
         Calendar c = Calendar.getInstance();
         c.set(year, month, day, 0, 0, 0);
         date = c.getTime();
         updateFragments();
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_ic_calender:
                new CalenderPickerFragment().show(getSupportFragmentManager(), "datePicker");
                return true;
            case R.id.menu_day:
                setDrawStrategy(new ChartDrawDayStrategy(), new ParameterLoadDayStrategy());
                return true;
            case R.id.menu_week:
                setDrawStrategy(new ChartDrawWeekStrategy(date), new ParameterLoadWeekStrategy());
                return true;
            case R.id.menu_month:
                setDrawStrategy(new ChartDrawMonthStrategy(date), new ParameterLoadMonthStrategy());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sets the chart drawing and parameter selecting strategy.
     * @param chartStrategy the chart drawing strategy.
     * @param parameterStrategy the parameter selecting strategy.
     */
    private void setDrawStrategy(AbstractChartDrawStrategy chartStrategy,
                                 AbstractParameterLoadStrategy parameterStrategy) {
        this.parameterStrategy = parameterStrategy;
        this.chartStrategy = chartStrategy;
        updateFragments();
    }

    @Override
    public void drawChart(ColumnChartView chart, HRVValue hrvValue, Context context) {
        chartStrategy.drawChart(measurements, chart, hrvValue, context);
    }

    @Override
    public List<Measurement> getMeasurements() {
        return measurements;
    }

    @Override
    public void updateFragments() {
        if (date == null) {
            return;
        }
        measurements = getMeasurements(date);
        sectionsPagerAdapter.updateFragments();
    }
}
