package hrv.band.app.model.storage.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 14.09.2017
 */

@Dao
public interface MeasurementDao {

    @Insert
    void saveData(Measurement parameter);

    @Insert
    void saveData(List<Measurement> parameter);

    @Query("SELECT * From MEASUREMENT")
    LiveData<List<Measurement>> loadData();

    @Query("SELECT * From MEASUREMENT WHERE id = :id")
    LiveData<Measurement> loadData(int id);

    @Query("SELECT * From MEASUREMENT WHERE time = :date")
    LiveData<List<Measurement>> loadData(Date date);

    @Query("SELECT * From MEASUREMENT WHERE time BETWEEN :startDate AND :endDate ORDER BY time ASC")
    LiveData<List<Measurement>> loadData(Date startDate, Date endDate);

    @Update
    void update(Measurement measurement);

    @Delete
    void deleteData(Measurement parameter);

}
