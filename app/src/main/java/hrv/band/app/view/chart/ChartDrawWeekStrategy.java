package hrv.band.app.view.chart;

import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.control.Measurement;
import hrv.band.app.view.adapter.HRVValue;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 07.03.2017
 */


public class ChartDrawWeekStrategy extends AbstractChartDrawStrategy {

    private static final int COLUMN_COUNT = 7;
    private static final int SUB_COLUMN_COUNT = 24;
    private static final String X_AXIS_LABEL = "Day";

    @Override
    protected void setChartValues(List<Measurement> parameters, HRVValue hrvValue) {
        for (int i = 0; i < parameters.size(); i++) {
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(parameters.get(i).getTime());
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            columns[day].getValues().set(hour,
                    new SubcolumnValue((float) HRVValue.getHRVValue(hrvValue, parameters.get(i)),
                            ContextCompat.getColor(context, R.color.colorAccent)));
            columns[day].setHasLabels(false);
            columns[day].setHasLabelsOnlyForSelected(false);
        }
    }

    @Override
    protected int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    protected int getSubColumnCount() {
        return SUB_COLUMN_COUNT;
    }

    @Override
    protected String getXAxisLabel() {
        return X_AXIS_LABEL;
    }
}
