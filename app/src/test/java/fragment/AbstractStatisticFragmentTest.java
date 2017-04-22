package fragment;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import hrv.band.app.BuildConfig;
import hrv.band.app.ui.view.adapter.HRVValue;
import hrv.band.app.ui.view.fragment.HistoryFragment;

import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 30.01.2017
 *
 * Tests for {@link HistoryFragment}.
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public abstract class AbstractStatisticFragmentTest {
    private HistoryFragment fragment;

    abstract HRVValue getHrvType();

    @Before
    public void setup()  {
        fragment = HistoryFragment.newInstance(getHrvType());
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }
    @Test
    public void checkFragmentNotNull() throws Exception {
        assertNotNull(fragment);
    }

    public static class LFHFStatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.LFHF;
        }
    }

    public static class BaevskyStatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.BAEVSKY;
        }
    }

    public static class SDNNStatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.SDNN;
        }
    }

    public static class SD1StatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.SD1;
        }
    }

    public static class SD2StatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.SD2;
        }
    }

    public static class RMSSDStatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.RMSSD;
        }
    }
}
