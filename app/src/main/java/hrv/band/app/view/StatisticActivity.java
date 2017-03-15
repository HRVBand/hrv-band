package hrv.band.app.view;

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
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.sqlite.HRVSQLController;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.adapter.SectionPagerAdapter;
import hrv.band.app.view.chart.ChartDrawDayStrategy;
import hrv.band.app.view.chart.ChartDrawWeekStrategy;
import hrv.band.app.view.chart.StatisticListener;
import hrv.band.app.view.chart.ChartStrategyContext;
import hrv.band.app.view.fragment.CalenderPickerFragment;
import hrv.band.app.view.fragment.OverviewFragment;
import hrv.band.app.view.fragment.StatisticFragment;
import lecho.lib.hellocharts.view.ColumnChartView;
import units.TimeUnitConverter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This Activity holds the fragments which each shows a HRV value.
 */
public class StatisticActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, StatisticListener {

    /** Storage object to manage the sql database. **/
    private IStorage storage;
    /** The Fragments this Activity holds. **/
    private List<Fragment> fragments;
    /** Code which indicates that a HRV value was deleted. **/
    public static final int RESULT_DELETED = 42;

    private SectionPagerAdapter sectionsPagerAdapter;

    private ChartStrategyContext chartContext;

    private List<Measurement> parameters;

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

        storage = new HRVSQLController();
        initFragments();

        chartContext = new ChartStrategyContext();
        chartContext.setStrategy(new ChartDrawWeekStrategy());

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

        parameters = getParameters(date);
        for(int i = 0; i < HRVValue.values().length; i++) {
            fragments.add(StatisticFragment.newInstance(HRVValue.values()[i]));
        }
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


     //What should happen after Date is selected.
     @Override
     public void onDateSet(DatePicker view, int year, int month, int day) {
         Calendar c = Calendar.getInstance();
         c.set(year, month, day, 0, 0, 0);
         date = c.getTime();
         updateFragments(date);
     }

    @Override
    public void updateFragments(Date date) {
        parameters = getParameters(date);
        sectionsPagerAdapter.updateFragments();
    }

    /**
     * Returns parameters of given date.
     * @param date selected date to get all HRV values.
     * @return parameters of given date
     */
    private List<Measurement> getParameters(Date date) {
        List<Measurement> result = new ArrayList<>();

        List<Measurement> params = storage.loadData(this, date);

        for (Measurement parameter : params) {
            RRData.createFromRRInterval(parameter.getRRIntervals(), TimeUnitConverter.TimeUnit.SECOND);
            Measurement.MeasurementBuilder measurementBuilder = Measurement.from(parameter.getTime(), parameter.getRRIntervals())
                    .category(parameter.getCategory())
                    .rating(parameter.getRating())
                    .note(parameter.getNote());
            result.add(measurementBuilder.build());
        }

        return result;
    }

    /**
     * Returns position of the given HRV value.
     * @param value the HRV value to get the position.
     * @return position of the given HRV value.
     */
    private int getTitlePosition(HRVValue value) {
        return value.ordinal();
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
                /** Opens a calender picker. **/
                new CalenderPickerFragment().show(getSupportFragmentManager(), "datePicker");
                return true;
            case R.id.menu_day:
                return true;
            case R.id.menu_week:
                return true;
            case R.id.menu_month:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void drawChart(List<Measurement> parameters, ColumnChartView chart, HRVValue hrvValue,
                          Context context) {
        chartContext.drawChart(parameters, chart, hrvValue, context);
    }

    @Override
    public List<Measurement> getParameters() {
        return parameters;
    }

    @Override
    public Date getDate() {
        return date;
    }
}
