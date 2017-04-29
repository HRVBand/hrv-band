package ui.presenter;

import org.junit.Test;

import java.util.Date;

import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HRVParameterPresenter;
import hrv.band.app.ui.presenter.IHRVParameterPresenter;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */

public class HRVParameterPresenterTest {

    private IHRVParameterPresenter presenter;

    @Test
    public void calculateParameterTest() {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(1000), new double[]{1, 1, 1, 1, 1});
        presenter = new HRVParameterPresenter(builder.build());

        presenter.calculateParameters();
        assertFalse(presenter.getHRVParameters().isEmpty());
    }

    @Test
    public void calculateParameterNullMeasurementTest() {
        presenter = new HRVParameterPresenter(null);
        presenter.calculateParameters();
        assertTrue(presenter.getHRVParameters().isEmpty());
    }

    @Test
    public void calculateParameterInvalidMeasurementTest() {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(1000), new double[]{1, 1});
        presenter = new HRVParameterPresenter(builder.build());

        presenter.calculateParameters();
        assertTrue(presenter.getHRVParameters().isEmpty());
    }
}
