package hrv.band.app.ui.view.activity.history.measurementstrategy;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.band.app.model.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 25.03.2017
 * <p>
 * Loads parameter from given week.
 */
public class ParameterLoadWeekStrategy extends AbstractParameterLoadStrategy {

    public ParameterLoadWeekStrategy(Context context) {
        super(context);
    }

    @Override
    public LiveData<List<Measurement>> loadParameter(Date date) {
        Calendar calendar = getCalenderFromDate(date);
        Date startDate = getStartOfTheWeek(calendar);
        Date endDate = getEndOfWeekDate(calendar);
        return database.measurementDao().loadData(startDate, endDate);
    }

    private Date getStartOfTheWeek(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    private Date getEndOfWeekDate(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        return calendar.getTime();
    }

    @NonNull
    private Calendar getCalenderFromDate(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}