package activity;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import hrv.band.app.BuildConfig;
import hrv.band.app.ui.view.activity.SettingsActivity;

import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 30.01.2017
 *
 * Tests for {@link SettingsActivity}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class SettingsActivityTest {
    private SettingsActivity activity;

    @Before
    public void setup()  {
        activity = Robolectric.buildActivity(SettingsActivity.class)
                .create().get();
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }
}
