package hrv.band.app.ui.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HRVPresenter;
import hrv.band.app.ui.presenter.IHRVPresenter;
import hrv.band.app.ui.view.adapter.SectionPagerAdapter;
import hrv.band.app.ui.view.fragment.MeasuredParameterFragment;
import hrv.band.app.ui.view.fragment.MeasuredRRFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This abstract Activity holds the fragments to show a specific HRV value.
 */
public abstract class AbstractMeasurementActivity extends AppCompatActivity {
    protected IHRVPresenter presenter;
    /**
     * The Fragments this Activity holds.
     **/
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        presenter = new HRVPresenter((Measurement) getIntent().getParcelableExtra(MainActivity.HRV_PARAMETER_ID), this);

        createFragments();

        setViewPager();
    }

    @Override
    public abstract void onBackPressed();

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
     *
     * @return the page titles of the fragments.
     */
    private String[] getPageTitles() {
        return new String[]{
                getResources().getString(R.string.hrv_activity_tab_value),
                getResources().getString(R.string.hrv_activity_tab_rr),
                getResources().getString(R.string.hrv_activity_tab_details)
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
        fragments = new ArrayList<>();
        fragments.add(MeasuredParameterFragment.newInstance(presenter.getMeasurement()));
        fragments.add(MeasuredRRFragment.newInstance(presenter.getMeasurement()));
        addDetailsFragment(fragments);
    }

    /**
     * Returns the id of the layout of this activity.
     *
     * @return the id of the layout of this activity.
     */
    protected abstract int getContentViewId();

    /**
     * Returns the id of the viewPager of this activity.
     *
     * @return the id of the viewPager of this activity.
     */
    protected abstract int getViewPagerID();

    /**
     * Returns the id of the tabLayout of this activity.
     *
     * @return the id of the tabLayout of this activity.
     */
    protected abstract int getTabID();

    /**
     * Adds the Fragment to show or set the HRV value details.
     *
     * @param fragments list of fragments of this activity.
     */
    protected abstract void addDetailsFragment(List<Fragment> fragments);
}
