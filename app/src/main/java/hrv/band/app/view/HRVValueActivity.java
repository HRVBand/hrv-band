package hrv.band.app.view;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import hrv.band.app.R;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.sqlite.HRVSQLController;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hrv_value, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            /** Deletes the measurement. **/
            case R.id.menu_ic_delete:
                deleteParameter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Deletes the actual parameter.
     */
    private void deleteParameter() {
        if (getParameter() == null) {
            return;
        }
        IStorage storage = new HRVSQLController();
        storage.deleteData(getApplicationContext(), getParameter());
        setResult(RESULT_DELETED);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
