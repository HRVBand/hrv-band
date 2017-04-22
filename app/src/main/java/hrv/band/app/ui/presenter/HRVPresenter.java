package hrv.band.app.ui.presenter;


import android.content.Context;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.IStorage;
import hrv.band.app.model.storage.sqlite.HRVSQLController;
import hrv.band.app.ui.view.adapter.MeasurementCategoryAdapter;
import hrv.band.app.ui.view.fragment.IMeasuredDetailsView;

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
    public void saveMeasurement(Context context, IMeasuredDetailsView details) {
        IStorage storage = new HRVSQLController();
        storage.saveData(context, createSavableMeasurement(details));
    }

    @Override
    public void deleteMeasurement(Context context) {
        if (measurement == null) {
            return;
        }
        IStorage storage = new HRVSQLController();
        storage.deleteData(context, measurement);
    }

    private Measurement createSavableMeasurement(IMeasuredDetailsView details) {

        float rating = details != null ? details.getRating() : 0;
        MeasurementCategoryAdapter.MeasureCategory category = details != null ? details.getCategory() : MeasurementCategoryAdapter.MeasureCategory.GENERAL;
        String note = details != null ? details.getNote() : "";


        Measurement.MeasurementBuilder measurementBuilder = new Measurement.MeasurementBuilder(measurement)
                .rating(rating)
                .category(category)
                .note(note);
        return measurementBuilder.build();
    }
}
