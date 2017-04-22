package hrv.band.app.ui.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import hrv.band.app.ui.view.fragment.FragmentObserver;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionPagerAdapter extends FragmentPagerAdapter {

    /**
     * The Fragments contained in this adapter.
     **/
    private final List<Fragment> fragments;
    /**
     * The titles of the single Fragments.
     **/
    private final String[] pageTitles;

    private Observable mObservers = new FragmentObserver();

    public SectionPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] pageTitles) {
        super(fm);
        this.fragments = fragments;
        this.pageTitles = pageTitles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment instanceof Observer) {
            mObservers.addObserver((Observer) fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }

    public void updateFragments() {
        mObservers.notifyObservers();
    }
}
