package hrv.band.aurora.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hrv.band.aurora.R;
import hrv.band.aurora.view.fragment.AboutFragment;
import hrv.band.aurora.view.fragment.FeedbackDialogFragment;
import hrv.band.aurora.view.fragment.LicenseFragment;

public class ImprintActivity extends AppCompatActivity {
    private ImprintActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragments = new ArrayList<>();
        fragments.add(AboutFragment.newInstance());
        fragments.add(LicenseFragment.newInstance());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.imprint_viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.imprint_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void sendFeedback(View view) {
        FeedbackDialogFragment picker = new FeedbackDialogFragment();
        picker.show(getFragmentManager(), "Feedback");
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
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return getResources().getString(R.string.tab_about);
                case 1: return getResources().getString(R.string.tab_license);
                default: return "";
            }
        }
    }
}
