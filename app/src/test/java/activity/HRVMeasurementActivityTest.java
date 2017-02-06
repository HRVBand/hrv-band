package activity;

import android.app.Dialog;
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
import org.robolectric.shadows.ShadowDialog;

import java.util.Date;

import hrv.band.app.BuildConfig;
import hrv.band.app.R;
import hrv.band.app.control.HRVParameters;
import hrv.band.app.storage.IStorage;
import hrv.band.app.storage.sqlite.HRVSQLController;
import hrv.band.app.view.HRVMeasurementActivity;
import hrv.band.app.view.MainActivity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 30.01.2017
 *
 * Tests for {@link MainActivity}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class HRVMeasurementActivityTest {
    private HRVMeasurementActivity activity;
    private static HRVParameters parameter;
    private static IStorage storage;

    @BeforeClass
    public static void init() {
        storage = new HRVSQLController();
        parameter = new HRVParameters(new Date(1000), 0, 0, 0, 0, 0, 0, 0, 0, new double[] {1,1,1,1,1});
    }

    @Before
    public void setup()  {
        Intent intent = new Intent(ShadowApplication.getInstance().getApplicationContext(), HRVMeasurementActivity.class);
        intent.putExtra(MainActivity.HRV_PARAMETER_ID, parameter);
        activity = Robolectric.buildActivity(HRVMeasurementActivity.class).withIntent(intent)
                .create().visible().get();
    }
    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void testOnBackPressed() throws Exception {
        activity.onBackPressed();
    }

    @Test
    public void onOptionsItemSelectedSave() {
        ActionMenuItemView item = (ActionMenuItemView) activity.findViewById(R.id.menu_ic_save);
        activity.onOptionsItemSelected(item.getItemData());

        assertEquals(parameter, storage.loadData(activity, new Date(1000)).get(0));
    }

    @After
    public void tearDown() {
        activity = null;
    }

    @AfterClass
    public static void afterClassTearDown() {
        storage = null;
        parameter = null;

    }

}
