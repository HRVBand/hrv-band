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
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.fragment.MeasuredDetailsFragment;

import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 31.01.2017
 *
 * Tests for {@link MeasuredDetailsFragment}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class MeasuredDetailsFragmentTest {

    private static Measurement parameter;
    private MeasuredDetailsFragment fragment;

    @BeforeClass
    public static void init() {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(1000), new double[] {1,1,1,1,1});
        parameter = builder.build();
    }

    @Before
    public void setup()  {
        fragment = MeasuredDetailsFragment.newInstance(parameter);
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }

    @Test
    public void checkFragmentNotNull() throws Exception {
        assertNotNull(fragment);
    }
}
