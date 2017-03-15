package hrv.band.app.view.chart;

import android.content.Context;

import java.util.Date;
import java.util.List;

import hrv.band.app.control.Measurement;
import hrv.band.app.view.adapter.HRVValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 13.03.2017
 */


public interface StatisticListener {
    void drawChart(List<Measurement> parameters, ColumnChartView chart, HRVValue hrvValue,
                   Context context);

    List<Measurement> getParameters();

    Date getDate();

    void updateFragments(Date date);
}
