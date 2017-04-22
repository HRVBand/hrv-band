package fragment;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import hrv.band.app.BuildConfig;
import hrv.band.app.ui.view.fragment.MeasuringFragment;

import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 31.01.2017
 *
 * Tests for {@link MeasuringFragment}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class MeasuringFragmentTest {
    private MeasuringFragment fragment;
    @Before
    public void setup()  {
        fragment = MeasuringFragment.newInstance();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }

    @Test
    public void checkFragmentNotNull() throws Exception {
        assertNotNull(fragment);
    }
}
