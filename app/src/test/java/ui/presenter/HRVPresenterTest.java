package ui.presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Date;

import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.HRVPresenter;
import hrv.band.app.ui.presenter.IHRVPresenter;
import hrv.band.app.ui.view.adapter.MeasurementCategoryAdapter;
import hrv.band.app.ui.view.fragment.IMeasuredDetailsView;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 27.04.2017
 */

@RunWith(RobolectricTestRunner.class)
public class HRVPresenterTest {
    private IHRVPresenter presenter;
    @Mock
    IMeasuredDetailsView details;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(details.getRating()).thenReturn(1.0f);
        when(details.getCategory()).thenReturn(MeasurementCategoryAdapter.MeasureCategory.GENERAL);
        when(details.getNote()).thenReturn("note");

        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(), new double[]{1, 1, 1, 1, 1});
        Measurement measurement = builder.build();

        presenter = new HRVPresenter(measurement, RuntimeEnvironment.application);
    }

    @Test
    public void saveMeasurementTest() {
        saveMeasurement();
    }

    @Test
    public void deleteMeasurementTest() {
        saveMeasurement();

        presenter.deleteMeasurement();
        assertTrue(presenter.getStorage().loadData(new Date()).isEmpty());
    }

    private void saveMeasurement() {
        presenter.saveMeasurement(details);
        assertEquals(1, presenter.getStorage().loadData(new Date()).size());
    }

    @After
    public void tearDown() {
        presenter.getStorage().closeDatabaseHelper();
    }
}
