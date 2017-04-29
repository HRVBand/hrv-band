package ui.view.fragment;

import android.os.Build;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import hrv.band.app.BuildConfig;
import hrv.band.app.R;
import hrv.band.app.ui.view.adapter.MeasurementCategoryAdapter;
import hrv.band.app.ui.view.fragment.MeasuredDetailsEditFragment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 31.01.2017
 *
 * Tests for {@link MeasuredDetailsEditFragment}
 */

@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class MeasuredDetailsEditFragmentTest {

    private MeasuredDetailsEditFragment fragment;

    @Before
    public void setup()  {
        fragment = MeasuredDetailsEditFragment.newInstance();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }

    @Test
    public void checkFragmentNotNull() throws Exception {
        assertNotNull(fragment);
    }

    @Test
    public void checkRating() throws Exception {
        RatingBar ratingbar = (RatingBar) fragment.getActivity().findViewById(R.id.measure_rating);

        for (int rating = 0; rating <= 5; rating++) {
            ratingbar.setRating(rating);
            assertEquals((float) rating, fragment.getRating());
        }
    }

    @Test
    public void checkCategory() throws Exception {
        Spinner spinner = (Spinner) fragment.getActivity().findViewById(R.id.measure_categories);

        int max = MeasurementCategoryAdapter.MeasureCategory.values().length;

        for (int position = 0; position < max; position++) {
            spinner.setSelection(position);
            assertEquals(MeasurementCategoryAdapter.MeasureCategory.values()[position], fragment.getCategory());
        }
    }

    @Test
    public void checkNote() throws Exception {
        String msg = "Hello World";
        TextView note = (TextView) fragment.getActivity().findViewById(R.id.measure_note);
        note.setText(msg);

        assertEquals(msg, fragment.getNote());
    }

    @Test
    public void checkIfViewIsRecycled() throws Exception {
        Spinner spinner = (Spinner) fragment.getActivity().findViewById(R.id.measure_categories);

        for (int index = 0; index < spinner.getCount(); index++) {
            View view = getViewAtIndex(index, null, spinner);
            View viewRecycled = getViewAtIndex(index, view, spinner);
            assertSame(view, viewRecycled);
        }
    }

    private View getViewAtIndex(int index, View parent, Spinner spinner) {
        return spinner.getAdapter().getView(index, parent, null);
    }
}
