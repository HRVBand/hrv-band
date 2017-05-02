package hrv.band.app.ui.view.activity.history.measurementstrategy;

import android.content.Context;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.IStorage;
import hrv.band.app.model.storage.sqlite.HRVSQLController;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 25.03.2017
 * <p>
 * Strategy to select parameters from db.
 */
public abstract class AbstractParameterLoadStrategy {
    protected IStorage storage;

    AbstractParameterLoadStrategy(Context context) {
        this.storage = new HRVSQLController(context);
    }

    public abstract List<Measurement> loadParameter(Date date);
}
