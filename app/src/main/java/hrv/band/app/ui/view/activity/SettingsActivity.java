package hrv.band.app.ui.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import hrv.band.app.R;
import hrv.band.app.ui.presenter.ISampleDataPresenter;
import hrv.band.app.ui.presenter.MeasurementViewModel;
import hrv.band.app.ui.presenter.SampleDataPresenter;
import hrv.band.app.ui.view.fragment.SettingsFragment;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 24.01.2017.
 * <p>
 * Preferences are just one single fragment that contain all options grouped by
 * category
 */

public class SettingsActivity extends AppCompatActivity implements ISettings {

    private MeasurementViewModel measurementViewModel;
    private ISampleDataPresenter presenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new SampleDataPresenter();

        measurementViewModel = ViewModelProviders.of(this).get(MeasurementViewModel.class);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //Replace Activity with settings fragment content
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
    }

    @Override
    public void createSampleData() {
        presenter.createSampleData(measurementViewModel);
    }
}
