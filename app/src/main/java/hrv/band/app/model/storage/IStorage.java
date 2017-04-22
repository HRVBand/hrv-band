package hrv.band.app.model.storage;

import android.content.Context;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 18.06.2016.
 */
public interface IStorage {
    void saveData(Context context, List<Measurement> parameters);

    void saveData(Context context, Measurement parameter);

    List<Measurement> loadData(Context context, Date date);

    boolean deleteData(Context context, Measurement parameter);

    boolean deleteData(Context context, List<Measurement> parameters);
}
