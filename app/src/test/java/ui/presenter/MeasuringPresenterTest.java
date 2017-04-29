package ui.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.IMeasuringPresenter;
import hrv.band.app.ui.presenter.MeasuringPresenter;
import hrv.band.app.ui.view.fragment.IMeasuringView;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 27.04.2017
 */

public class MeasuringPresenterTest {

    private IMeasuringPresenter presenter;

    private List<Double> rrInterval;

    @Mock
    private IMeasuringView measuringView;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new MeasuringPresenter(measuringView);
        rrInterval = new ArrayList<>();
    }

    @Test
    public void createMeasurementTest() {
        rrInterval.add(1.0);
        rrInterval.add(1.0);
        rrInterval.add(1.0);
        rrInterval.add(1.0);

        Measurement measurement = presenter.createMeasurement(rrInterval, new Date());
        assertNotNull(measurement);
    }

    @Test
    public void createMeasurementInvalidTest() {
        Measurement measurement = presenter.createMeasurement(rrInterval, new Date());
        assertNull(measurement);
    }
}
