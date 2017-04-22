package hrv.band.app.ui.presenter;

import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.activity.history.chartstrategy.AbstractChartDrawStrategy;
import hrv.band.app.ui.view.activity.history.measurementstrategy.AbstractParameterLoadStrategy;

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
