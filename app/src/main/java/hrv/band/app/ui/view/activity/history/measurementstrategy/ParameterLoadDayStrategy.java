package hrv.band.app.ui.view.activity.history.measurementstrategy;

import android.content.Context;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 25.03.2017
 * <p>
 * Loads parameters of given date.
 */
public class ParameterLoadDayStrategy extends AbstractParameterLoadStrategy {

    @Override
    public List<Measurement> loadParameter(Context context, Date date) {
        return storage.loadData(context, date);
    }
}