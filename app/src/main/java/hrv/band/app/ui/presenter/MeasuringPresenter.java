package hrv.band.app.ui.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;
import java.util.List;

import common.ArrayUtils;
import hrv.RRData;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.fragment.IMeasuringView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 23.04.2017
 */

public class MeasuringPresenter implements IMeasuringPresenter {
    private SharedPreferences sharedPreferences;

    private static final String RECORDING_LENGTH_ID = "recording_length";


    public MeasuringPresenter(IMeasuringView measuringView) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(measuringView.getParentActivity());
    }

    @Override
    public int getDuration() {
        //TODO: int statt string
        String durationPrefVal = sharedPreferences.getString(RECORDING_LENGTH_ID, "90");
        return Integer.parseInt(durationPrefVal) * 1000;
    }

    @Override
    public Measurement createMeasurement(List<Double> rrInterval, Date time) {
        //start calculation
        double[] rrArray = convertListToDouble(rrInterval);

        RRData.createFromRRInterval(rrArray, units.TimeUnit.SECOND);
        Measurement.MeasurementBuilder measurementBuilder = Measurement.from(time, rrArray);
        return measurementBuilder.build();
    }

    private double[] convertListToDouble(List<Double> doubles) {
        return ArrayUtils.toPrimitiveIgnoreNull(doubles.toArray(new Double[0]));
    }
}
