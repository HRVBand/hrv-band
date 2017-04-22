package hrv.band.app.ui.presenter;

import hrv.band.app.model.Measurement;

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
