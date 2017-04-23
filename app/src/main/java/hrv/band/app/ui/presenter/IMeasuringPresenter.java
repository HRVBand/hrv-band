package hrv.band.app.ui.presenter;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 23.04.2017
 */

public interface IMeasuringPresenter {
    int getDuration();

    Measurement createMeasurement(List<Double> rrInterval, Date time);
}
