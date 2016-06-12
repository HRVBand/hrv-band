package inno.hacks.ms.band.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

import inno.hacks.ms.band.Control.HRVParameters;
import inno.hacks.ms.band.Control.SharedPreferencesController;
import inno.hacks.ms.band.rrintervalExample.BandRRIntervalAppActivity;
import inno.hacks.ms.band.rrintervalExample.R;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //testData();
    }

    public void openSingleMeasure(View view) {
        Intent intent = new Intent(getApplicationContext(), BandRRIntervalAppActivity.class);
        startActivity(intent);
    }

    public void openContinuouslyMeasure(View view) {

    }

    public void showChart(View view) {
        Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
        startActivity(intent);
    }
    private void testData() {
        ArrayList<HRVParameters> parameters = new ArrayList<>();
        for (int j = 0; j < 24; j++) {
            for (int i = 0; i < 4; i++) {
                HRVParameters p = new HRVParameters();
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, 2016);
                cal.set(Calendar.MONTH, 6);
                cal.set(Calendar.DATE, 11);
                cal.set(Calendar.HOUR_OF_DAY, j);
                cal.set(Calendar.MINUTE, i * 15);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                p.setTime(cal.getTime());
                p.setLfhfRatio(Math.random() * 10);
                Context c = getApplicationContext();
                (new SharedPreferencesController()).AddParams(c, p);
            }
        }

    }

}
