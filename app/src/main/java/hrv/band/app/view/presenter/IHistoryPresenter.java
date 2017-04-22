package hrv.band.app.view.presenter;

import java.util.Date;
import java.util.List;

import hrv.band.app.control.Measurement;
import hrv.band.app.view.control.chart.AbstractChartDrawStrategy;
import hrv.band.app.view.control.parameter.AbstractParameterLoadStrategy;

/**
 * Copyright (c) 2017
 * Created by Thomas on 22.04.2017.
 */

public interface IHistoryPresenter {
    List<Measurement> getMeasurements(Date date);

    void setDrawChartStrategy(AbstractChartDrawStrategy chartStrategy);

    void setParameterLoadStrategy(AbstractParameterLoadStrategy parameterStrategy);

    String[] getPageTitles();

    AbstractChartDrawStrategy getChartDrawStrategy();
}
