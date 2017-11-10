package hrv.band.app.ui.view.activity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
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
import hrv.band.app.ui.presenter.HistoryViewModel;
import hrv.band.app.ui.presenter.IHistoryPresenter;
import hrv.band.app.ui.view.adapter.SectionPagerAdapter;
import hrv.band.app.ui.view.fragment.CalenderPickerFragment;
import hrv.band.app.ui.view.fragment.HistoryFragment;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This Activity holds the fragments which each shows a HRV value.
 */
public class HistoryActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    public static final String FRAGMENT_ID = "fragment_id";

    private SectionPagerAdapter sectionsPagerAdapter;

    private Date date;

    private HistoryViewModel historyViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setupToolbar();

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        date = new Date();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HistoryFragment.HistoryTodayFragment.newInstance());
        fragments.add(HistoryFragment.HistoryWeekFragment.newInstance());
        fragments.add(HistoryFragment.HistoryMonthFragment.newInstance());
        //set up viewpager
        sectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), fragments, getPageTitles());
        ViewPager mViewPager = findViewById(R.id.statistic_viewpager);
        mViewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.statistic_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /*HRVParameterEnum type = (HRVParameterEnum)
               // getIntent().getSerializableExtra(OverviewFragment.VALUE_TYPE);

        if (type != null) {
            mViewPager.setCurrentItem(presenter.getTitlePosition(type));
        }*/
    }

    private String[] getPageTitles() {
        return new String[]{
                getResources().getString(R.string.common_today),
                getResources().getString(R.string.common_week),
                getResources().getString(R.string.common_month)
        };
    }

    //What should happen after Date is selected.
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        date = c.getTime();
        historyViewModel.setMeasurements(c.getTime());
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
}
