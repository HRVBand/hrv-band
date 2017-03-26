package hrv.band.app.view.control.chart;

import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.control.Measurement;
import hrv.band.app.view.adapter.HRVValue;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.SubcolumnValue;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 07.03.2017
 *
 * Draws parameters into chart which were measured in that month.
 */
public class ChartDrawMonthStrategy extends AbstractChartDrawStrategy {

    private static int columnCount;
    private static final int SUB_COLUMN_COUNT = 0;
    private static final String X_AXIS_LABEL = "Date";


    public ChartDrawMonthStrategy(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        columnCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void setChartValues(List<Measurement> parameters, HRVValue hrvValue) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        for (int i = 0; i < parameters.size(); i++) {
            calendar.setTime(parameters.get(i).getTime());
            int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;

            columns[day].getValues().add(
                    new SubcolumnValue((float) HRVValue.getHRVValue(hrvValue, parameters.get(i)),
                            ContextCompat.getColor(context, R.color.colorAccent)));
            configColumnLabels(day);
        }
    }

    @Override
    protected int getColumnCount() {
        return columnCount;
    }

    @Override
    protected int getSubColumnCount() {
        return SUB_COLUMN_COUNT;
    }

    @Override
    protected String getXAxisLabel() {
        return X_AXIS_LABEL;
    }

    @Override
    protected List<AxisValue> getXAxisValues() {
        List<AxisValue> values = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            AxisValue value = new AxisValue(i);
            if (i % 5 == 0) {
                value.setLabel(Integer.toString(i) + "");
                values.add(value);
            }
        }
        return values;
    }
}
