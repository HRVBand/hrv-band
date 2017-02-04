package hrv.band.app.view;

import android.support.v4.app.Fragment;
import android.view.View;

import java.util.List;

import hrv.band.app.R;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.sqlite.SQLController;
import hrv.band.app.view.fragment.MeasuredDetailsFragment;

import static hrv.band.app.view.StatisticActivity.RESULT_DELETED;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This Activity holds the Fragments which are showing past HRV Measurements.
 */
public class HRVValueActivity extends AbstractHRVActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_hrv_value;
    }

    @Override
    protected int getViewPagerID() {
        return R.id.hrv_viewpager;
    }

    @Override
    protected int getTabID() {
        return R.id.hrv_tabs;
    }

    @Override
    protected void addDetailsFragment(List<Fragment> fragments) {
        fragments.add(MeasuredDetailsFragment.newInstance(getParameter()));
    }

    /**
     * Deletes the actual parameter.
     * @param view the View calling this method.
     */
    public void deleteParameter(View view) {
        if (getParameter() == null) {
            return;
        }
        IStorage storage = new SQLController();
        storage.deleteData(getApplicationContext(), getParameter());
        setResult(RESULT_DELETED);
        this.finish();
    }

}
