package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.view.MainActivity;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment showing the rr intervals of a measurement.
 */
public class MeasureRRFragment extends Fragment {

    /** The chart showing rr intervals **/
    private ColumnChartView mChart;
    /** The hrv parameter to extract rr intervals from. **/
    private HRVParameters parameter;
    /** The root view of this fragment. **/
    private View rootView;

    /**
     * Returns a new instance of this fragment.
     * @param parameter the hrv parameter to get rr intervals from.
     * @return a new instance of this fragment.
     */
    public static MeasureRRFragment newInstance(HRVParameters parameter) {
        MeasureRRFragment fragment = new MeasureRRFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.HRV_VALUE, parameter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_measure_rr, container, false);

        parameter = getArguments().getParcelable(MainActivity.HRV_VALUE);

        mChart = (ColumnChartView) rootView.findViewById(R.id.rr_chart);
        initChart();
        setRRStatistic();

        return rootView;
    }

    /**
     * Calculates rr interval statistics and sets the text views.
     */
    private void setRRStatistic() {
        List<Double> rr = parameter.getRRIntervals();

        double average = 0;
        double min = Double.MAX_VALUE;
        double max = 0;
        for (int i = 0; i < rr.size(); i++) {
            average += rr.get(i);
            if (min > rr.get(i)) {
                min = rr.get(i);
            }
            if (max < rr.get(i)) {
                max = rr.get(i);
            }
        }
        average /= rr.size();

        TextView minTxt = (TextView) rootView.findViewById(R.id.rr_min);
        TextView maxTxt = (TextView) rootView.findViewById(R.id.rr_max);
        TextView averageTxt = (TextView) rootView.findViewById(R.id.rr_average);
        TextView countTxt = (TextView) rootView.findViewById(R.id.rr_count);

        minTxt.setText(trimValue(min));
        maxTxt.setText(trimValue(max));
        averageTxt.setText(trimValue(average));
        countTxt.setText(String.valueOf(rr.size()));
    }

    /**
     * Trims a single rr interval value into #.#### format.
     * @param value the rr value to trim
     * @return the trimmed rr interval value
     */
    private String trimValue(double value) {
        return new DecimalFormat("#.####").format(value);
    }

    /**
     * Initialized chart showing rr intervals from hrv measurement.
     */
    private void initChart() {
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for (int i = 0; i < parameter.getRRIntervals().size(); i++) {

            values = new ArrayList<>();
            values.add(new SubcolumnValue(parameter.getRRIntervals().get(i).floatValue(),
                    ContextCompat.getColor(getContext(), R.color.colorAccent)));

            Column column = new Column(values);
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("s");
        axisY.setName("ms");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        mChart.setZoomEnabled(false);
        mChart.setColumnChartData(data);
    }
}
