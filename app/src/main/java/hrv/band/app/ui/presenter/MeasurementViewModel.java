package hrv.band.app.ui.presenter;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.room.AppDatabase;
import hrv.band.app.ui.view.adapter.MeasurementCategoryAdapter;
import hrv.band.app.ui.view.fragment.IMeasuredDetailsView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 18.09.2017
 */

public class MeasurementViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    public MeasurementViewModel(Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabaseInstance(this.getApplication());
    }

    public void addMeasurement(IMeasuredDetailsView details, Measurement measurement) {
        addMeasurement(createSavableMeasurement(details, measurement));
    }
    public void addMeasurement(Measurement measurement) {
        addMeasurements(Arrays.asList(measurement));
    }
    public void addMeasurements(List<Measurement> measurements) {
        new addAsyncTask(appDatabase).execute(measurements);
    }

    private void updateMeasurement(Measurement measurement) {
        new updateAsyncTask(appDatabase).execute(measurement);
    }

    public void deleteMeasurement(Measurement measurement) {
        new deleteAsyncTask(appDatabase).execute(measurement);
    }

    public void saveMeasurement(Measurement measurement) {
        if(measurement.hasId()) {
            updateMeasurement(measurement);
        } else {
            measurement.setId(0);
            addMeasurement(measurement);
        }
    }

    private Measurement createSavableMeasurement(IMeasuredDetailsView details, Measurement measurement) {

        float rating = details != null ? details.getRating() : 0;
        MeasurementCategoryAdapter.MeasureCategory category = details != null ? details.getCategory() : MeasurementCategoryAdapter.MeasureCategory.GENERAL;
        String note = details != null ? details.getNote() : "";


        Measurement.MeasurementBuilder measurementBuilder = new Measurement.MeasurementBuilder(measurement)
                .rating(rating)
                .category(category)
                .note(note);
        return measurementBuilder.build();
    }



    private static class addAsyncTask extends AsyncTask<List<Measurement>, Void, Void> {
        private AppDatabase db;

        addAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final List<Measurement>... params) {
            db.measurementDao().saveData(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Measurement, Void, Void> {
        private AppDatabase db;

        deleteAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Measurement... params) {
            db.measurementDao().deleteData(params[0]);
            return null;
        }
    }


    private static class updateAsyncTask extends AsyncTask<Measurement, Void, Void> {
        private AppDatabase db;

        updateAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(Measurement... measurements) {
            db.measurementDao().update(measurements[0]);
            return null;
        }
    }
}
