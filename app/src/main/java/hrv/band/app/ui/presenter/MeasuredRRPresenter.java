package hrv.band.app.ui.presenter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import hrv.HRVLibFacade;
import hrv.RRData;
import hrv.band.app.model.HRVParameterUnitAdapter;
import hrv.band.app.model.Measurement;
import hrv.calc.manipulator.HRVDataManipulator;
import hrv.calc.manipulator.window.NoWindow;
import hrv.calc.parameter.HRVParameter;
import units.TimeUnit;

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
    private HRVDataManipulator filter = new NoWindow();
    private List<HRVParameter> parameters;

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
        double[] rrIntervals = getFilteredData();

        rrCount = rrIntervals.length;
        rrMin = Double.MAX_VALUE;
        rrMax = Double.MIN_VALUE;

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

        HRVLibFacade facade = new HRVLibFacade(RRData.createFromRRInterval(measurement.getRRIntervals(), TimeUnit.SECOND));
        facade.addDataFilter(filter);

        parameters = facade.calculateParameters();

        HRVParameterUnitAdapter unitAdapter = new HRVParameterUnitAdapter();
        unitAdapter.adaptParameters(parameters);
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

    @Override
    public List<HRVParameter> getParameters() {
        return parameters;
    }

    @Override
    public void setFilter(HRVDataManipulator filter) {
        this.filter = filter;
    }

    public double[] getFilteredData() {
        return filter.manipulate(RRData.createFromRRInterval(measurement.getRRIntervals(), TimeUnit.SECOND)).getValueAxis();
    }

    private String trimValue(double value) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        return new DecimalFormat("#.####", symbols).format(value);
    }
}
