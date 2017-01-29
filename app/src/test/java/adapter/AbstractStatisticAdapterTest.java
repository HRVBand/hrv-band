package adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.fragment.StatisticFragment;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 25.01.2017
 */


public abstract class AbstractStatisticAdapterTest extends AbstractAdapterTest {

    private static List<HRVParameters> parameters;

    public abstract HRVValue getHrvType();

    @BeforeClass
    public static void setUpData() throws Exception {
        parameters = new ArrayList<>();
        parameters.add(new HRVParameters(new Date(1000), 0, 0, 0, 0, 0, 0, 0, 0, new ArrayList<Double>()));
        parameters.add(new HRVParameters(new Date(1000), 0, 0, 0, 0, 0, 0, 0, 0, new ArrayList<Double>()));
    }


    @Override
    public Fragment getFragment() {
        return StatisticFragment.newInstance(getHrvType(), parameters, new Date(1000));
    }

    @Override
    public ListView getListView() {
        return (ListView)fragment.getActivity().findViewById(R.id.stats_measure_history);
    }

    @Override
    public int getSize() {
        return parameters.size();
    }

    @Override
    public void checkViewElement(View view, int position) {
        TextView value = (TextView) view.findViewById(R.id.stats_value);
        TextView time = (TextView) view.findViewById(R.id.stats_time);
        TextView category = (TextView) view.findViewById(R.id.stats_category);

        assertNotNull(value);
        assertNotNull(time);
        assertNotNull(category);

        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(fragment.getActivity());
        assertEquals(dateFormat.format(parameters.get(position).getTime()), time.getText());

        assertEquals(parameters.get(position).getCategory().getText(fragment.getActivity().getResources()), category.getText());

        assertEquals(getValues(parameters, getHrvType()).get(position), value.getText());
    }

    @Override
    public Object getItemAtIndex(int index) {
        return parameters.get(index);
    }

    @Override
    public View getItemLayout() {
        return fragment.getActivity().findViewById(R.id.stats_measure_history);
    }

    /**
     * Returns a list containing the value of the given hrv type of a hrv parameter.
     * @param parameters the parameter to extract the value.
     * @param type indicates which value to extract from parameter.
     * @return a list containing the value of the given hrv type of a hrv parameter.
     */
    private List<String> getValues(List<HRVParameters> parameters, HRVValue type) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            double value = HRVValue.getHRVValue(type, parameters.get(i));
            values.add(new DecimalFormat("#.##").format(value));
        }
        return values;
    }

    @RunWith(Suite.class)
    @Suite.SuiteClasses({LFHFStatisticTest.class,
            BaevskyStatisticTest.class, SDNNStatisticTest.class, SD1StatisticTest.class,
            SD2StatisticTest.class,  RMSSDStatisticTest.class})

    public class StatisticAdapterTestSuite { }

    public static class LFHFStatisticTest extends AbstractStatisticAdapterTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.LFHF;
        }
    }

    public static class BaevskyStatisticTest extends AbstractStatisticAdapterTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.BAEVSKY;
        }
    }

    public static class SDNNStatisticTest extends AbstractStatisticAdapterTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.SDNN;
        }
    }

    public static class SD1StatisticTest extends AbstractStatisticAdapterTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.SD1;
        }
    }

    public static class SD2StatisticTest extends AbstractStatisticAdapterTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.SD2;
        }
    }

    public static class RMSSDStatisticTest extends AbstractStatisticAdapterTest {

        @Override
        public HRVValue getHrvType() {
            return HRVValue.RMSSD;
        }
    }
}
