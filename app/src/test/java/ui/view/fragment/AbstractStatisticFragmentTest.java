package ui.view.fragment;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import hrv.band.app.BuildConfig;
import hrv.band.app.ui.view.fragment.OverviewFragment;
import hrv.calc.parameter.HRVParameterEnum;

import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 30.01.2017
 *
 * Tests for {@link OverviewFragment}.
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public abstract class AbstractStatisticFragmentTest {
    private OverviewFragment fragment;

    abstract HRVParameterEnum getHrvType();

    @Before
    public void setup()  {
        fragment = OverviewFragment.newInstance();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }
    @Test
    public void checkFragmentNotNull() throws Exception {
        assertNotNull(fragment);
    }

    public static class LFHFStatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVParameterEnum getHrvType() {
            return HRVParameterEnum.LFHF;
        }
    }

    public static class BaevskyStatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVParameterEnum getHrvType() {
            return HRVParameterEnum.BAEVSKY;
        }
    }

    public static class SDNNStatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVParameterEnum getHrvType() {
            return HRVParameterEnum.SDNN;
        }
    }

    public static class SD1StatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVParameterEnum getHrvType() {
            return HRVParameterEnum.SD1;
        }
    }

    public static class SD2StatisticTest extends AbstractStatisticFragmentTest {

        @Override
        public HRVParameterEnum getHrvType() {
            return HRVParameterEnum.SD2;
        }
    }

}
