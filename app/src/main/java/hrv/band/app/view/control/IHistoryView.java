package hrv.band.app.view.control;

import android.content.Context;

import java.util.List;

import hrv.band.app.control.Measurement;
import hrv.band.app.view.activity.HistoryActivity;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.fragment.HistoryFragment;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 13.03.2017
 *
 * Interface between {@link HistoryActivity} and {@link HistoryFragment}.
 */
public interface IHistoryView {
    /**
     * Draws chart for given hrv value.
     * @param chart to draw into.
     * @param hrvValue to draw chart for.
     * @param context in which to draw chart.
     */
    void drawChart(ColumnChartView chart, HRVValue hrvValue,
                   Context context);

    /**
     * Returns parameters of {@link HistoryActivity}.
     * @return parameters.
     */
    List<Measurement> getMeasurements();

    /**
     * Updates all {@link HistoryFragment} in {@link HistoryActivity}.
     */
    void updateFragments();
}
