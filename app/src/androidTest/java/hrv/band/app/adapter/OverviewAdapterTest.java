package hrv.band.app.adapter;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.junit.BeforeClass;
import org.junit.ClassRule;

import hrv.band.app.R;
import hrv.band.app.view.MainActivity;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.adapter.OverviewValueAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 24.01.2017
 */


public class OverviewAdapterTest extends AbstractAdapterTest {
    private static OverviewValueAdapter overviewValueAdapter;
    private static HRVValue[] hrvValues;

    @ClassRule
    public static ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class);


    @BeforeClass
    public static void setUp() throws Exception {
        hrvValues = HRVValue.values();
        overviewValueAdapter = new OverviewValueAdapter(activityRule.getActivity());
    }

    @Override
    public BaseAdapter getAdapter() {
        return overviewValueAdapter;
    }

    @Override
    public int getSize() {
        return hrvValues.length;
    }

    @Override
    public Activity getActivity() {
        return activityRule.getActivity();
    }

    @Override
    public void checkViewElement(View view, int position) {
        TextView hrvNameTxt = (TextView) view.findViewById(R.id.firstLine);
        assertNotNull(hrvNameTxt);
        assertEquals(hrvValues[position].toString(), hrvNameTxt.getText());
    }

    @Override
    public Object getItemAtIndex(int index) {
        return hrvValues[index];
    }

    @Override
    public View getItemLayout() {
        return getActivity().findViewById(R.id.overview_value_list);
    }
}
