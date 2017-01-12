package hrv.band.app.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.SQLite.SQLController;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.fragment.CalenderPickerFragment;
import hrv.band.app.view.fragment.OverviewFragment;
import hrv.band.app.view.fragment.StatisticFragment;

public class StatisticActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private IStorage storage;
    private List<StatisticFragment> fragments;

    public static final int RESULT_DELETED = 42;


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

        storage = new SQLController();
        initFragments();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.statistic_viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.statistic_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        HRVValue type = (HRVValue)
                getIntent().getSerializableExtra(OverviewFragment.valueType);

        if (!(type == null)) {
            mViewPager.setCurrentItem(getTitlePosition(type));
        }
    }

    private void initFragments() {
        fragments = new ArrayList<>();
        Date date = new Date();

        ArrayList<HRVParameters> params = getParameters(date);
        for(int i = 0; i < HRVValue.values().length; i++) {
            fragments.add(StatisticFragment.newInstance(HRVValue.values()[i], params,
                    date));
        }
    }


     //What should happen after Date is selected.
     @Override
     public void onDateSet(DatePicker view, int year, int month, int day) {
         Calendar c = Calendar.getInstance();
         c.set(year, month, day, 0, 0, 0);
         updateFragments(c.getTime());
     }

    public void updateFragments(Date date) {
        ArrayList<HRVParameters> parameters = getParameters(date);
        for(StatisticFragment fragment : fragments) {
            fragment.updateValues(parameters, date);
        }
    }

    private ArrayList<HRVParameters> getParameters(Date date) {
        ArrayList<HRVParameters> result = new ArrayList<>();

        List<HRVParameters> params = storage.loadData(this, date);
        result.addAll(params);
        return result;
    }

    public void openCalender(View view) {
        CalenderPickerFragment picker = new CalenderPickerFragment();
        picker.show(getSupportFragmentManager(), "datePicker");
    }

    private int getTitlePosition(HRVValue value) {
        return value.ordinal();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistic, menu);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return HRVValue.values()[position].toString();
        }
    }
}
