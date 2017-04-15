package presenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import hrv.band.app.view.fragment.ISettingsView;
import hrv.band.app.view.presenter.ISettingsPresenter;
import hrv.band.app.view.presenter.SettingsPresenter;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Copyright (c) 2017
 * Created by Thomas on 15.04.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class SettingsPresenterTest {

    private ISettingsPresenter presenter;

    @Mock
    ISettingsView settingsView;

    @Before
    public void setup() {
        presenter = new SettingsPresenter(settingsView);
    }
    @Test
    public void isRecordTimeValid() {
        assertTrue(presenter.isRecordLengthValid("10"));
        assertTrue(presenter.isRecordLengthValid("90"));
        assertTrue(presenter.isRecordLengthValid("999999"));
        assertFalse(presenter.isRecordLengthValid(""));
        assertFalse(presenter.isRecordLengthValid("1"));
        assertFalse(presenter.isRecordLengthValid("1000000"));
    }
}
