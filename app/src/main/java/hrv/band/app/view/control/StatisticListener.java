package hrv.band.app.view.control;

import android.content.Context;

import java.util.List;

import hrv.band.app.control.Measurement;
import hrv.band.app.view.activity.StatisticActivity;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.fragment.StatisticFragment;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 13.03.2017
 *
 * Interface between {@link StatisticActivity} and {@link StatisticFragment}.
 */
public interface StatisticListener {
    /**
     * Draws chart for given hrv value.
     * @param chart to draw into.
     * @param hrvValue to draw chart for.
     * @param context in which to draw chart.
     */
    void drawChart(ColumnChartView chart, HRVValue hrvValue,
                   Context context);

    /**
     * Returns parameters of {@link StatisticActivity}.
     * @return parameters.
     */
    List<Measurement> getMeasurements();

    /**
     * Updates all {@link StatisticFragment} in {@link StatisticActivity}.
     */
    void updateFragments();
}
