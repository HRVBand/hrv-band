package hrv.band.app.ui.view.activity.history.measurementstrategy;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.band.app.model.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 25.03.2017
 * <p>
 * Loads parameter of given month.
 */
public class ParameterLoadMonthStrategy extends AbstractParameterLoadStrategy {

    public ParameterLoadMonthStrategy(Context context) {
        super(context);
    }

    @Override
    public List<Measurement> loadParameter(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_MONTH, daysInMonth - 1);
        Date endDate = calendar.getTime();
        return storage.loadData(startDate, endDate);
    }
}