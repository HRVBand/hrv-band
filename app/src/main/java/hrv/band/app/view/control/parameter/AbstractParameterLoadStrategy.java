package hrv.band.app.view.control.parameter;

import android.content.Context;

import java.util.Date;
import java.util.List;

import hrv.band.app.control.Measurement;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.sqlite.HRVSQLController;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 25.03.2017
 *
 * Strategy to select parameters from db.
 */
public abstract class AbstractParameterLoadStrategy {
    protected IStorage storage = new HRVSQLController();

    /**
     * Loads parameters from db.
     * @param context for db selection.
     * @param date from which to select parameters.
     * @return parameters from db.
     */
    public abstract List<Measurement> loadParameter(Context context, Date date);
}
