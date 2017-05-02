package ui.presenter;

import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HistoryPresenter;
import hrv.band.app.ui.presenter.IHistoryPresenter;
import hrv.band.app.ui.view.activity.history.measurementstrategy.AbstractParameterLoadStrategy;
import hrv.band.app.ui.view.adapter.HRVValue;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2017
 * Created by Thomas on 22.04.2017.
 */

public class HistoryPresenterTest {
    private IHistoryPresenter presenter;

    private Date date;

    @Mock
    private AbstractParameterLoadStrategy parameterLoadStrategy;

    @Mock
    private Activity activity;

    @Before
    public void setup() {
        date = new Date();

        MockitoAnnotations.initMocks(this);
        when(parameterLoadStrategy.loadParameter(date)).thenReturn(getMeasurements());

        presenter = new HistoryPresenter();
    }

    @Test
    public void getMeasurementsTest() {
        presenter.setParameterLoadStrategy(parameterLoadStrategy);
        assertEquals(2, presenter.getMeasurements(date).size());
    }

    @Test
    public void getPageTitleTest() {
        assertEquals(HRVValue.values().length, presenter.getPageTitles().length);
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
