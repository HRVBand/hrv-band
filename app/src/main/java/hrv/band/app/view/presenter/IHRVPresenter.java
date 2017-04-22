package hrv.band.app.view.presenter;

import android.content.Context;

import hrv.band.app.control.Measurement;
import hrv.band.app.view.fragment.IMeasuredDetails;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */

public interface IHRVPresenter {
    Measurement getMeasurement();
    void saveMeasurement(Context context, IMeasuredDetails fragment);
    void deleteMeasurement(Context context);
}
