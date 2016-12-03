package hrv.band.app.view;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import hrv.band.app.R;
import hrv.band.app.view.fragment.DisclaimerDialogFragment;
import hrv.band.app.view.fragment.ExportFragment;
import hrv.band.app.view.fragment.FeedbackDialogFragment;
import hrv.band.app.view.fragment.ImportFragment;
import hrv.band.app.view.fragment.MeasuringFragment;
import hrv.band.app.view.fragment.OverviewFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String HRV_VALUE_ID = "HRV_VALUE";
    public static final String HRV_PARAMETER_ID = "HRV_PARAMETER";
    public static final String HRV_DATE = "HRV_DATE";
    public static final String HRV_VALUE = "hrv_rr_value";
    public static final String WEBSITE_URL = "https://thomcz.github.io/hrv-band";
    private static final String WEBSITE_PRIVACY_URL = WEBSITE_URL + "/privacy";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    //private IRRInterval rrInterval;
    private MeasuringFragment measureFragment;
    private OverviewFragment overviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        handleDisclaimer();

        setContentView(R.layout.activity_main);
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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        assert mViewPager != null;

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        assert tabLayout != null;

        tabLayout.setupWithViewPager(mViewPager);
    }

   /* @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            measureFragment.stopMeasuring();
        }
    }*/

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.menu_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_website) {
            openWebsite(WEBSITE_URL);
        } else if (id == R.id.menu_share) {
            openShareIntent();
        } else if (id == R.id.menu_privacy) {
            openWebsite(WEBSITE_PRIVACY_URL);
        } else if (id == R.id.menu_feedback) {
            FeedbackDialogFragment picker = new FeedbackDialogFragment();
            picker.show(getFragmentManager(), "Feedback");
        } else if (id == R.id.menu_imprint) {
            Intent intent = new Intent(this, ImprintActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_rate) {

            //Laut Stack overflow
            //http://stackoverflow.com/questions/10816757/rate-this-app-link-in-google-play-store-app-on-the-phone
            //Kann erst getestet werden, wenn die App im Store ist!!
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
        } else if(id == R.id.menu_export_db) {
            DialogFragment exportFragment = ExportFragment.newInstance();
            exportFragment.show(getFragmentManager(), getResources().getString(R.string.common_export   ));
        } else if(id == R.id.menu_import_db) {
            DialogFragment importFragment = ImportFragment.newInstance();
            importFragment.show(getFragmentManager(), getResources().getString(R.string.common_import));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openWebsite(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

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
                    return getResources().getString(R.string.common_bold_measure);
                case 1:
                    return getResources().getString(R.string.common_bold_overview   );
            }
            return null;
        }
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

    private void handleDisclaimer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Clears SharedPreferences
        //SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        //sharedEditor.clear();
        //sharedEditor.commit();


        if(!sharedPreferences.getBoolean(DisclaimerDialogFragment.DISCLAIMER_AGREEMENT, false)) {
            DisclaimerDialogFragment disclaimerDialogFragment = new DisclaimerDialogFragment();
            disclaimerDialogFragment.show(getFragmentManager(), "dialog");
        }
    }
}
