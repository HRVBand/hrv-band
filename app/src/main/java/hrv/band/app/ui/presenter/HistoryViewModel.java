package hrv.band.app.ui.presenter;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.room.AppDatabase;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 17.09.2017
 */


public class HistoryViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    public HistoryViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabaseInstance(this.getApplication());
    }


    public LiveData<List<Measurement>> getMeasurements(Date date) {
        return appDatabase.measurementDao().loadData(getStartOfDay(date), getEndOfDay(date));
    }

    private static Date getEndOfDay(Date date) {
        return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
    }

    private static Date getStartOfDay(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }


}
