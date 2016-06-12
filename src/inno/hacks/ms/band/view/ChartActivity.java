package inno.hacks.ms.band.view;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import inno.hacks.ms.band.Control.HRVParameters;
import inno.hacks.ms.band.Control.SharedPreferencesController;
import inno.hacks.ms.band.rrintervalExample.R;

public class ChartActivity extends Activity {

    private LineChart lineChart;
    private Spinner spinner;
    private SharedPreferencesController sharedPreferencesController;
    private String[] valueItems = {
            "sd1","sd2", "sd1sd2Ratio", "lf", "hf", "lfhfRatio", "rmssd", "sdnn", "baevsky"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        lineChart = (LineChart) findViewById(R.id.chart);
        //spinner = (Spinner) findViewById(R.id.chart_spinner);
        registerForContextMenu(lineChart);
        lineChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                (Toast.makeText(getApplicationContext(), "Down", Toast.LENGTH_LONG)).show();
                return true;
            }
        });
        sharedPreferencesController = new SharedPreferencesController();
        initChart();
    }

    private void setSpinnerValues() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valueItems); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }



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
        LineDataSet dataset = new LineDataSet(setDiagramStressValues(list), "# of Calls");
        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            Date time = list.get(i).getTime();
            labels.add(String.valueOf(time.getHours()) + ":" + String.valueOf(time.getMinutes()));
        }
        LineData data = new LineData(labels, dataset);
        lineChart.setData(data); // set the data and list of lables into chart
        dataset.setDrawFilled(true);
        lineChart.setDescription("Description");  // set the description
    }

    private List<String> setLabels(List<HRVParameters> list) {
        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            Date time = list.get(i).getTime();
            labels.add(String.valueOf(time.getHours()) + ":" + String.valueOf(time.getMinutes()));
        }
        return labels;
    }

    private List<Entry> setDiagramStressValues(List<HRVParameters> hrvParametersList) {
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < hrvParametersList.size(); i++) {
            entries.add(new Entry((float) hrvParametersList.get(i).getLfhfRatio(), i));
        }
        return entries;
    }

    private int getMinutes(int minutes, int hours) {
        return (hours * 60) + minutes;
    }
}
