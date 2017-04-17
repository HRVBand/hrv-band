package hrv.band.app.view.presenter;


import android.content.Context;

import hrv.band.app.control.Measurement;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.sqlite.HRVSQLController;
import hrv.band.app.view.adapter.MeasurementCategoryAdapter;
import hrv.band.app.view.fragment.MeasuredDetailsEditFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */

public class HRVPresenter implements IHRVPresenter {

    private Measurement measurement;


    public HRVPresenter(Measurement measurement) {
        this.measurement = measurement;
    }

    @Override
    public Measurement getMeasurement() {
        return measurement;
    }

    @Override
    public void saveMeasurement(Context context, MeasuredDetailsEditFragment fragment) {
        IStorage storage = new HRVSQLController();
        storage.saveData(context, createSavableMeasurement(fragment));
    }

    @Override
    public void deleteMeasurement(Context context) {
        if (measurement == null) {
            return;
        }
        IStorage storage = new HRVSQLController();
        storage.deleteData(context, measurement);
    }

    private Measurement createSavableMeasurement(MeasuredDetailsEditFragment fragment) {

        float rating = fragment != null ? fragment.getRating() : 0;
        MeasurementCategoryAdapter.MeasureCategory category = fragment != null ? fragment.getCategory() : MeasurementCategoryAdapter.MeasureCategory.GENERAL;
        String note = fragment != null ? fragment.getNote() : "";


        Measurement.MeasurementBuilder measurementBuilder = new Measurement.MeasurementBuilder(measurement)
                .rating(rating)
                .category(category)
                .note(note);
        return measurementBuilder.build();
    }
}
