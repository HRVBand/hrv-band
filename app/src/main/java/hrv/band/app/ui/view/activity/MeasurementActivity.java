package hrv.band.app.ui.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HRVParameterPresenter;
import hrv.band.app.ui.presenter.IHRVParameterPresenter;
import hrv.band.app.ui.presenter.MeasurementViewModel;
import hrv.band.app.ui.view.adapter.MeasurementCategoryAdapter;
import hrv.band.app.ui.view.adapter.MeasurementResultListAdapter;
import hrv.band.app.ui.view.fragment.TextDialogFragment;

import static hrv.band.app.ui.view.activity.web.WebsiteUrls.WEBSITE_PARAMETER_URL;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 16.11.2017
 */


public class MeasurementActivity extends AppCompatActivity {
    private IHRVParameterPresenter presenter;

    protected MeasurementViewModel measurementViewModel;
    protected Measurement measurement; //TODO change to id and create viewmodel to get measurement by id, therefore measurement has to be in the database already
    protected int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrv_value);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        measurement = getIntent().getParcelableExtra(MainActivity.HRV_PARAMETER_ID);
        id = getIntent().getIntExtra(MainActivity.HRV_PARAMETER_ID_ID, -1);

        measurement.setId(id);
        //presenter = new HRVPresenter(measurement, this);

        measurementViewModel = ViewModelProviders.of(this).get(MeasurementViewModel.class);

        presenter = new HRVParameterPresenter(measurement);
        presenter.calculateParameters();

        setParametersListView();
    }

    @Override
    public void onBackPressed() {
        MeasurementActivity.CancelMeasurementFragment.newInstance().show(getSupportFragmentManager(), "cancel");
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
            case R.id.menu_ic_edit:
                return true;
            case R.id.menu_ic_save:
                saveMeasurement();
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
        finish();
    }

    /**
     * Saves the actual measured and calculated HRV parameter.
     */
    private void saveMeasurement() {
        measurementViewModel.saveMeasurement(createSavableMeasurement(measurement));
        Toast.makeText(this, R.string.common_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private Measurement createSavableMeasurement(Measurement measurement) {

        /*float rating = details != null ? details.getRating() : 0;
        findViewById(R.id.)
        MeasurementCategoryAdapter.MeasureCategory category = details != null ? details.getCategory() : MeasurementCategoryAdapter.MeasureCategory.valueOf();
        String note = details != null ? details.getNote() : "";*/


        Measurement.MeasurementBuilder measurementBuilder = new Measurement.MeasurementBuilder(measurement)
                .rating(3)
                .category(MeasurementCategoryAdapter.MeasureCategory.GENERAL)
                .note("note");
        return measurementBuilder.build();
    }

    private void setParametersListView() {
        ListView listview = findViewById(R.id.hrv_value_list);
        MeasurementResultListAdapter adapter = new MeasurementResultListAdapter(getApplicationContext(), presenter.getMeasurement());

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra(WebActivity.WEBSITE_URL_ID, WEBSITE_PARAMETER_URL);
                startActivity(intent);
            }

        });
    }

    /**
     * Dialog which asks the user if he wants to save the measurement.
     */
    public static class CancelMeasurementFragment extends TextDialogFragment {

        public static TextDialogFragment newInstance() {
            return new MeasurementActivity.CancelMeasurementFragment();
        }

        @Override
        public void positiveButton() {
            getActivity().finish();
        }

        @Override
        public void negativeButton() {
            MeasurementActivity.CancelMeasurementFragment.this.getDialog().cancel();
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
