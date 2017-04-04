package hrv.band.app.view.control.chart;

import android.content.res.Resources;
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
import hrv.band.app.control.CollectionUtils;
import hrv.band.app.control.IPredicate;
import hrv.band.app.control.Measurement;
import hrv.band.app.view.adapter.HRVValue;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import units.TimeUnit;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 07.03.2017
 *
 * Draws parameters into chart which were measured on that week.
 */
public class ChartDrawWeekStrategy extends AbstractChartDrawStrategy {

    private static final int COLUMN_COUNT = 7;
    private static final int SUB_COLUMN_COUNT = 0;
    private static final String X_AXIS_LABEL = "Day";

    private Date dateOfWeek;

    public ChartDrawWeekStrategy(Date dateOfWeek) {
        this.dateOfWeek = dateOfWeek;
    }

    @Override
    protected void setChartValues(List<Measurement> measurements, HRVValue hrvValueType) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        List<Measurement> measurementsOfWeek = filterDates(measurements);

        for (Measurement measurement : measurementsOfWeek) {
            calendar.setTime(measurement.getTime());

            //Minus 2 because get always returns 1 for sunday.
            int day = calendar.get(Calendar.DAY_OF_WEEK) - 2;
            //If day == sunday, make it the last day of the week.
            if(day == -1) {
                day = 6;
            }

            HRVLibFacade hrvCalc = new HRVLibFacade(RRData.createFromRRInterval(measurement.getRRIntervals(), TimeUnit.SECOND));
            hrvCalc.setParameters(EnumSet.of(hrvValueType.getHRVparam()));
            double value = hrvCalc.calculateParameters().get(0).getValue();

            columns[day].getValues().add(
                    new SubcolumnValue((float) value,
                            ContextCompat.getColor(context, R.color.colorAccent)));
            configColumnLabels(day);
        }
    }

    private List<Measurement> filterDates(List<Measurement> measurements) {
        return new ArrayList<>(CollectionUtils.filter(measurements, new IPredicate<Measurement>() {
            @Override
            public boolean apply(Measurement element) {
                return inSameWeek(element.getTime(), dateOfWeek);
            }
        }));
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
        Resources resources = context.getResources();
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

    private static boolean inSameWeek(Date date1, Date date2) {
        if (null == date1 || null == date2) {
            return false;
        }

        Calendar earlier = Calendar.getInstance();
        Calendar later = Calendar.getInstance();

        if (date1.before(date2)) {
            earlier.setTime(date1);
            later.setTime(date2);
        } else {
            earlier.setTime(date2);
            later.setTime(date1);
        }
        if (inSameYear(date1, date2)) {
            int week1 = earlier.get(Calendar.WEEK_OF_YEAR);
            int week2 = later.get(Calendar.WEEK_OF_YEAR);
            if (week1 == week2) {
                return true;
            }
        } else {
            int dayOfWeek = earlier.get(Calendar.DAY_OF_WEEK);
            earlier.add(Calendar.DATE, 7 - dayOfWeek);
            if (inSameYear(earlier.getTime(), later.getTime())) {
                int week1 = earlier.get(Calendar.WEEK_OF_YEAR);
                int week2 = later.get(Calendar.WEEK_OF_YEAR);
                if (week1 == week2) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean inSameYear(Date date1, Date date2) {
        if (null == date1 || null == date2) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        int year1 = cal1.get(Calendar.YEAR);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int year2 = cal2.get(Calendar.YEAR);
        return year1 == year2;

    }
}
