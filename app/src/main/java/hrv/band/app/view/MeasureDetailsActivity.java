package hrv.band.app.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.SQLite.SQLController;
import hrv.band.app.view.fragment.MeasureDetailsEditFragment;
import hrv.band.app.view.fragment.MeasureRRFragment;
import hrv.band.app.view.fragment.MeasureValueFragment;

public class MeasureDetailsActivity extends AppCompatActivity {

    private HRVParameters parameter;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        parameter = getIntent().getParcelableExtra(MainActivity.HRV_PARAMETER_ID);

        fragments = new ArrayList<>();
        fragments.add(MeasureValueFragment.newInstance(parameter));
        fragments.add(MeasureRRFragment.newInstance(parameter));
        fragments.add(MeasureDetailsEditFragment.newInstance(parameter));

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.measure_details_viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.measure_details_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public void saveMeasurement(View view) {
        IStorage storage = new SQLController();
        setMeasurementDetails();
        storage.saveData(getApplicationContext(), parameter);
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setMeasurementDetails() {
        MeasureDetailsEditFragment fragment = null;
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof MeasureDetailsEditFragment) {
                fragment = (MeasureDetailsEditFragment) fragments.get(i);
                break;
            }
        }
        parameter.setRating(fragment != null ? fragment.getRating() : 0);
        parameter.setCategory(fragment != null ? fragment.getCategory() : null);
        parameter.setNote(fragment.getNote());
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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "HRV Values";
                case 1: return "RR Intervals";
                case 2: return "Details";
            }
            return "";
        }
    }
}
