package ui.view.fragment;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import hrv.band.app.BuildConfig;
import hrv.band.app.ui.view.fragment.SettingsFragment;

import static junit.framework.Assert.assertNotNull;
import static org.robolectric.util.FragmentTestUtil.startFragment;

/**
 * Copyright (c) 2017
 * Created by Julian on 08.02.2017.
 *
 * Tests for {@link SettingsFragmentTest}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class SettingsFragmentTest {
    private SettingsFragment fragment;

    @Before
    public void setup()  {
        fragment = new SettingsFragment();
        startFragment(fragment);
    }

    @Test
    public void checkFragmentNotNull() throws Exception {
        assertNotNull(fragment);
    }
}
