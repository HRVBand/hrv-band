package hrv.band.app.view.chart;

import android.content.Context;

import java.util.List;

import hrv.band.app.control.Measurement;
import hrv.band.app.view.adapter.HRVValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 07.03.2017
 */


public class ChartStrategyContext {
    private AbstractChartDrawStrategy strategy = null;
    public void setStrategy(AbstractChartDrawStrategy strategy) {
        this.strategy = strategy;
    }

    public void drawChart(List<Measurement> parameters, ColumnChartView mChart, HRVValue hrvValue,
                          Context context) {
        strategy.drawChart(parameters, mChart, hrvValue, context);
    }
}
