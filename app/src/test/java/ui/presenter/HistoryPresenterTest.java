package ui.presenter;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.model.HRVParameterSettings;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HistoryPresenter;
import hrv.band.app.ui.presenter.IHistoryPresenter;
import hrv.band.app.ui.view.activity.IHistoryView;
import hrv.band.app.ui.view.activity.history.measurementstrategy.AbstractParameterLoadStrategy;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2017
 * Created by Thomas on 22.04.2017.
 */
@RunWith(RobolectricTestRunner.class)
public class HistoryPresenterTest {
    private IHistoryPresenter presenter;

    private Date date;

    @Mock
    private AbstractParameterLoadStrategy parameterLoadStrategy;

    @Mock
    private Context context;

    @Mock
    private IHistoryView view;

    @Before
    public void setup() {
        date = new Date();

        MockitoAnnotations.initMocks(this);
        when(parameterLoadStrategy.loadParameter(date)).thenReturn(getMeasurements());

        presenter = new HistoryPresenter(view, RuntimeEnvironment.application);
    }

    @Test
    public void getPageTitleTest() {
        assertEquals(HRVParameterSettings.DefaultSettings.visibleHRVParameters.size(), presenter.getPageTitles().length);
    }

    private List<Measurement> getMeasurements() {
        List<Measurement> measurements = new ArrayList<>();
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(date, new double[]{1, 1, 1, 1, 1});
        Measurement measurement = builder.build();
        measurements.add(measurement);
        measurements.add(measurement);

        return measurements;
    }
}
