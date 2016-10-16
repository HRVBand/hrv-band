package hrv.band.aurora.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.view.MainActivity;
import hrv.band.aurora.view.ValueDescriptionActivity;
import hrv.band.aurora.view.adapter.AbstractValueAdapter;
import hrv.band.aurora.view.adapter.RRValueAdapter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Thomas on 10.08.2016.
 */
public class MeasureRRFragment extends Fragment {

    private ColumnChartView mChart;
    private HRVParameters parameter;


    public MeasureRRFragment() {
    }

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
        View rootView = inflater.inflate(R.layout.fragment_measure_rr, container, false);

        parameter = getArguments().getParcelable(MainActivity.HRV_VALUE);

        mChart = (ColumnChartView) rootView.findViewById(R.id.rr_chart);
        initChart();

        ListView listview = (ListView) rootView.findViewById(R.id.rr_value_list);

        AbstractValueAdapter adapter = new RRValueAdapter(this.getActivity(),
                R.layout.rr_value_item, parameter.getRRIntervals());
        listview.setAdapter(adapter);

       listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(getContext(), ValueDescriptionActivity.class);
                intent.putExtra(MainActivity.HRV_VALUE_ID, position);
                startActivity(intent);
            }

        });


        return rootView;
    }

    private ColumnChartData data;
    private void initChart() {
        //int numSubcolumns = 1;
        //int numColumns = 8;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for (int i = 0; i < parameter.getRRIntervals().size(); i++) {

            values = new ArrayList<>();
            values.add(new SubcolumnValue(parameter.getRRIntervals().get(i).floatValue(),
                    getResources().getColor(R.color.colorAccent)));

            Column column = new Column(values);
            //column.setHasLabels(hasLabels);
            // column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        data = new ColumnChartData(columns);

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
