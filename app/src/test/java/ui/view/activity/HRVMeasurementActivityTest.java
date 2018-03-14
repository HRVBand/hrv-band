package ui.view.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.view.menu.ActionMenuItemView;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.Date;

import hrv.band.app.BuildConfig;
import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.activity.MainActivity;

import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 30.01.2017
 * <p>
 * Tests for {@link MainActivity}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class HRVMeasurementActivityTest {
    private static Measurement parameter;
    //private SavableMeasurementActivity activity;

    @BeforeClass
    public static void init() {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(1000), new double[]{1, 1, 1, 1, 1});
        parameter = builder.build();
    }

    @AfterClass
    public static void afterClassTearDown() {
        parameter = null;

    }

    @Before
    public void setup() {
        /*Intent intent = new Intent(ShadowApplication.getInstance().getApplicationContext(), SavableMeasurementActivity.class);
        intent.putExtra(MainActivity.HRV_PARAMETER_ID, parameter);
        activity = Robolectric.buildActivity(SavableMeasurementActivity.class).withIntent(intent)
                .create().visible().get();*/
    }

    @Test
    public void checkActivityNotNull() throws Exception {
       // assertNotNull(activity);
    }

    @Test
    public void testOnBackPressed() throws Exception {
        //activity.onBackPressed();
    }

    @Test
    public void onOptionsItemSelectedSave() {
       /* ActionMenuItemView item = (ActionMenuItemView) activity.findViewById(R.id.menu_ic_save);
        activity.onOptionsItemSelected(item.getItemData());
        //storage = new HRVSQLController(activity);

        //assertEquals(parameter, storage.loadData(new Date(1000)).get(0));*/
    }

    @After
    public void tearDown() {
        //activity = null;
    }

}
