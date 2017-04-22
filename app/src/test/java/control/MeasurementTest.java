package control;

import android.os.Build;
import android.os.Parcel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import hrv.band.app.BuildConfig;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.adapter.MeasurementCategoryAdapter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 16.02.2017
 */


@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class MeasurementTest {

    private static Date date;
    private static double[] rr;
    private Measurement.MeasurementBuilder measurementBuilder;

    @BeforeClass
    public static void init() {
        date = new Date(1000);
        rr = new double[]{1,1,1,1};
    }

    @AfterClass
    public static void tearDown() {
        date = null;
        rr = null;
    }

    @Before
    public void setupBuilder() {
        measurementBuilder = new Measurement
                .MeasurementBuilder(date, rr);
    }

    @Test
    public void buildSimpleMeasurement() {
        Measurement parameter = measurementBuilder.build();
        assertNotNull(parameter);
        assertEquals(date, parameter.getTime());
        assertEquals(rr, parameter.getRRIntervals());
    }

    @Test
    public void buildSMeasurementWithRating() {
        double rating = 4.2;
        Measurement parameter = measurementBuilder.rating(rating).build();
        assertNotNull(parameter);
        assertEquals(rating, parameter.getRating());
    }

    @Test
    public void buildSMeasurementWithCategory() {
        MeasurementCategoryAdapter.MeasureCategory[] categories = MeasurementCategoryAdapter
                .MeasureCategory.values();

        for (MeasurementCategoryAdapter.MeasureCategory category : categories) {
            Measurement parameter = measurementBuilder.category(category).build();
            assertNotNull(parameter);
            assertEquals(category, parameter.getCategory());
        }

    }

    @Test
    public void parcelableTest() {
        Measurement parameter = measurementBuilder.build();
        Parcel parcel = Parcel.obtain();
        parameter.writeToParcel(parcel, parameter.describeContents());
        parcel.setDataPosition(0);

        Measurement createFromParcel = Measurement.CREATOR.createFromParcel(parcel);
        assertEquals(parameter.getTime(), createFromParcel.getTime());

        assertEquals(parameter.getRRIntervals().length, createFromParcel.getRRIntervals().length);

        int size = parameter.getRRIntervals().length;
        for (int i = 0; i < size; i++) {
            assertEquals(parameter.getRRIntervals()[i], createFromParcel.getRRIntervals()[i]);
        }
    }

    @Test
    public void buildSMeasurementWithNote() {
        String note = "this is a note";
        Measurement parameter = measurementBuilder.note(note).build();
        assertNotNull(parameter);
        assertEquals(note, parameter.getNote());
    }

    @After
    public void tearDownBuilder() {
        measurementBuilder = null;
    }
}
