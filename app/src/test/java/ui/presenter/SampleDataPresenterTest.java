package ui.presenter;

import android.app.Activity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Date;

import hrv.band.app.model.storage.IStorage;
import hrv.band.app.model.storage.sqlite.HRVSQLController;
import hrv.band.app.ui.presenter.ISampleDataPresenter;
import hrv.band.app.ui.presenter.SampleDataPresenter;

import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas on 29.04.2017.
 */
@RunWith(RobolectricTestRunner.class)
public class SampleDataPresenterTest {
    private ISampleDataPresenter presenter;
    private IStorage storage;

    @Mock
    Activity activity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new SampleDataPresenter(activity);
        storage = new HRVSQLController(RuntimeEnvironment.application);
    }

    @Test
    public void createSampleDataTest() {
        presenter.createSampleData();
        assertNotNull(storage.loadData(new Date()));

    }

    @After
    public void tearDown() {
        storage.closeDatabaseHelper();
    }
}
