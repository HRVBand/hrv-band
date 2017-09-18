package hrv.band.app.ui.view.activity;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import hrv.band.app.R;
import hrv.band.app.ui.view.fragment.MeasuredDetailsFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This Activity holds the Fragments which are showing past HRV Measurements.
 */
public class EditableMeasurementActivity extends AbstractMeasurementActivity {

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
        fragments.add(MeasuredDetailsFragment.newInstance(measurement));
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
            case R.id.menu_ic_delete:
                deleteMeasurement();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Deletes the actual parameter.
     */
    private void deleteMeasurement() {
        measurementViewModel.deleteMeasurement(measurement);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
