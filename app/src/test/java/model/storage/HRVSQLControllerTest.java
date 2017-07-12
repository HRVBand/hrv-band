package model.storage;

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
import hrv.band.app.model.storage.sqlite.SQLiteStorageController;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 27.04.2017
 */
@RunWith(RobolectricTestRunner.class)
public class HRVSQLControllerTest {

    private SQLiteStorageController storageController;
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
        storageController = new SQLiteStorageController(RuntimeEnvironment.application);
        storageController.clearDatabaseAndRecreate();
        storage = new HRVSQLController(storageController);
    }

    @Test
    public void saveSingleMeasurementTest() {
        saveSingleMeasurement(measurement);
    }

    @Test
    public void saveMeasurementWithNullCategoryTest() {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(), new double[]{1, 1, 1, 1, 1});
        builder.category(null);
        saveSingleMeasurement(builder.build());
    }
    @Test
    public void saveMeasurementWithNullNoteTest() {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(), new double[]{1, 1, 1, 1, 1});
        builder.note(null);
        saveSingleMeasurement(builder.build());
    }

    @Test
    public void saveListOfMeasurementsTest() {
        saveListOfMeasurements();
    }

    private void saveListOfMeasurements() {
        storage.saveData(measurements);
        assertEquals(measurements.size(), storage.loadData(new Date()).size());
        assertEquals(measurements, storage.loadData(new Date()));
    }

    @Test
    public void saveEmptyListOfMeasurementsTest() {
        storage.saveData(new ArrayList<Measurement>());
        assertTrue(storage.loadData(new Date()).isEmpty());
    }

    @Test
    public void deleteMeasurementTest() {
        saveSingleMeasurement(measurement);
        storage.deleteData(measurement);
        assertTrue(storage.loadData(new Date()).isEmpty());
    }

    private void saveSingleMeasurement(Measurement measurement) {
        storage.saveData(measurement);
        assertEquals(1, storage.loadData(new Date()).size());
        assertEquals(measurement, storage.loadData(new Date()).get(0));
    }

    @Test
    public void deleteListOfMeasurementsTest() {
        saveListOfMeasurements();
        storage.deleteData(measurements);
        assertTrue(storage.loadData(new Date()).isEmpty());
    }

    @After
    public void tearDownDB() {
        storageController.clearDatabase();
    }

    @AfterClass
    public static void tearDownMeasurements() {
        measurement = null;
        measurements = null;
    }
}
