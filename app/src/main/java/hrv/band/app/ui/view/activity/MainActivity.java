package hrv.band.app.ui.view.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.device.ConnectionManager;
import hrv.band.app.ui.presenter.IMainPresenter;
import hrv.band.app.ui.presenter.MainPresenter;
import hrv.band.app.ui.view.adapter.SectionPagerAdapter;
import hrv.band.app.ui.view.fragment.DisclaimerDialogFragment;
import hrv.band.app.ui.view.fragment.FeedbackDialogFragment;
import hrv.band.app.ui.view.fragment.OverviewFragment;
import hrv.band.app.ui.view.fragment.IMeasuringView;
import hrv.band.app.ui.view.fragment.MeasuringFragment;
import hrv.band.app.ui.view.fragment.SurveyDialogFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This Activity is the main Activity and holds the measurement and overview Fragment.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainView {

    public static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    public static final String HRV_PARAMETER_ID_ID = "HRV_PARAMETER_ID";
    public static final String HRV_VALUE = "hrv_rr_value";

    private IMainPresenter presenter;
    private IMeasuringView measuringView;
    private ConnectionManager connectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainPresenter(this);
        connectionManager = new ConnectionManager(this);
        handleDisclaimer();

        handleSurvey();

        //Needed to set default values
        PreferenceManager.setDefaultValues(this, R.xml.settings_fragment, false);

        setContentView(R.layout.navbar_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //NavigationDrawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        assert navigationView != null;

        navigationView.setNavigationItemSelectedListener(this);

        //Fragment
        List<Fragment> fragments = new ArrayList<>();
        measuringView = MeasuringFragment.newInstance();
        fragments.add((Fragment) measuringView);
        fragments.add(OverviewFragment.newInstance());

        SectionPagerAdapter mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), fragments, getPageTitles());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.main_viewpager);
        assert mViewPager != null;

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.main_tabs);
        assert tabLayout != null;

        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Returns the page titles of the fragments.
     *
     * @return the page titles of the fragments.
     */
    private String[] getPageTitles() {
        return new String[]{
                getResources().getString(R.string.common_bold_measure),
                getResources().getString(R.string.common_bold_overview)
        };
    }

    @Override
    public void onBackPressed() {

        //Close the app, if disclaimer has not yet been accepted
        int backStackCount = getFragmentManager().getBackStackEntryCount();
        if (backStackCount != 0 && !presenter.agreedToDisclaimer()) {
            finish();
        }

        //If the Navigation Drawer is opened, a backPressed closes the Navigation Drawer.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.connect_ant_menu:
                measuringView.setHrvRrIntervalDevice(connectionManager.connectToAnt());
                measuringView.addDeviceListeners();
                break;
            case R.id.connect_band_menu:
                measuringView.setHrvRrIntervalDevice(connectionManager.connectToMSBand());
                measuringView.addDeviceListeners();
                break;
            case R.id.disconnect_devices_menu:
                measuringView.setHrvRrIntervalDevice(connectionManager.disconnectDevices(measuringView.getHrvRrIntervalDevice()));
                measuringView.makeToast(R.string.msg_disconnecting);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        presenter.handleNavBar(item.getItemId());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    /**
     * Opens play store hence the user can rate the app.
     * http://stackoverflow.com/questions/10816757/rate-this-app-link-in-google-play-store-app-on-the-phone
     */
    @Override
    public void rateApp() {
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
            Log.e(e.getClass().getName(), "ActivityNotFoundException", e);
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

    /**
     * Opens dialog which lists all apps the user can share a message with.
     */
    @Override
    public void openShareIntent() {
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
    @Override
    public void stopMeasuring() {
        if (measuringView != null) {
            measuringView.stopMeasuring();
        }
    }

    /**
     * Opens the disclaimer dialog if the app opens the first time. If the user accept the
     * disclaimer it will not open again.
     */
    private void handleDisclaimer() {
        if (!presenter.agreedToDisclaimer()) {
            DisclaimerDialogFragment disclaimerDialogFragment = new DisclaimerDialogFragment();
            disclaimerDialogFragment.show(getFragmentManager(), "dialog");
        }
    }

    private void handleSurvey() {
        if (!presenter.askedForSurvey()) {
            startSurveyDialog();
        }
    }

    @Override
    public void startActivity(Class<? extends Activity> activity) {
        startActivity(new Intent(this, activity));
    }

    @Override
    public void startActivity(Class<? extends Activity> activity, String extraId, String extra) {
        Intent intent = new Intent(this, activity);
        intent.putExtra(extraId, extra);
        startActivity(intent);
    }

    @Override
    public void startFeedbackDialog() {
        FeedbackDialogFragment.newInstance().show(getSupportFragmentManager(), "Feedback");
    }

    @Override
    public void startSurveyDialog() {
        SurveyDialogFragment.newInstance().show(getSupportFragmentManager(), "Feedback");
    }

    @Override
    public Activity getMainActivity() {
        return this;
    }

}
