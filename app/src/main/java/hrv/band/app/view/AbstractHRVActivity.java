package hrv.band.app.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import hrv.band.app.control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.view.adapter.SectionPagerAdapter;
import hrv.band.app.view.fragment.MeasuredRRFragment;
import hrv.band.app.view.fragment.MeasuredValueFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This abstract Activity holds the fragments to show a specific HRV value.
 */
public abstract class AbstractHRVActivity extends AppCompatActivity {
    /** The HRV parameter to show. **/
    private HRVParameters parameter;
    /** The Fragments this Activity holds. **/
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        parameter = getIntent().getParcelableExtra(MainActivity.HRV_PARAMETER_ID);
        fragments = new ArrayList<>();

        createFragments();

        setViewPager();
    }

    /**
     * Sets the ViewPager of this Activity.
     */
    private void setViewPager() {
        ViewPager mViewPager = (ViewPager) findViewById(getViewPagerID());
        SectionPagerAdapter sectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), fragments, getPageTitles());
        mViewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(getTabID());
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Returns the page titles of the fragments.
     * @return the page titles of the fragments.
     */
    private String[] getPageTitles() {
        return new String[] {
                "HRV Values",
                "RR Intervals",
                "Details"
        };
    }

    /**
     * Sets the contentView of this Activity.
     */
    private void setContentView() {
        setContentView(getContentViewId());
    }

    /**
     * Adds Fragments to this Activity.
     */
    private void createFragments() {
        fragments.add(MeasuredValueFragment.newInstance(parameter));
        fragments.add(MeasuredRRFragment.newInstance(parameter));
        addDetailsFragment(fragments);
    }

    /**
     * Returns the HRV parameter to show.
     * @return the HRV parameter to show.
     */
    HRVParameters getParameter() {
        return parameter;
    }

    /**
     * Returns the id of the layout of this activity.
     * @return the id of the layout of this activity.
     */
    protected abstract int getContentViewId();

    /**
     * Returns the id of the viewPager of this activity.
     * @return the id of the viewPager of this activity.
     */
    protected abstract int getViewPagerID();

    /**
     * Returns the id of the tabLayout of this activity.
     * @return the id of the tabLayout of this activity.
     */
    protected abstract int getTabID();

    /**
     * Adds the Fragment to show or set the HRV value details.
     * @param fragments list of fragments of this activity.
     */
    protected abstract void addDetailsFragment(List<Fragment> fragments);
}
