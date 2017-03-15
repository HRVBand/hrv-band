package hrv.band.app.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import hrv.band.app.control.Measurement;
import hrv.band.app.R;
import hrv.band.app.view.HRVValueActivity;
import hrv.band.app.view.MainActivity;
import hrv.band.app.view.adapter.HRVValue;
import hrv.band.app.view.adapter.StatisticValueAdapter;
import hrv.band.app.view.chart.StatisticListener;
import lecho.lib.hellocharts.view.ColumnChartView;

import static hrv.band.app.view.StatisticActivity.RESULT_DELETED;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment allowing user to start measurement.
 */
public class StatisticFragment extends Fragment implements Observer {

    /** Key value for the hrv type of this fragment. **/
    private static final String ARG_SECTION_VALUE = "sectionValue";
    /** The adapter holding the parameters to show. **/
    private StatisticValueAdapter adapter;
    /** The root view of this fragment. **/
    private View rootView;
    /** The chart showing the parameters in this fragment. **/
    private ColumnChartView mChart;
    /** The hrv type that this fragment is showing. **/
    private HRVValue hrvType;
    /** The parameters this fragment should show. **/
    private List<Measurement> parameters;
    /** The head line of listview showing actual chosen date. **/
    private TextView date;

    private StatisticListener statisticListener;

    /**
     * Returns a new instance of this fragment.
     * @return a new instance of this fragment.
     */
    public static StatisticFragment newInstance(HRVValue type) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SECTION_VALUE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.statistic_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.stats_measure_history);

        parameters = statisticListener.getParameters();
        hrvType = (HRVValue) getArguments().getSerializable(ARG_SECTION_VALUE);

        date = (TextView) rootView.findViewById(R.id.stats_date);
        TextView desc = (TextView) rootView.findViewById(R.id.stats_value_desc);
        TextView type = (TextView) rootView.findViewById(R.id.stats_type);

        date.setText(formatDate(statisticListener.getDate()));

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

        statisticListener.drawChart(parameters, mChart, hrvType, getActivity());

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StatisticListener) {
            statisticListener = (StatisticListener) context;
        }
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
        if(resultCode == RESULT_DELETED){
            statisticListener.updateFragments(statisticListener.getDate());
        }
    }

    /**
     * Sets user chosen date of list view head.
     */
    private void setDate() {
        if (rootView == null) {
            return;
        }
        date.setText(formatDate(statisticListener.getDate()));
    }


    @Override
    public void update(Observable observable, Object o) {
        parameters = statisticListener.getParameters();
        if (adapter != null) {
            setDate();
            adapter.setDataset(parameters);
            adapter.notifyDataSetChanged();
            statisticListener.drawChart(parameters, mChart, hrvType, getActivity());
        }
    }
}
