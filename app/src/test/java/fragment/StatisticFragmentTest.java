package fragment;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Date;

import hrv.band.app.BuildConfig;
import hrv.band.app.control.Measurement;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.fragment.StatisticFragment;

import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 30.01.2017
 *
 *
 * Tests for {@link StatisticFragment}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class StatisticFragmentTest {
    private StatisticFragment fragment;
    @Before
    public void setup()  {
        fragment = StatisticFragment.newInstance(HRVValue.BAEVSKY, new ArrayList<Measurement>(), new Date());
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }
    @Test
    public void checkFragmentNotNull() throws Exception {
        assertNotNull(fragment);
    }
}
