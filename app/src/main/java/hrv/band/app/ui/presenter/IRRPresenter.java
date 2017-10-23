package hrv.band.app.ui.presenter;

import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.calc.manipulator.HRVDataManipulator;
import hrv.calc.parameter.HRVParameter;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */

public interface IRRPresenter {
    Measurement getMeasurement();

    void calculateRRStatistic();

    String getRRAverage();

    String getRRMin();

    String getRRMax();

    String getRRCount();

    List<HRVParameter> getParameters();

    double[] getFilteredData();

    void setFilter(HRVDataManipulator filter);
}
