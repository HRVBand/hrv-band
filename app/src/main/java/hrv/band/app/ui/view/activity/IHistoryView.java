package hrv.band.app.ui.view.activity;

import android.content.Context;

import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.fragment.OverviewFragment;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 13.03.2017
 * <p>
 * Interface between {@link HistoryActivity} and {@link OverviewFragment}.
 */
public interface IHistoryView {

    void drawChart(ColumnChartView chart, HRVParameterEnum hrvValue, Context context);

    List<Measurement> getMeasurements();

    void updateFragments();
}
