package hrv.band.app.ui.view.activity;

import java.util.Date;

import hrv.band.app.ui.view.fragment.OverviewFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 13.03.2017
 * <p>
 * Interface between {@link HistoryActivity} and {@link OverviewFragment}.
 */
public interface IHistoryView {

    void updateMeasurements(Date date);

}
