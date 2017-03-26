package hrv.band.app.view.control.parameter;

import android.content.Context;

import java.util.Date;
import java.util.List;

import hrv.band.app.control.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 25.03.2017
 *
 * Loads parameters of given date.
 */
public class ParameterLoadDayStrategy extends AbstractParameterLoadStrategy {

    @Override
    public List<Measurement> loadParameter(Context context, Date date) {
        return storage.loadData(context, date);
    }
}