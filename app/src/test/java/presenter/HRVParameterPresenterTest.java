package presenter;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HRVParameterPresenter;
import hrv.band.app.ui.presenter.IHRVParameterPresenter;

import static junit.framework.Assert.assertFalse;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */

public class HRVParameterPresenterTest {

    private IHRVParameterPresenter presenter;

    @Before
    public void setup() {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(1000), new double[]{1, 1, 1, 1, 1});
        presenter = new HRVParameterPresenter(builder.build());
    }

    @Test
    public void calculateParameterTest() {
        presenter.calculateParameters();
        assertFalse(presenter.getHRVParameters().isEmpty());
    }
}
