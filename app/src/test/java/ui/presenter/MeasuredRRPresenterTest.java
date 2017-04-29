package ui.presenter;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.IRRPresenter;
import hrv.band.app.ui.presenter.MeasuredRRPresenter;

import static junit.framework.Assert.assertEquals;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */

public class MeasuredRRPresenterTest {
    private IRRPresenter presenter;

    @Before
    public void setup() {
        Measurement measurement = new Measurement.MeasurementBuilder(new Date(1000), new double[]{1, 2}).build();
        presenter = new MeasuredRRPresenter(measurement);
    }

    @Test
    public void nullMeasurementTest() {
        presenter = new MeasuredRRPresenter(null);
        presenter.calculateRRStatistic();

        assertEquals("0", presenter.getRRCount());
        assertEquals("0", presenter.getRRAverage());
        assertEquals("0", presenter.getRRMin());
        assertEquals("0", presenter.getRRMax());
    }
    @Test
    public void calculateStatisticsTest() {
        presenter.calculateRRStatistic();

        assertEquals("2", presenter.getRRCount());
        assertEquals("1.5", presenter.getRRAverage());
        assertEquals("1", presenter.getRRMin());
        assertEquals("2", presenter.getRRMax());
    }
}
