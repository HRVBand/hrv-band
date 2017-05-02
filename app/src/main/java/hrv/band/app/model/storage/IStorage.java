package hrv.band.app.model.storage;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 18.06.2016.
 */
public interface IStorage {
    void saveData(List<Measurement> parameters);

    void saveData(Measurement parameter);

    List<Measurement> loadData(Date date);

    List<Measurement> loadData(Date startDate, Date endDate);

    boolean deleteData(Measurement parameter);

    boolean deleteData(List<Measurement> parameters);

    void closeDatabaseHelper();
}
