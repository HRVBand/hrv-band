package storage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.IStorage;
import hrv.band.app.model.storage.sqlite.HRVSQLController;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 27.04.2017
 */

@RunWith(RobolectricTestRunner.class)
public class HRVSQLControllerTest {
    private IStorage storage;
    private static Measurement measurement;
    private static List<Measurement> measurements;

    @BeforeClass
    public static void setupMeasurements() {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(), new double[]{1, 1, 1, 1, 1});
        measurement = builder.build();

        measurements = new ArrayList<>();
        measurements.add(measurement);
        measurements.add(measurement);
    }

    @Before
    public void setupDB() {
        storage = new HRVSQLController(RuntimeEnvironment.application);
    }

    @Test
    public void saveSingleMeasurementTest() {
        saveSingleMeasurement();
    }

    @Test
    public void saveListOfMeasurementsTest() {
        saveListOfMeasurements();
    }

    private void saveListOfMeasurements() {
        storage.saveData(measurements);
        assertEquals(2, storage.loadData(new Date()).size());
    }

    @Test
    public void saveEmptyListOfMeasurementsTest() {
        storage.saveData(new ArrayList<Measurement>());
        assertTrue(storage.loadData(new Date()).isEmpty());
    }

    @Test
    public void deleteMeasurementTest() {
        saveSingleMeasurement();
        storage.deleteData(measurement);
        assertTrue(storage.loadData(new Date()).isEmpty());
    }

    private void saveSingleMeasurement() {
        storage.saveData(measurement);
        assertEquals(1, storage.loadData(new Date()).size());
    }

    @Test
    public void deleteListOfMeasurementsTest() {
        saveListOfMeasurements();
        storage.deleteData(measurements);
        assertTrue(storage.loadData(new Date()).isEmpty());
    }

    @After
    public void tearDownDB() {
        storage.closeDatabaseHelper();
    }

    @AfterClass
    public static void tearDownMeasurements() {
        measurement = null;
        measurements = null;
    }
}
