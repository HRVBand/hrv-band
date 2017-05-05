package hrv.band.app.ui.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas on 22.04.2017.
 */

public interface IHistoryPresenter {

    List<Measurement> getMeasurements();

    void updateMeasurements(Date date);

    String[] getPageTitles();

    List<Fragment> createFragments();

    int getTitlePosition(HRVParameterEnum value);

    boolean setDrawStrategy(int id, Date date);

    void drawChart(ColumnChartView chart, HRVParameterEnum hrvValue, Context context);
}
