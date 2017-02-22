package activity;

import android.os.Build;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.BuildConfig;
import hrv.band.app.control.Measurement;
import hrv.band.app.view.StatisticActivity;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.fragment.StatisticFragment;

import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 30.01.2017
 *
 * Tests for {@link StatisticActivity}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public abstract class AbstractStatisticActivityTest {
    //TODO: https://github.com/robolectric/robolectric/issues/2010
    // ActivityController is a Robolectric class that drives the Activity lifecycle
    private StatisticActivity activity;
    private static List<Measurement> parameters;

    protected abstract HRVValue getHrvType();

    @Ignore
    @BeforeClass
    public static void init() {
        parameters = new ArrayList<>();
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(1000), new double[] {1,1,1,1,1});
        parameters.add(builder.build());
    }
    @Ignore
    @Before
    public void setup()  {
        activity = Robolectric.setupActivity(StatisticActivity.class);
    }
    @Ignore
    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }
    @Ignore
    @Test
    public void registerFragments() throws Exception {
        StatisticFragment fragment = StatisticFragment.newInstance(getHrvType(), parameters, new Date(1000));
        SupportFragmentTestUtil.startVisibleFragment(fragment);

    }





    @RunWith(Suite.class)
    @Suite.SuiteClasses({LFHFStatisticTest.class,
            BaevskyStatisticTest.class, SDNNStatisticTest.class, SD1StatisticTest.class,
            SD2StatisticTest.class,  RMSSDStatisticTest.class})
    private class StatisticAdapterTestSuite { }

    public static class LFHFStatisticTest extends AbstractStatisticActivityTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.LFHF;
        }
    }

    public static class BaevskyStatisticTest extends AbstractStatisticActivityTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.BAEVSKY;
        }
    }

    public static class SDNNStatisticTest extends AbstractStatisticActivityTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.SDNN;
        }
    }

    public static class SD1StatisticTest extends AbstractStatisticActivityTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.SD1;
        }
    }

    public static class SD2StatisticTest extends AbstractStatisticActivityTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.SD2;
        }
    }

    public static class RMSSDStatisticTest extends AbstractStatisticActivityTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.RMSSD;
        }
    }
}
