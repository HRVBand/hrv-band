package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hrv.band.app.control.Measurement;
import hrv.band.app.R;
import hrv.band.app.view.activity.MainActivity;
import hrv.band.app.view.presenter.IRRPresenter;
import hrv.band.app.view.presenter.MeasuredRRPresenter;
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
public class MeasuredRRFragment extends Fragment {

    /** The chart showing rr intervals **/
    private ColumnChartView mChart;

    private IRRPresenter presenter;

    /**
     * Returns a new instance of this fragment.
     * @param parameter the hrv parameter to get rr intervals from.
     * @return a new instance of this fragment.
     */
    public static MeasuredRRFragment newInstance(Measurement parameter) {
        MeasuredRRFragment fragment = new MeasuredRRFragment();
        Bundle args = new Bundle();
        args.putParcelable(MainActivity.HRV_VALUE, parameter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.abstract_hrv_fragment_rr, container, false);

        presenter = new MeasuredRRPresenter((Measurement) getArguments().getParcelable(MainActivity.HRV_VALUE));
        presenter.calculateRRStatistic();

        mChart = (ColumnChartView) rootView.findViewById(R.id.rr_chart);

        if (presenter.getMeasurement() != null) {
            initRRChart();
            setRRStatistic(rootView);
        }

        return rootView;
    }

    private void setRRStatistic(View rootView) {

        TextView minTxt = (TextView) rootView.findViewById(R.id.rr_min);
        TextView maxTxt = (TextView) rootView.findViewById(R.id.rr_max);
        TextView averageTxt = (TextView) rootView.findViewById(R.id.rr_average);
        TextView countTxt = (TextView) rootView.findViewById(R.id.rr_count);

        minTxt.setText(presenter.getRRMin());
        maxTxt.setText(presenter.getRRMax());
        averageTxt.setText(presenter.getRRAverage());
        countTxt.setText(presenter.getRRCount());
    }

    /**
     * Initialized chart showing rr intervals from hrv measurement.
     */
    private void initRRChart() {
        List<Column> columns = createChartData();
        setChartData(columns);
    }

    private List<Column> createChartData() {
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        double[] rrIntervals = presenter.getMeasurement().getRRIntervals();
        for (double rrInterval : rrIntervals) {

            values = new ArrayList<>();
            values.add(new SubcolumnValue((float) rrInterval,
                    ContextCompat.getColor(getContext(), R.color.colorAccent)));

            Column column = new Column(values);
            columns.add(column);
        }
        return columns;
    }

    private void setChartData(List<Column> columns) {
        ColumnChartData data = new ColumnChartData(columns);

        setChartAxis(data);

        mChart.setZoomEnabled(false);
        mChart.setColumnChartData(data);
    }

    private void setChartAxis(ColumnChartData data) {
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("s");
        axisY.setName("s");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
    }
}
