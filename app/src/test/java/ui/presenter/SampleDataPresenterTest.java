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

import hrv.band.app.ui.presenter.ISampleDataPresenter;
import hrv.band.app.ui.presenter.SampleDataPresenter;

/**
 * Copyright (c) 2017
 * Created by Thomas on 29.04.2017.
 */
@RunWith(RobolectricTestRunner.class)
public class SampleDataPresenterTest {
    private ISampleDataPresenter presenter;

    @Mock
    Activity activity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new SampleDataPresenter(RuntimeEnvironment.application);
        //storage = new HRVSQLController(RuntimeEnvironment.application);
    }

    @Test
    public void createSampleDataTest() {
        presenter.createSampleData();
        //assertNotNull(storage.loadData(new Date()));

    }

    @After
    public void tearDown() {
        //storage.closeDatabaseHelper();
    }
}
