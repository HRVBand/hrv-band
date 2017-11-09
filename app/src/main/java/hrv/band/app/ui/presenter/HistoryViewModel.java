package hrv.band.app.ui.presenter;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.room.AppDatabase;
import hrv.band.app.ui.view.activity.history.chartstrategy.AbstractChartDrawStrategy;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.view.ColumnChartView;

import hrv.band.app.ui.view.util.DateUtil;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 17.09.2017
 */


public class HistoryViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private static MutableLiveData<List<Measurement>> todayMeasurements = new MutableLiveData<>();
    private static MutableLiveData<List<Measurement>> weekMeasurements = new MutableLiveData<>();
    private static MutableLiveData<List<Measurement>> monthMeasurements = new MutableLiveData<>();

    public HistoryViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabaseInstance(this.getApplication());

        setMeasurements(new Date());
    }


    public void setMeasurements(Date date) {
        new SetMeasurementAsyncTask(appDatabase).execute(date);
    }

    public LiveData<List<Measurement>> getTodayMeasurements() {
        return todayMeasurements;
    }
    public LiveData<List<Measurement>> getWeekMeasurements() {
        return weekMeasurements;
    }
    public LiveData<List<Measurement>> getMonthMeasurements() {
        return monthMeasurements;
    }

    public void drawChart(AbstractChartDrawStrategy chartStrategy, ColumnChartView chart,
                          List<Measurement> measurements, HRVParameterEnum hrvValue, Context context) {
        chartStrategy.drawChart(measurements, chart, hrvValue, context);
    }

    private static class SetMeasurementAsyncTask extends AsyncTask<Date, Void, Void> {
        private AppDatabase db;

        SetMeasurementAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Date... date) {
            todayMeasurements.postValue(db.measurementDao().loadData(DateUtil.getStartOfDay(date[0]), DateUtil.getEndOfDay(date[0])));
            weekMeasurements.postValue(db.measurementDao().loadData(DateUtil.getStartOfWeek(date[0]), DateUtil.getEndOfWeek(date[0])));
            monthMeasurements.postValue(db.measurementDao().loadData(DateUtil.getStartOfMonth(date[0]), DateUtil.getEndOfMonth(date[0])));
            return null;
        }
    }




}
