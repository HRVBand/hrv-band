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
 * Loads parameter from given week.
 */
public class ParameterLoadWeekStrategy extends AbstractParameterLoadStrategy {

    public ParameterLoadWeekStrategy(Context context) {
        super(context);
    }

    @Override
    public List<Measurement> loadParameter(Date date) {
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        Date endDate = calendar.getTime();
        return storage.loadData(startDate, endDate);
    }
}