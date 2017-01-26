package hrv.band.app.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.view.adapter.SectionPagerAdapter;
import hrv.band.app.view.fragment.DisclaimerDialogFragment;
import hrv.band.app.view.fragment.FeedbackDialogFragment;
import hrv.band.app.view.fragment.MeasuringFragment;
import hrv.band.app.view.fragment.OverviewFragment;
import hrv.band.app.view.fragment.SampleDataFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This Activity is the main Activity and holds the measurement and overview Fragment.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String HRV_VALUE_ID = "HRV_VALUE";
    public static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    public static final String HRV_DATE = "HRV_DATE";
    public static final String HRV_VALUE = "hrv_rr_value";
    public static final String WEBSITE_URL = "https://thomcz.github.io/hrv-band";
    private static final String WEBSITE_PRIVACY_URL = WEBSITE_URL + "/privacy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleDisclaimer();

        //Needed to set default values
        PreferenceManager.setDefaultValues(this, R.xml.settings_fragment, false);

        setContentView(R.layout.navbar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //NavigationDrawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;

        navigationView.setNavigationItemSelectedListener(this);

        //Fragment
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(MeasuringFragment.newInstance());
        fragments.add(OverviewFragment.newInstance());

        SectionPagerAdapter mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), fragments, getPageTitles());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        assert mViewPager != null;

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        assert tabLayout != null;

        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Returns the page titles of the fragments.
     * @return the page titles of the fragments.
     */
    private String[] getPageTitles() {
        return new String[] {
                getResources().getString(R.string.common_bold_measure),
                getResources().getString(R.string.common_bold_overview)
        };
    }

    @Override
    public void onBackPressed() {

        //Close the app, if disclaimer has not yet been accepted
        int backstackCount = getFragmentManager().getBackStackEntryCount();
        if(backstackCount != 0) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if(!sharedPreferences.getBoolean(DisclaimerDialogFragment.DISCLAIMER_AGREEMENT, false)) {
                System.exit(0);
            }
        }

        //If the Navigation Drawer is opened, a backPressed closes the Navigation Drawer.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.menu_help) {
            startActivity(new Intent(this, IntroActivity.class));
        } else if (id == R.id.menu_website) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE_URL)));
        } else if (id == R.id.menu_share) {
            openShareIntent();
        } else if (id == R.id.menu_privacy) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE_PRIVACY_URL)));
        } else if (id == R.id.menu_feedback) {
            new FeedbackDialogFragment().show(getFragmentManager(), "Feedback");
        } else if (id == R.id.menu_imprint) {
            startActivity(new Intent(this, ImprintActivity.class));
        } else if (id == R.id.menu_rate) {
            rateApp();
        } else if (id == R.id.menu_sample_data) {
            SampleDataFragment.newInstance(false).show(getFragmentManager(), getResources().getString(R.string.common_import));
        } else if (id == R.id.menu_Settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Opens play store hence the user can rate the app.
     * http://stackoverflow.com/questions/10816757/rate-this-app-link-in-google-play-store-app-on-the-phone
     */
    private void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    /**
     * Opens dialog which lists all apps the user can share a message with.
     */
    private void openShareIntent() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        Resources resources = getResources();
        String shareBody = resources.getString(R.string.share_body);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, resources.getString(R.string.share_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, resources.getString(R.string.share_via)));
    }

    /**
     * Stops the measurement.
     */
    public void stopMeasuring() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof MeasuringFragment) {
                ((MeasuringFragment) fragment).stopMeasuring();
            }
        }
    }

    /**
     * Opens the disclaimer dialog if the app opens the first time. If the user accept the
     * disclaimer it will not open again.
     */
    private void handleDisclaimer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(!sharedPreferences.getBoolean(DisclaimerDialogFragment.DISCLAIMER_AGREEMENT, false)) {
            DisclaimerDialogFragment disclaimerDialogFragment = new DisclaimerDialogFragment();
            disclaimerDialogFragment.show(getFragmentManager(), "dialog");
        }
        //if in older version disclaimer is accepted intro opens on first run after update
        //TODO remove this code starting with version 1.2
        else if (!sharedPreferences.getBoolean(IntroActivity.APP_INTRO, false)) {
            startActivity(new Intent(this, IntroActivity.class));
        }
    }
}
