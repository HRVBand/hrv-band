package hrv.band.aurora.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.view.adapter.HRVValue;
import hrv.band.aurora.view.adapter.StatisticValueAdapter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Thomas on 27.06.2016.
 */
public class StatisticFragment extends Fragment {
    public static final String ARG_SECTION_VALUE = "sectionValue";
    public static final String ARG_HRV_VALUE = "hrvValue";
    public static final String ARG_DATE_VALUE = "dateValue";
    private StatisticValueAdapter adapter;
    private final String dateFormat = "dd.MMM yyyy";
    private View rootView;
    //protected BarChart mChart;
    private ColumnChartView mChart;

    public StatisticFragment() {
    }

    public static StatisticFragment newInstance(HRVValue type, ArrayList<HRVParameters> parameters,
                                                Date date) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SECTION_VALUE, type);
        args.putParcelableArrayList(ARG_HRV_VALUE, parameters);
        args.putSerializable(ARG_DATE_VALUE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_statistic_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.stats_measure_history);

        List<HRVParameters> parameters = getArguments().getParcelableArrayList(ARG_HRV_VALUE);
        HRVValue hrvType = (HRVValue) getArguments().getSerializable(ARG_SECTION_VALUE);

        TextView date = (TextView) rootView.findViewById(R.id.stats_date);
        TextView desc = (TextView) rootView.findViewById(R.id.stats_value_desc);
        TextView type = (TextView) rootView.findViewById(R.id.stats_type);

        DateFormat df = new DateFormat();
        date.setText(df.format(dateFormat,
                ((Date) getArguments().getSerializable(ARG_DATE_VALUE)).getTime()));

        desc.setText(hrvType.toString());
        type.setText(hrvType.getUnit());


        adapter = new StatisticValueAdapter(getActivity().getApplicationContext(),
                R.layout.statistic_value_item,
                hrvType, parameters);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                //Intent intent = new Intent(getContext(), ValueDescriptionActivity.class);
                //startActivity(intent);
            }

        });

        mChart = (ColumnChartView) rootView.findViewById(R.id.stats_chart);
        initHelloChartTest(parameters);

        return rootView;
    }



    private ColumnChartData data;

    private void initHelloChartTest(List<HRVParameters> parameters) {
        int numSubcolumns = 4;
        int numColumns = 24;

        Column[] columns = new Column[numColumns];

        for (int i = 0; i < numColumns; i++) {
            ArrayList<SubcolumnValue> subColumns = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; j++) {
                subColumns.add(new SubcolumnValue());
            }
            columns[i] = new Column(subColumns);
        }

        for (int i = 0; i < parameters.size(); i++) {
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(parameters.get(i).getTime());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE) / 15;

            columns[hour].getValues().set(minutes, new SubcolumnValue((float)parameters.get(i).getLfhfRatio(),
                    getResources().getColor(R.color.colorAccent)));

            columns[hour].setHasLabels(false);
            columns[hour].setHasLabelsOnlyForSelected(false);
        }

        data = new ColumnChartData(new ArrayList<>(Arrays.asList(columns)));

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("Hour");
        axisY.setName("%");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        mChart.setZoomEnabled(false);
        mChart.setColumnChartData(data);
    }

    private void setDate() {
        if (rootView == null) {
            return;
        }
        DateFormat df = new DateFormat();
        TextView date = (TextView) rootView.findViewById(R.id.stats_date);
        date.setText(df.format(dateFormat,
                ((Date) getArguments().getSerializable(ARG_DATE_VALUE)).getTime()));
    }

    public void updateValues(ArrayList<HRVParameters> parameters, Date date) {
        getArguments().putParcelableArrayList(ARG_HRV_VALUE, parameters);
        getArguments().putSerializable(ARG_DATE_VALUE, date);
        setDate();
        if (adapter != null) {
            adapter.setDataset(parameters);
        }
    }
}
