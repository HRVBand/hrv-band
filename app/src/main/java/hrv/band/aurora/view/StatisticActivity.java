package hrv.band.aurora.view;

import android.app.DatePickerDialog;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.DatePicker;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.storage.IStorage;
import hrv.band.aurora.storage.SQLite.SQLController;
import hrv.band.aurora.view.adapter.HRVValue;
import hrv.band.aurora.view.fragment.OverviewFragment;
import hrv.band.aurora.view.fragment.StatisticFragment;
import hrv.band.aurora.view.fragment.CalenderPickerFragment;

public class StatisticActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private View view;
    private HRVValue type;
    private IStorage storage;
    private ArrayList<HRVParameters> parameters;
    private List<StatisticFragment> fragments;
    private final int fragmentCount = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storage = new SQLController();
        initFragments();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(mViewPager);

        type = (HRVValue)
                getIntent().getSerializableExtra(OverviewFragment.valueType);

        mViewPager.setCurrentItem(getTitlePosition(type));
    }

    private void initFragments() {
        fragments = new ArrayList<>();
       // Date date = new Date();
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        ArrayList<HRVParameters> params = getParameters(getDate(year, month, day, 1));
        for(int i = 0; i < HRVValue.values().length; i++) {
            fragments.add(StatisticFragment.newInstance(HRVValue.values()[i], params,
                    getDate(year, month, day, 0)));
        }
    }

    private Date getDate(int year, int month, int day, int addDays) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        c.add(Calendar.DATE, addDays);
        return c.getTime();
    }

     //What should happen after Date is selected.
     @Override
     public void onDateSet(DatePicker view, int year, int month, int day) {
        parameters = getParameters(getDate(year, month, day, 1));

        for(StatisticFragment fragment : fragments) {
            fragment.updateValues(parameters, getDate(year, month, day, 0));
        }
     }
    private ArrayList<HRVParameters> getParameters(Date date) {
        ArrayList<HRVParameters> result = new ArrayList<>();

        List<HRVParameters> params = storage.loadData(this, date);
        result.addAll(params);
        return result;
    }

    public void openCalender(View view) {
        this.view = view;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            /*Date date = new Date();
            return StatisticFragment.newInstance(HRVValue.values()[position],
                    getParameters(date), date);*/
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
