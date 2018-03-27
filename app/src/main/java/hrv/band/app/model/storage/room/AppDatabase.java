package hrv.band.app.model.storage.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import hrv.band.app.model.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 14.09.2017
 */

@Database(entities = {Measurement.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MeasurementDao measurementDao();

    private static AppDatabase databaseInstance;

    public static synchronized AppDatabase getDatabaseInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "HRVParamDB.db").
                    addMigrations(new Migration(1,2) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("CREATE TABLE MEASUREMENT " +
                            "(id INTEGER PRIMARY KEY NOT NULL, time INTEGER, rrIntervals TEXT, category TEXT, rating REAL NOT NULL DEFAULT 0, note TEXT)");

                    //database.execSQL("ALTER TABLE HRVEntry_new ADD COLUMN rating REAL NOT NULL");

                    database.execSQL("INSERT INTO MEASUREMENT (id, time, category, rating, note) SELECT id, time, category, rating, note FROM HRVEntry");
                    database.execSQL("INSERT INTO MEASUREMENT (rrIntervals) SELECT GROUP_CONCAT(rrvalue, ',') FROM RRIinterval, HRVEntry WHERE HRVEntry.rrid = RRIinterval.id GROUP BY HRVEntry.rrid");

                    //database.execSQL("DROP TABLE HRVEntry");
                    //database.execSQL("DROP TABLE RRIinterval");

                    //database.execSQL("ALTER TABLE HRVEntry_new RENAME TO HRVEntry");
                }
            }).build();
        }
        return databaseInstance;
    }
}
