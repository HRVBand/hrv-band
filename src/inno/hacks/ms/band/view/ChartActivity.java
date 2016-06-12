package inno.hacks.ms.band.view;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import inno.hacks.ms.band.Control.HRVParameters;
import inno.hacks.ms.band.Control.SharedPreferencesController;
import inno.hacks.ms.band.rrintervalExample.R;

public class ChartActivity extends Activity {

    private LineChart lineChart;
    private Spinner spinner;
    private SharedPreferencesController sharedPreferencesController;
   /* private String[] valueItems = {
            "sd1","sd2", "sd1sd2Ratio", "lf", "hf", "lfhfRatio", "rmssd", "sdnn", "baevsky"
    };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        lineChart = (LineChart) findViewById(R.id.chart);
        //spinner = (Spinner) findViewById(R.id.chart_spinner);
        /*registerForContextMenu(lineChart);
        lineChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                (Toast.makeText(getApplicationContext(), "Down", Toast.LENGTH_LONG)).show();
                return true;
            }
        });*/
        sharedPreferencesController = new SharedPreferencesController();
        initChart();
    }

   /* private void setSpinnerValues() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valueItems); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }*/

    private void initChart() {
        if (lineChart == null) {
            return;
        }
        //View v = getView();
        // LineChart lineChart = (LineChart) view.findViewById(R.id.chart);
        // creating list of entry
        List<HRVParameters> list = sharedPreferencesController.LoadData(getApplicationContext());
        if (list == null) {
            return;

        }
        LineData data = new LineData(setLabels(list), setDataSets(list));
        lineChart.setData(data); // set the data and list of lables into chart

        data.setDrawValues(true);

        //lineChart.setDescription("Description");  // set the description
    }

    private List<ILineDataSet> setDataSets(List<HRVParameters> list) {
        List<ILineDataSet> dataSets = new ArrayList<>();

        LineDataSet lfhfSet = new LineDataSet(setDiagramLfhfValues(list), "lfhf");
        lfhfSet.setLineWidth(2f);
        lfhfSet.setColor(Color.parseColor("#ff00f0"));

        LineDataSet rmssdSet = new LineDataSet(setDiagramRmssdValues(list), "rmssd");
        rmssdSet.setLineWidth(2f);
        rmssdSet.setColor(Color.parseColor("#03a9f4"));

        LineDataSet sdnnSet = new LineDataSet(setDiagramSDNNValues(list), "sdnn");
        sdnnSet.setLineWidth(2f);
        sdnnSet.setColor(Color.parseColor("#f44336"));

        LineDataSet sd1Set = new LineDataSet(setDiagramSd1Values(list), "sd1");
        sd1Set.setLineWidth(2f);
        sd1Set.setColor(Color.parseColor("#009688"));

        LineDataSet sd2Set = new LineDataSet(setDiagramSd2Values(list), "sd2");
        sd2Set.setLineWidth(2f);
        sd2Set.setColor(Color.parseColor("#cddc39"));

        dataSets.add(lfhfSet);
        dataSets.add(rmssdSet);
        dataSets.add(sdnnSet);
        dataSets.add(sd1Set);
        dataSets.add(sd2Set);

        return dataSets;
    }

    private List<String> setLabels(List<HRVParameters> list) {
        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            Date time = list.get(i).getTime();
            labels.add(new SimpleDateFormat("HH:mm").format(time));
            //labels.add(String.valueOf(time.getHours()) + ":" + String.valueOf(time.getMinutes()));
        }
        return labels;
    }

    private List<Entry> setDiagramLfhfValues(List<HRVParameters> hrvParametersList) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < hrvParametersList.size(); i++) {
            entries.add(new Entry((float) hrvParametersList.get(i).getLfhfRatio(), i));
        }
        return entries;
    }
    private List<Entry> setDiagramRmssdValues(List<HRVParameters> hrvParametersList) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < hrvParametersList.size(); i++) {
            entries.add(new Entry((float) hrvParametersList.get(i).getRmssd(), i));
        }
        return entries;
    }
    private List<Entry> setDiagramSDNNValues(List<HRVParameters> hrvParametersList) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < hrvParametersList.size(); i++) {
            entries.add(new Entry((float) hrvParametersList.get(i).getSdnn(), i));
        }
        return entries;
    }
    private List<Entry> setDiagramSd1Values(List<HRVParameters> hrvParametersList) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < hrvParametersList.size(); i++) {
            entries.add(new Entry((float) hrvParametersList.get(i).getSd1(), i));
        }
        return entries;
    }

    private List<Entry> setDiagramSd2Values(List<HRVParameters> hrvParametersList) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < hrvParametersList.size(); i++) {
            entries.add(new Entry((float) hrvParametersList.get(i).getSd2(), i));
        }
        return entries;
    }
}
