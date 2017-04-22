package hrv.band.app.ui.view.activity.history.chartstrategy;

import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.HRVLibFacade;
import hrv.RRData;
import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.adapter.HRVValue;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import units.TimeUnit;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 07.03.2017
 * <p>
 * Draws parameters into chart which were measured in that month.
 */
public class ChartDrawMonthStrategy extends AbstractChartDrawStrategy {

    private static final int SUB_COLUMN_COUNT = 0;
    private static final String X_AXIS_LABEL = "Date";
    private int columnCount;

    public ChartDrawMonthStrategy(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        columnCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void setChartValues(List<Measurement> measurements, HRVValue hrvValueType) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        for (Measurement measurement : measurements) {
            calendar.setTime(measurement.getTime());
            int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;

            HRVLibFacade hrvCalc = new HRVLibFacade(RRData.createFromRRInterval(measurement.getRRIntervals(), TimeUnit.SECOND));
            hrvCalc.setParameters(EnumSet.of(hrvValueType.getHRVparam()));
            double value = hrvCalc.calculateParameters().get(0).getValue();

            columns[day].getValues().add(
                    new SubcolumnValue((float) value,
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
