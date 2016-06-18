package inno.hacks.ms.band.view;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import inno.hacks.ms.band.Control.Calculation;
import inno.hacks.ms.band.Control.HRVParameters;
import inno.hacks.ms.band.Fourier.FastFourierTransform;
import inno.hacks.ms.band.Interpolation.CubicSplineInterpolation;
import inno.hacks.ms.band.RRInterval.Interval;
import inno.hacks.ms.band.rrintervalExample.R;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //testData();
    }

    public void openSingleMeasure(View view) {
        Intent intent = new Intent(getApplicationContext(), MeasureActivity.class);
        startActivity(intent);
    }

    public void openContinuouslyMeasure(View view) {

    }

    public void showChart(View view) {
        Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
        startActivity(intent);
    }

    public void openChartView(View view) {
        Intent intent = new Intent(getApplicationContext(), ChartViewActivity.class);
        startActivity(intent);
    }

    public void calculation(View view) {
        Resources res = getResources();
        List<String[]> rrStrings = new ArrayList<>();
        rrStrings.add(res.getStringArray(R.array.rr0));
        rrStrings.add(res.getStringArray(R.array.rr1));
        rrStrings.add(res.getStringArray(R.array.rr2));
        rrStrings.add(res.getStringArray(R.array.rr3));
        rrStrings.add(res.getStringArray(R.array.rr4));
        rrStrings.add(res.getStringArray(R.array.rr5));
        rrStrings.add(res.getStringArray(R.array.rr6));
        rrStrings.add(res.getStringArray(R.array.rr7));
        rrStrings.add(res.getStringArray(R.array.rr8));
        rrStrings.add(res.getStringArray(R.array.rr9));

        List<HRVParameters> hrvParameters = new ArrayList<>();
        for (String[] stringArr : rrStrings) {
            double[] rr = new double[stringArr.length];
            for (int i = 0; i < stringArr.length; i++) {
                rr[i] = Double.valueOf(stringArr[i]);
            }
            hrvParameters.add((new Calculation(new FastFourierTransform(4096), new CubicSplineInterpolation())).Calculate(new Interval(new Date(), rr)));
        }
        /*new SharedPreferencesController().SaveData(getApplicationContext(), hrvParameters);*/
        Gson gson = new Gson();
        String json = gson.toJson(hrvParameters);
        int a= 0;

    }
}
