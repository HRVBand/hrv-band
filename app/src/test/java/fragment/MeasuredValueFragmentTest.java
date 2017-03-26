package fragment;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ListView;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Date;

import hrv.band.app.BuildConfig;
import hrv.band.app.control.Measurement;
import hrv.band.app.R;
import hrv.band.app.view.activity.WebActivity;
import hrv.band.app.view.fragment.MeasuredValueFragment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 31.01.2017
 *
 * Tests for {@link MeasuredValueFragment}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class MeasuredValueFragmentTest {

    private MeasuredValueFragment fragment;
    private static Measurement parameter;

    @BeforeClass
    public static void init() {
        Measurement.MeasurementBuilder builder = new Measurement.MeasurementBuilder(new Date(1000), new double[] {1,1,1,1,1});
        parameter = builder.build();
    }

    @Before
    public void setup()  {
        fragment = MeasuredValueFragment.newInstance(parameter);
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }

    @Test
    public void checkFragmentNotNull() throws Exception {
        assertNotNull(fragment);
    }

    @Test
    public void checkItemClicked() throws Exception {
        ListView listView = (ListView) fragment.getActivity().findViewById(R.id.hrv_value_list);
        for (int i = 0; i < listView.getCount(); i ++) {
            listView.performItemClick(
                    listView.getAdapter().getView(i, null, null),
                    i,
                    listView.getAdapter().getItemId(i));

            Intent expectedIntent = new Intent(fragment.getActivity(), WebActivity.class);
            checkExpectedIntent(expectedIntent);
        }
    }

    @Test
    public void checkItems() throws Exception {
        ListView listView = (ListView) fragment.getActivity().findViewById(R.id.hrv_value_list);
        for (int i = 0; i < listView.getCount(); i ++) {
            assertEquals(parameter, listView.getItemAtPosition(i));
        }
    }

    private void checkExpectedIntent(Intent expectedIntent) {
        ShadowActivity shadowActivity = Shadows.shadowOf(fragment.getActivity());
        Intent actualIntent = shadowActivity.getNextStartedActivity();

        assertTrue(actualIntent.filterEquals(expectedIntent));
    }
    @Test
    public void checkIfViewIsRecycled() throws Exception {
        ListView listView = (ListView) fragment.getActivity().findViewById(R.id.hrv_value_list);

        for (int index = 0; index < listView.getCount(); index++) {
            View view = getViewAtIndex(index, null, listView);
            View viewRecycled = getViewAtIndex(index, view, listView);
            assertSame(view, viewRecycled);
        }
    }

    private View getViewAtIndex(int index, View parent, ListView listView) {
        return listView.getAdapter().getView(index, parent, null);
    }
}
