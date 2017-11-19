package hrv.band.app.ui.presenter;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.room.AppDatabase;
import hrv.band.app.ui.view.activity.history.chartstrategy.AbstractChartDrawStrategy;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.view.ColumnChartView;

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

    public LiveData<List<Measurement>> getMeasurements(Date startDate, Date endDate) {
        return appDatabase.measurementDao().loadData(startDate, endDate);
    }

    public void drawChart(AbstractChartDrawStrategy chartStrategy, ColumnChartView chart,
                          List<Measurement> measurements, HRVParameterEnum hrvValue, Context context) {
        chartStrategy.drawChart(measurements, chart, hrvValue, context);
    }
}
