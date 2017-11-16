package hrv.band.app.ui.view.activity.history.measurementstrategy;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.room.AppDatabase;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 25.03.2017
 * <p>
 * Strategy to select parameters from db.
 */
public abstract class AbstractParameterLoadStrategy {
    protected AppDatabase database;

    AbstractParameterLoadStrategy(Context context) {
        this.database = AppDatabase.getDatabaseInstance(context);
    }

    public abstract LiveData<List<Measurement>> loadParameter(Date date);
}
