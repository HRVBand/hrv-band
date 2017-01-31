package activity;

import android.content.Intent;
import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.lang.reflect.Field;

import hrv.band.app.BuildConfig;
import hrv.band.app.view.StatisticActivity;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.fragment.OverviewFragment;

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
    // ActivityController is a Robolectric class that drives the Activity lifecycle
    private ActivityController<StatisticActivity> controller;
    private StatisticActivity activity;

    public abstract HRVValue getHrvType();

    @Before
    public void setup()  {
        controller = Robolectric.buildActivity(StatisticActivity.class);
    }

    @Ignore
    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Ignore
    @Test
    public void createsAndDestroysActivity() {
        Intent intent = new Intent(RuntimeEnvironment.application, StatisticActivity.class);
        intent.putExtra(OverviewFragment.valueType, getHrvType());

        activity = controller
                .withIntent(intent)
                .create()
                .get();

        assertNotNull(activity);
    }

    @After
    public void tearDown() throws Exception {

        Field field = StatisticActivity.class.getDeclaredField("storage");
        field.setAccessible(true);
        field.set(null, null);

        // Destroy activity after every test
        controller
                .pause()
                .stop()
                .destroy();
    }

    @RunWith(Suite.class)
    @Suite.SuiteClasses({LFHFStatisticTest.class,
            BaevskyStatisticTest.class, SDNNStatisticTest.class, SD1StatisticTest.class,
            SD2StatisticTest.class,  RMSSDStatisticTest.class})

    public class StatisticAdapterTestSuite { }

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
