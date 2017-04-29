package ui.presenter;

import android.app.Activity;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.presenter.IMeasurementDetailsPresenter;
import hrv.band.app.ui.presenter.MeasurementDetailsPresenter;
import hrv.band.app.ui.view.adapter.MeasurementCategoryAdapter;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */
public class MeasurementDetailsPresenterTest {
    private Measurement.MeasurementBuilder builder;

    @Mock
    private Activity activity;

    @Mock
    private Resources resources;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(activity.getResources()).thenReturn(resources);
        when(resources.getText(R.string.measure_category_general)).thenReturn("General");

        builder = new Measurement.MeasurementBuilder(new Date(1000), new double[]{1, 1, 1, 1, 1});
    }

    @Test
    public void getNoteTest() {
        builder.note("abc");
        IMeasurementDetailsPresenter presenter = new MeasurementDetailsPresenter(builder.build(), activity);
        assertEquals("abc", presenter.getNote());
    }

    @Test
    public void getEmptyNoteTest() {
        builder.note("");
        IMeasurementDetailsPresenter presenter = new MeasurementDetailsPresenter(builder.build(), activity);
        assertEquals("", presenter.getNote());
    }

    @Test
    public void getRatingTest() {
        builder.rating(1.5);
        IMeasurementDetailsPresenter presenter = new MeasurementDetailsPresenter(builder.build(), activity);
        assertEquals("1.5/5", presenter.getRating());
    }

    @Test
    public void getCategoryTest() {
        builder.category(MeasurementCategoryAdapter.MeasureCategory.GENERAL);
        IMeasurementDetailsPresenter presenter = new MeasurementDetailsPresenter(builder.build(), activity);
        assertEquals(MeasurementCategoryAdapter.MeasureCategory.GENERAL.getText(resources), presenter.getCategory());
    }
}
