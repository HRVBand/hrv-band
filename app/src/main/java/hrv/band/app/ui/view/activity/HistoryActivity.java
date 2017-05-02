package hrv.band.app.ui.view.activity;

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

import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HistoryPresenter;
import hrv.band.app.ui.presenter.IHistoryPresenter;
import hrv.band.app.ui.view.activity.history.chartstrategy.AbstractChartDrawStrategy;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawDayStrategy;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawMonthStrategy;
import hrv.band.app.ui.view.activity.history.chartstrategy.ChartDrawWeekStrategy;
import hrv.band.app.ui.view.activity.history.measurementstrategy.AbstractParameterLoadStrategy;
import hrv.band.app.ui.view.activity.history.measurementstrategy.ParameterLoadDayStrategy;
import hrv.band.app.ui.view.activity.history.measurementstrategy.ParameterLoadMonthStrategy;
import hrv.band.app.ui.view.activity.history.measurementstrategy.ParameterLoadWeekStrategy;
import hrv.band.app.ui.view.adapter.HRVValue;
import hrv.band.app.ui.view.adapter.SectionPagerAdapter;
import hrv.band.app.ui.view.fragment.CalenderPickerFragment;
import hrv.band.app.ui.view.fragment.HistoryFragment;
import hrv.band.app.ui.view.fragment.OverviewFragment;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This Activity holds the fragments which each shows a HRV value.
 */
public class HistoryActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, IHistoryView {

    /**
     * Code which indicates that a HRV value was deleted.
     **/
    public static final int RESULT_DELETED = 42;
    /**
     * The Fragments this Activity holds.
     **/
    private List<Fragment> fragments;
    private SectionPagerAdapter sectionsPagerAdapter;
    /**
     * Parameters to show in this ui.view.activity.
     **/
    private List<Measurement> measurements;
    /**
     * Date that user wants to show measurements.
     **/
    private Date date;

    private IHistoryPresenter presenter;

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

        presenter = new HistoryPresenter();

        setDrawStrategy(new ChartDrawDayStrategy(), new ParameterLoadDayStrategy(getApplicationContext()));

        initFragments();

        //set up viewpager
        sectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), fragments, presenter.getPageTitles());
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
     * Creates a ui.view.fragment for each HRV value.
     */
    private void initFragments() {
        fragments = new ArrayList<>();
        date = new Date();

        measurements = presenter.getMeasurements(date);
        for (int i = 0; i < HRVValue.values().length; i++) {
            fragments.add(HistoryFragment.newInstance(HRVValue.values()[i]));
        }
    }

    /**
     * Returns position of the given HRV value.
     *
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
                setDrawStrategy(new ChartDrawDayStrategy(), new ParameterLoadDayStrategy(getApplicationContext()));
                return true;
            case R.id.menu_week:
                setDrawStrategy(new ChartDrawWeekStrategy(), new ParameterLoadWeekStrategy(getApplicationContext()));
                return true;
            case R.id.menu_month:
                setDrawStrategy(new ChartDrawMonthStrategy(date), new ParameterLoadMonthStrategy(getApplicationContext()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sets the chart drawing and parameter selecting strategy.
     *
     * @param chartStrategy     the chart drawing strategy.
     * @param parameterStrategy the parameter selecting strategy.
     */
    private void setDrawStrategy(AbstractChartDrawStrategy chartStrategy,
                                 AbstractParameterLoadStrategy parameterStrategy) {
        presenter.setDrawChartStrategy(chartStrategy);
        presenter.setParameterLoadStrategy(parameterStrategy);
        updateFragments();
    }

    @Override
    public void drawChart(ColumnChartView chart, HRVValue hrvValue, Context context) {
        presenter.getChartDrawStrategy().drawChart(measurements, chart, hrvValue, context);
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
        measurements = presenter.getMeasurements(date);
        sectionsPagerAdapter.updateFragments();
    }
}
