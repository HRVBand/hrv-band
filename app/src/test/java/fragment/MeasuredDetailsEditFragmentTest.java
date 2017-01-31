package fragment;

import android.os.Build;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Random;

import hrv.band.app.BuildConfig;
import hrv.band.app.R;
import hrv.band.app.view.adapter.MeasurementCategoryAdapter;
import hrv.band.app.view.fragment.MeasuredDetailsEditFragment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

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

        Random r = new Random();
        float rating = (int) (5 + (0 - 5) * r.nextDouble());

        ratingbar.setRating(rating);
        assertEquals(rating, fragment.getRating());
    }

    @Test
    public void checkCategory() throws Exception {
        Spinner spinner = (Spinner) fragment.getActivity().findViewById(R.id.measure_categories);

        int max = MeasurementCategoryAdapter.MeasureCategory.values().length;
        Random r = new Random();
        int categoryPosition = (int) (max + (0 - max) * r.nextDouble());

        spinner.setSelection(categoryPosition);
        assertEquals(MeasurementCategoryAdapter.MeasureCategory.values()[categoryPosition], fragment.getCategory());
    }

    @Test
    public void checkNote() throws Exception {
        String msg = "Hello World";
        TextView note = (TextView) fragment.getActivity().findViewById(R.id.measure_note);
        note.setText(msg);

        assertEquals(msg, fragment.getNote());
    }
}
