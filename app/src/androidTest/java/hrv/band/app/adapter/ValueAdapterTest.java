package hrv.band.app.adapter;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.junit.BeforeClass;
import org.junit.ClassRule;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.view.HRVMeasurementActivity;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.adapter.ValueAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 24.01.2017
 */


public class ValueAdapterTest extends AbstractAdapterTest {

    private static ValueAdapter valueAdapter;
    private static HRVParameters parameter;

    @ClassRule
    public static ActivityTestRule<HRVMeasurementActivity> activityRule = new ActivityTestRule<>(
            HRVMeasurementActivity.class);


    @BeforeClass
    public static void setUp() throws Exception {
        parameter = new HRVParameters(new Date(1000), 0, 0, 0, 0, 0, 0, 0, 0, new ArrayList<Double>());
        valueAdapter = new ValueAdapter(activityRule.getActivity(), parameter);
    }

    @Override
    public BaseAdapter getAdapter() {
        return valueAdapter;
    }

    @Override
    public int getSize() {
        return HRVValue.values().length;
    }

    @Override
    public Activity getActivity() {
        return activityRule.getActivity();
    }

    @Override
    public void checkViewElement(View view, int position) {
        TextView descText = (TextView) view.findViewById(R.id.measure_value_desc);
        TextView valueText = (TextView) view.findViewById(R.id.hrv_value);
        TextView unitText = (TextView) view.findViewById(R.id.measure_value_unit);

        assertNotNull(descText);
        assertNotNull(valueText);
        assertNotNull(unitText);

        assertEquals(HRVValue.values()[position].toString(), descText.getText());

        double value = HRVValue.getHRVValue(HRVValue.values()[position], parameter);
        assertEquals(new DecimalFormat("#.##").format(value), valueText.getText());

        assertEquals(HRVValue.values()[position].getUnit(), unitText.getText());

    }

    @Override
    public Object getItemAtIndex(int index) {
        return parameter;
    }

    @Override
    public View getItemLayout() {
        return getActivity().findViewById(R.id.hrv_value_list);
    }
}
