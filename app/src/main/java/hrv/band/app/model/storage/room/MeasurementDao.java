package hrv.band.app.model.storage.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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

    @Query("SELECT * From Measurement")
    LiveData<List<Measurement>> loadData();

    @Query("SELECT * From Measurement WHERE id = :id")
    LiveData<Measurement> loadData(int id);

    @Query("SELECT * From Measurement WHERE date = :date")
    LiveData<List<Measurement>> loadData(Date date);

    @Query("SELECT * From Measurement WHERE date BETWEEN :startDate AND :endDate")
    LiveData<List<Measurement>> loadData(Date startDate, Date endDate);

    @Delete
    void deleteData(Measurement parameter);

}
