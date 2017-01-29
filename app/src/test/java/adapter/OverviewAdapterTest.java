package adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.BeforeClass;

import hrv.band.app.R;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.fragment.OverviewFragment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 24.01.2017
 */


public class OverviewAdapterTest extends AbstractAdapterTest {
    private static HRVValue[] hrvValues;

    @BeforeClass
    public static void setUp() throws Exception {
        hrvValues = HRVValue.values();
    }

    @Override
    public Fragment getFragment() {
        return OverviewFragment.newInstance();
    }

    @Override
    public ListView getListView() {
        return (ListView)fragment.getActivity().findViewById(R.id.overview_value_list);
    }

    @Override
    public int getSize() {
        return hrvValues.length;
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
        return fragment.getActivity().findViewById(R.id.overview_value_list);
    }
}
