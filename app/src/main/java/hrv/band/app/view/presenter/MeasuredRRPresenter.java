package hrv.band.app.view.presenter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import hrv.band.app.control.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */

public class MeasuredRRPresenter implements IRRPresenter {
    private Measurement measurement;
    private double rrAverage;
    private double rrMin;
    private double rrMax;
    private int rrCount;

    public MeasuredRRPresenter(Measurement measurement) {
        this.measurement = measurement;
    }

    @Override
    public Measurement getMeasurement() {
        return measurement;
    }

    @Override
    public void calculateRRStatistic() {
        if (measurement == null) {
            return;
        }
        double[] rrIntervals = measurement.getRRIntervals();
        rrCount = rrIntervals.length;
        rrMin = Double.MAX_VALUE;

        for (double aRr : rrIntervals) {
            rrAverage += aRr;
            if (rrMin > aRr) {
                rrMin = aRr;
            }
            if (rrMax < aRr) {
                rrMax = aRr;
            }
        }
        rrAverage /= rrIntervals.length;
    }

    @Override
    public String getRRAverage() {
        return trimValue(rrAverage);
    }

    @Override
    public String getRRMin() {
        return trimValue(rrMin);
    }

    @Override
    public String getRRMax() {
        return trimValue(rrMax);
    }

    @Override
    public String getRRCount() {
        return String.valueOf(rrCount);
    }

    private String trimValue(double value) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        return new DecimalFormat("#.####", symbols).format(value);
    }
}
