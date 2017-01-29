package hrv.band.app.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.view.HRVValueActivity;
import hrv.band.app.view.MainActivity;
import hrv.band.app.view.StatisticActivity;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.adapter.StatisticValueAdapter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

import static hrv.band.app.view.StatisticActivity.RESULT_DELETED;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment allowing user to start measurement.
 */
public class StatisticFragment extends Fragment {

    /** Key value for the hrv type of this fragment. **/
    private static final String ARG_SECTION_VALUE = "sectionValue";
    /** Key value for the parameters to show in this fragment. **/
    private static final String ARG_HRV_VALUE = "hrvValue";
    /** Key value for the date this fragment is showing. **/
    private static final String ARG_DATE_VALUE = "dateValue";
    /** The adapter holding the parameters to show. **/
    private StatisticValueAdapter adapter;
    /** The root view of this fragment. **/
    private View rootView;
    /** The chart showing the parameters in this fragment. **/
    private ColumnChartView mChart;
    /** The columns of mChart. **/
    private Column[] columns;
    private List<int[]> chartValuesIndex;
    /** The hrv type that this fragment is showing. **/
    private HRVValue hrvType;
    /** The parameters this fragment should show. **/
    private List<HRVParameters> parameters;
    /** The head line of listview showing actual chosen date. **/
    private TextView date;

    /**
     * Returns a new instance of this fragment.
     * @return a new instance of this fragment.
     */
    public static StatisticFragment newInstance(HRVValue type, List<HRVParameters> parameters,
                                                Date date) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SECTION_VALUE, type);
        args.putParcelableArrayList(ARG_HRV_VALUE, new ArrayList<>(parameters));
        args.putSerializable(ARG_DATE_VALUE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.statistic_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.stats_measure_history);

        parameters = getArguments().getParcelableArrayList(ARG_HRV_VALUE);
        hrvType = (HRVValue) getArguments().getSerializable(ARG_SECTION_VALUE);

        date = (TextView) rootView.findViewById(R.id.stats_date);
        TextView desc = (TextView) rootView.findViewById(R.id.stats_value_desc);
        TextView type = (TextView) rootView.findViewById(R.id.stats_type);

        date.setText(formatDate((Date) getArguments().getSerializable(ARG_DATE_VALUE)));

        desc.setText(hrvType.toString());
        type.setText(hrvType.getUnit());


        adapter = new StatisticValueAdapter(getActivity().getApplicationContext(),
                hrvType, parameters);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getContext(), HRVValueActivity.class);
                intent.putExtra(MainActivity.HRV_PARAMETER_ID, parameters.get(position));
                intent.putExtra(MainActivity.HRV_DATE, date.getText());
                startActivityForResult(intent, 1);
            }

        });

        mChart = (ColumnChartView) rootView.findViewById(R.id.stats_chart);
        initChart(parameters);

        return rootView;
    }

    /**
     * Formats the given date into the user locale.
     * @param date the date to format.
     * @return the formatted given date.
     */
    private String formatDate(Date date) {
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(getContext());
        return dateFormat.format(date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_DELETED){
            Activity root = getActivity();
            if (root instanceof StatisticActivity) {
                Date date = (Date) getArguments().getSerializable(ARG_DATE_VALUE);
                ((StatisticActivity) root).updateFragments(date);
            }
        }
    }


    /**
     * Initialized the chart and showing the given parameters.
     * @param parameters the parameters to show in the chart.
     */
    private void initChart(List<HRVParameters> parameters) {
        int numSubcolumns = 4;
        int numColumns = 24;

        chartValuesIndex = new ArrayList<>();
        columns = new Column[numColumns];

        for (int i = 0; i < numColumns; i++) {
            ArrayList<SubcolumnValue> subColumns = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; j++) {
                subColumns.add(new SubcolumnValue());
            }
            columns[i] = new Column(subColumns);
        }
        setChartValues(parameters);
        setAxis();
    }

    /**
     * Sets the properties of the X and Y axis of the chart.
     */
    private void setAxis() {
        ColumnChartData data = new ColumnChartData(new ArrayList<>(Arrays.asList(columns)));

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Hour");
        axisY.setName(hrvType.getUnit());
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        mChart.setZoomEnabled(false);
        mChart.setColumnChartData(data);
    }

    /**
     * Sets the values of the chart with the given parameters.
     * @param parameters the parameters to show in the chart.
     */
    private void setChartValues(List<HRVParameters> parameters) {
        resetChartValues();
        for (int i = 0; i < parameters.size(); i++) {
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(parameters.get(i).getTime());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE) / 15;

            columns[hour].getValues().set(minutes,
                    new SubcolumnValue((float) HRVValue.getHRVValue(hrvType, parameters.get(i)),
                            ContextCompat.getColor(getContext(), R.color.colorAccent)));
            chartValuesIndex.add(new int[] {hour, minutes});
            columns[hour].setHasLabels(false);
            columns[hour].setHasLabelsOnlyForSelected(false);
        }
        setAxis();
    }

    /**
     * Resets the chart value.
     */
    private void resetChartValues() {
        for (int i = 0; i < chartValuesIndex.size(); i++) {
            int[] tuple = chartValuesIndex.get(i);
            columns[tuple[0]].getValues().set(tuple[1], new SubcolumnValue());
        }
        chartValuesIndex = new ArrayList<>();
    }

    /**
     * Sets user chosen date of list view head.
     */
    private void setDate() {
        if (rootView == null) {
            return;
        }
        //TextView date = (TextView) rootView.findViewById(R.id.stats_date);
        date.setText(formatDate((Date) getArguments().getSerializable(ARG_DATE_VALUE)));
    }

    /**
     * Updates date und parameters.
     * @param parameters the new parameters.
     * @param date the new date.
     */
    public void updateValues(ArrayList<HRVParameters> parameters, Date date) {
        this.parameters = parameters;
        getArguments().putParcelableArrayList(ARG_HRV_VALUE, parameters);
        getArguments().putSerializable(ARG_DATE_VALUE, date);
        setDate();
        if (adapter != null) {
            adapter.setDataset(parameters);
            setChartValues(parameters);
        }
    }
}
