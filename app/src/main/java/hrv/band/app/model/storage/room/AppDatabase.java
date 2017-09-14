package hrv.band.app.model.storage.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import hrv.band.app.model.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 14.09.2017
 */

@Database(entities = {Measurement.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MeasurementDao measurementDao();

    private static AppDatabase databaseInstance;

    public static synchronized AppDatabase getDatabaseInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "hrv_band_db").build();
        }
        return databaseInstance;
    }
}
