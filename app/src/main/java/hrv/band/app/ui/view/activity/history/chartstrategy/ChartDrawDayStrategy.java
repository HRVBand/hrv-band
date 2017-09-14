package hrv.band.app.ui.view.activity.history.chartstrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.calc.parameter.HRVParameterEnum;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.SubcolumnValue;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 07.03.2017
 * <p>
 * Draws parameters into chart which were measured on that day.
 */
public class ChartDrawDayStrategy extends AbstractChartDrawStrategy {

    private static final int COLUMN_COUNT = 24;
    private static final int SUB_COLUMN_COUNT = 4;
    private static final String X_AXIS_LABEL = "Hour";

    @Override
    protected void setChartValues(List<Measurement> measurements, HRVParameterEnum hrvValueType) {
        Calendar calendar = GregorianCalendar.getInstance();
        for (Measurement measurement : measurements) {
            calendar.setTime(measurement.getDate());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE) / 15;

            double value = getValue(measurement, hrvValueType);
            if (value < 0) {
                continue;
            }

            columns[hour].getValues().set(minutes,
                    new SubcolumnValue((float) value, getColor()));
            configColumnLabels(hour);
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
        List<AxisValue> values = new ArrayList<>();
        for (int i = 0; i < COLUMN_COUNT; i++) {
            AxisValue value = new AxisValue(i);
            if (i % 4 == 0) {
                value.setLabel(i + ":00");
                values.add(value);
            }
        }
        return values;
    }

}
