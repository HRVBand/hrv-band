package hrv.band.app.view;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import hrv.band.app.R;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.sqlite.HRVSQLController;
import hrv.band.app.view.adapter.MeasurementCategoryAdapter;
import hrv.band.app.view.fragment.MeasuredDetailsEditFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This Activity holds the Fragments which shows the actual HRV Measurement.
 */
public class HRVMeasurementActivity extends AbstractHRVActivity {


    @Override
    protected int getContentViewId() {
        return R.layout.activity_hrv_measurement;
    }

    @Override
    protected int getViewPagerID() {
        return R.id.measure_details_viewpager;
    }

    @Override
    protected int getTabID() {
        return R.id.measure_details_tabs;
    }

    @Override
    protected void addDetailsFragment(List<Fragment> fragments) {
        fragments.add(MeasuredDetailsEditFragment.newInstance());
    }

    /**
     * Saves the actual measured and calculated HRV parameter.
     * @param view the View calling this method.
     */
    public void saveMeasurement(View view) {
        IStorage storage = new HRVSQLController();
        setMeasurementDetails();
        storage.saveData(getApplicationContext(), getParameter());
        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Sets the details of the actual measured and calculated HRV parameter.
     */
    private void setMeasurementDetails() {
        MeasuredDetailsEditFragment fragment = null;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i) instanceof MeasuredDetailsEditFragment) {
                    fragment = (MeasuredDetailsEditFragment) fragments.get(i);
                    break;
                }
            }
        }
        getParameter().setRating(fragment != null ? fragment.getRating() : 0);
        getParameter().setCategory(fragment != null ? fragment.getCategory() : MeasurementCategoryAdapter.MeasureCategory.GENERAL);
        getParameter().setNote(fragment != null ? fragment.getNote() : "");
    }
}
