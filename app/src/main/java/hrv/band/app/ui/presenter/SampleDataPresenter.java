package hrv.band.app.ui.presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.room.AppDatabase;
import hrv.band.app.ui.view.adapter.MeasurementCategoryAdapter;

/**
 * Copyright (c) 2017
 * Created by Thomas on 29.04.2017.
 */

public class SampleDataPresenter implements ISampleDataPresenter {

    private Context context;

    private static final int RR_COUNT = 50;
    private static final int SAMPLE_COUNT = 20;

    public SampleDataPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void createSampleData() {
        List<Measurement> measurements = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        for (int i = 0; i < SAMPLE_COUNT; i++) {
            measurements.add(createSampleData(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        storeMeasurement(measurements);
    }

    private Measurement createSampleData(Date date) {
        double[] rrValues = createRRData();
        return createMeasurement(date, rrValues);
    }

    private void storeMeasurement(List<Measurement> measurements) {
        AppDatabase storage = AppDatabase.getDatabaseInstance(context);
        storage.measurementDao().saveData(measurements);
    }

    private Measurement createMeasurement(Date date, double[] rrValues) {
        Measurement.MeasurementBuilder measurementBuilder = new Measurement.MeasurementBuilder(date, rrValues);
        return measurementBuilder
                .rating(4.2)
                .category(MeasurementCategoryAdapter.MeasureCategory.GENERAL)
                .note("This is a sample data")
                .build();
    }

    private double[] createRRData() {
        double[] rrValues = new double[RR_COUNT];

        for (int i = 0; i < rrValues.length; i++) {
            rrValues[i] = getRandomDouble(0.5, 1.5);
        }
        return rrValues;
    }

    private double getRandomDouble(double min, double max) {
        Random r = new Random();
        return max + (min - max) * r.nextDouble();
    }
}
