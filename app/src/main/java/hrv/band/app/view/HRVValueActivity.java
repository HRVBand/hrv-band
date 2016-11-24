package hrv.band.app.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.SQLite.SQLController;
import hrv.band.app.view.fragment.MeasureDetailsFragment;
import hrv.band.app.view.fragment.MeasureValueFragment;
import hrv.band.app.view.fragment.MeasureRRFragment;

import static hrv.band.app.view.StatisticActivity.RESULT_DELETED;

public class HRVValueActivity extends AppCompatActivity {


    private HRVParameters parameter;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrv_value);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parameter = getIntent().getParcelableExtra(MainActivity.HRV_PARAMETER_ID);

        fragments = new ArrayList<>();
        fragments.add(MeasureValueFragment.newInstance(parameter));
        fragments.add(MeasureRRFragment.newInstance(parameter));
        fragments.add(MeasureDetailsFragment.newInstance(parameter));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.hrv_viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.hrv_tabs);
        tabLayout.setupWithViewPager(mViewPager);
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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void deleteParameter(View view) {
        if (parameter == null) {
            return;
        }
        IStorage storage = new SQLController();
        storage.deleteData(getApplicationContext(), parameter);
        setResult(RESULT_DELETED);
        this.finish();

        //Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
          //      .setAction("Action", null).show();
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
