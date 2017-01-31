package hrv.band.app.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.view.adapter.SectionPagerAdapter;
import hrv.band.app.view.fragment.AboutFragment;
import hrv.band.app.view.fragment.DisclaimerFragment;
import hrv.band.app.view.fragment.FeedbackDialogFragment;
import hrv.band.app.view.fragment.LicenseFragment;
import hrv.band.app.view.fragment.PrivacyFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This Activity holds the fragments to show imprint stuff.
 */
public class ImprintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /* The Fragments this Activity holds. */
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(AboutFragment.newInstance());
        fragments.add(LicenseFragment.newInstance());
        fragments.add(DisclaimerFragment.newInstance());
        fragments.add(PrivacyFragment.newInstance());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionPagerAdapter mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), fragments, getPageTitles());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.imprint_viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.imprint_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Returns the page titles of the fragments.
     * @return the page titles of the fragments.
     */
    private String[] getPageTitles() {
        return new String[] {
                getResources().getString(R.string.tab_about),
                getResources().getString(R.string.tab_license),
                getResources().getString(R.string.tab_disclaimer),
                getResources().getString(R.string.tab_privacy)
        };
    }

    /**
     * Opens a Feedback dialog.
     * @param view the View calling this method.
     */
    public void sendFeedback(View view) {
        FeedbackDialogFragment picker = new FeedbackDialogFragment();
        picker.show(getSupportFragmentManager(), "Feedback");
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    /*public class SectionsPagerAdapter extends FragmentPagerAdapter {

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
            switch(position) {
                case 0: return getResources().getString(R.string.tab_about);
                case 1: return getResources().getString(R.string.tab_license);
                case 2: return getResources().getString(R.string.tab_disclaimer);
                case 3: return getResources().getString(R.string.tab_privacy);
                default: return "";
            }
        }
    }*/
}
