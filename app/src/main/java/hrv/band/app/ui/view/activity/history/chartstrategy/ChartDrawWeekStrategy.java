package hrv.band.app.ui.view.activity.history.chartstrategy;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.SubcolumnValue;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 07.03.2017
 * <p>
 * Draws parameters into chart which were measured on that week.
 */
public class ChartDrawWeekStrategy extends AbstractChartDrawStrategy {

    private static final int COLUMN_COUNT = 7;
    private static final int SUB_COLUMN_COUNT = 0;
    private static final String X_AXIS_LABEL = "Day";

    @Override
    protected void setChartValues(List<Measurement> measurements, HRVParameterEnum hrvValueType) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        for (Measurement measurement : measurements) {
            calendar.setTime(measurement.getDate());

            //Minus 2 because get always returns 1 for sunday.
            int day = calendar.get(Calendar.DAY_OF_WEEK) - 2;
            //If day == sunday, make it the last day of the week.
            if (day == -1) {
                day = 6;
            }

            double value = getValue(measurement, hrvValueType);
            if (value < 0) {
                continue;
            }

            columns[day].getValues().add(
                    new SubcolumnValue((float) value, getColor()));
            configColumnLabels(day);
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

    @Override
    protected List<AxisValue> getXAxisValues() {
        Resources resources = getResources();
        List<AxisValue> values = new ArrayList<>();
        AxisValue value = new AxisValue(0);
        values.add(value.setLabel(resources.getString(R.string.monday_sc)));
        value = new AxisValue(1);
        values.add(value.setLabel(resources.getString(R.string.tuesday_sc)));
        value = new AxisValue(2);
        values.add(value.setLabel(resources.getString(R.string.wednesday_sc)));
        value = new AxisValue(3);
        values.add(value.setLabel(resources.getString(R.string.thursday_sc)));
        value = new AxisValue(4);
        values.add(value.setLabel(resources.getString(R.string.friday_sc)));
        value = new AxisValue(5);
        values.add(value.setLabel(resources.getString(R.string.saturday_sc)));
        value = new AxisValue(6);
        values.add(value.setLabel(resources.getString(R.string.sunday_sc)));
        return values;
    }
}
