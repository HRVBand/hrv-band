package hrv.band.app.view.control.parameter;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.band.app.control.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 25.03.2017
 *
 * Loads parameter from given week.
 */
public class ParameterLoadWeekStrategy extends AbstractParameterLoadStrategy {

    @Override
    public List<Measurement> loadParameter(Context context, Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        List<Measurement> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            result.addAll(storage.loadData(context, calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }
}