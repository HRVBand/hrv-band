package hrv.band.aurora.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Date;

import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.RRInterval.Interval;
import hrv.band.aurora.storage.IStorage;
import hrv.band.aurora.storage.SQLite.SQLController;
import hrv.band.aurora.storage.SQLite.SQLiteStorageController;
import hrv.band.aurora.storage.SampleDataCreation.ISampleDataFactory;
import hrv.band.aurora.storage.SampleDataCreation.PointThreeAndPoint5RandomSampleDataFactory;
import hrv.band.aurora.storage.SampleDataCreation.SimpleRandomSampleDataFactory;
import hrv.band.aurora.storage.SampleDataCreation.StaticSampleDataFactory;
import hrv.band.aurora.view.fragment.MeasuringFragment;
import hrv.band.aurora.view.fragment.OverviewFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    //private IRRInterval rrInterval;
    private MeasuringFragment measureFragment;
    private OverviewFragment overviewFragment;

    public static final String HRV_PARAMETER_ID = "HRV_PARAMETER";

    private Interval ival;
    private TextView rrStatus;
    private TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //NavigationDrawer

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Fragment
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Context context = getApplicationContext();
            context.deleteDatabase(SQLiteStorageController.DATABASE_NAME);

            ISampleDataFactory factory = new SimpleRandomSampleDataFactory();
            List<HRVParameters> parameters = factory.create(5);

            IStorage storage2 = new SQLController();
            storage2.saveData(getApplicationContext(), parameters);

            List<HRVParameters> params = storage2.loadData(context, parameters.get(1).getTime());
            double a = params.get(0).getBaevsky();

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return measureFragment = new MeasuringFragment();
            }
            return overviewFragment = new OverviewFragment();
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MEASURING";
                case 1:
                    return "OVERVIEW";
            }
            return null;
        }
    }

    public void startMeasuring(View view) {
        measureFragment.startAnimation(new Interval(new Date()));
    }

    public void getDevicePermission(View view) {
        measureFragment.getRRInterval().getDevicePermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //reset to default
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (measureFragment != null) {
            measureFragment.getRRInterval().pauseMeasuring();
        }
    }

    @Override
    protected void onDestroy() {
        if (measureFragment != null) {
            measureFragment.getRRInterval().destroy();
        }
        super.onDestroy();
    }
}
