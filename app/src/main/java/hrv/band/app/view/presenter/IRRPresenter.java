package hrv.band.app.view.presenter;

import hrv.band.app.control.Measurement;

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
}
