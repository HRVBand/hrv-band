package fragment;

import android.os.Build;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Date;

import hrv.band.app.BuildConfig;
import hrv.band.app.Control.HRVParameters;
import hrv.band.app.view.fragment.MeasuredRRFragment;

import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 31.01.2017
 *
 * Tests for {@link MeasuredRRFragment}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class MeasureRRFragmentTest {

    private MeasuredRRFragment fragment;
    private static HRVParameters parameter;

    @BeforeClass
    public static void init() {
        parameter = new HRVParameters(new Date(1000), 0, 0, 0, 0, 0, 0, 0, 0, new double[] {1,1,1,1,1});
    }

    @Before
    public void setup()  {
        fragment = MeasuredRRFragment.newInstance(parameter);
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }

    @Test
    public void checkFragmentNotNull() throws Exception {
        assertNotNull(fragment);
    }
}
