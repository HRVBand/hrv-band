package hrv.band.app.ui.presenter;

import android.content.Context;

import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.fragment.IMeasuredDetailsView;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */

public interface IHRVPresenter {
    Measurement getMeasurement();

    void saveMeasurement(Context context, IMeasuredDetailsView fragment);
    void deleteMeasurement(Context context);
}
