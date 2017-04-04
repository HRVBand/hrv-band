package activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;

import hrv.band.app.BuildConfig;
import hrv.band.app.R;
import hrv.band.app.view.activity.ImprintActivity;
import hrv.band.app.view.activity.IntroActivity;
import hrv.band.app.view.activity.MainActivity;
import hrv.band.app.view.activity.SettingsActivity;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 30.01.2017
 *
 * Tests for {@link MainActivity}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;
    @Before
    public void setup()  {
        activity = Robolectric.buildActivity(MainActivity.class)
                .create().get();
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
    public void testOnBackPressedCloseDrawer() throws Exception {
        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);

        activity.onBackPressed();
    }

    @Test
    public void testOnBackPressedOnDisclaimer() throws Exception {
        activity.onBackPressed();
    }

    @Test
    public void shouldStartIntro() throws Exception {
        Intent expectedIntent = new Intent(activity, IntroActivity.class);
        checkExpectedIntent(R.id.menu_help, expectedIntent);
    }

    @Test
    public void shouldStartSettings() throws Exception {
        Intent expectedIntent = new Intent(activity, SettingsActivity.class);
        checkExpectedIntent(R.id.menu_Settings, expectedIntent);
    }

    @Test
    public void shouldStartImprint() throws Exception {
        Intent expectedIntent = new Intent(activity, ImprintActivity.class);
        checkExpectedIntent(R.id.menu_imprint, expectedIntent);
    }

    @Test
    public void shouldStartRate() throws Exception {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent expectedIntent = new Intent(Intent.ACTION_VIEW, uri);
        checkExpectedIntent(R.id.menu_rate, expectedIntent);
    }

    @Test
    public void shouldStartShare() throws Exception {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Intent expectedIntent = Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share_via));
        checkExpectedIntent(R.id.menu_share, expectedIntent);
    }

    private void checkExpectedIntent(int id, Intent expectedIntent) {
        MenuItem item = new RoboMenuItem(id);


        activity.onNavigationItemSelected(item);

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();

        assertTrue(actualIntent.filterEquals(expectedIntent));
    }
}
