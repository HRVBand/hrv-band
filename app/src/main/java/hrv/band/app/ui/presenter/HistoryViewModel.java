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

    public HistoryViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getDatabaseInstance(this.getApplication());
    }

    public LiveData<List<Measurement>> getTodayMeasurements(Date date) {
        return appDatabase.measurementDao().loadData(DateUtil.getStartOfDay(date), DateUtil.getEndOfDay(date));
    }
    public LiveData<List<Measurement>> getWeekMeasurements(Date date) {
        return appDatabase.measurementDao().loadData(DateUtil.getStartOfWeek(date), DateUtil.getEndOfWeek(date));
    }
    public LiveData<List<Measurement>> getMonthMeasurements(Date date) {
        return appDatabase.measurementDao().loadData(DateUtil.getStartOfMonth(date), DateUtil.getEndOfMonth(date));
    }

    public void drawChart(AbstractChartDrawStrategy chartStrategy, ColumnChartView chart,
                          List<Measurement> measurements, HRVParameterEnum hrvValue, Context context) {
        chartStrategy.drawChart(measurements, chart, hrvValue, context);
    }
}
