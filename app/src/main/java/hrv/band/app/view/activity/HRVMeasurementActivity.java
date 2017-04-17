package hrv.band.app.view.activity;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import hrv.band.app.R;
import hrv.band.app.view.fragment.MeasuredDetailsEditFragment;
import hrv.band.app.view.fragment.TextDialogFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This Activity holds the Fragments which shows the actual HRV Measurement.
 */
public class HRVMeasurementActivity extends AbstractHRVActivity implements ISavableMeasurementView {
    private MeasuredDetailsEditFragment measuredDetailsEditFragment;

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
        measuredDetailsEditFragment = MeasuredDetailsEditFragment.newInstance();
        fragments.add(measuredDetailsEditFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hrv_measurement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            /** Saves the measurement. **/
            case R.id.menu_ic_save:
                saveMeasurement();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Saves the actual measured and calculated HRV parameter.
     */
    private void saveMeasurement() {
        presenter.saveMeasurement(getApplicationContext(), measuredDetailsEditFragment);
        Toast.makeText(this, R.string.common_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        CancelMeasurementFragment.newInstance().show(getSupportFragmentManager(), "cancel");
    }

    /**
     * Dialog which asks the user if he wants to save the measurement.
     */
    public static class CancelMeasurementFragment extends TextDialogFragment {

        public static TextDialogFragment newInstance() {
            return new CancelMeasurementFragment();
        }

        @Override
        public void positiveButton() {
            getActivity().finish();
        }

        @Override
        public void negativeButton() {
            CancelMeasurementFragment.this.getDialog().cancel();
        }

        @Override
        public String getDialogTitle() {
            return getResources().getString(R.string.hrv_measurement_cancel_title);
        }

        @Override
        public String getDialogDescription() {
            return getResources().getString(R.string.hrv_measurement_cancel_desc);
        }

        @Override
        public int getDialogPositiveLabel() {
            return R.string.common_yes;
        }

        @Override
        public int getDialogNegativeLabel() {
            return R.string.common_no;
        }
    }
}
