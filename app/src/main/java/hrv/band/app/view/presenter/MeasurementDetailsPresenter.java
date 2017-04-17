package hrv.band.app.view.presenter;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

import hrv.band.app.control.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */
public class MeasurementDetailsPresenter implements IMeasurementDetailsPresenter {

    private Measurement measurement;
    private Activity activity;

    public MeasurementDetailsPresenter(Measurement measurement, Activity activity) {
        this.measurement = measurement;
        this.activity = activity;
    }

    @Override
    public String getDate() {
        return formatDateTime(measurement != null ? measurement.getTime() : null);
    }

    @Override
    public String getRating() {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        return new DecimalFormat("#.#", symbols).format(measurement != null ? measurement.getRating() : 0).concat("/5");
    }

    @Override
    public String getNote() {
        return measurement != null ? measurement.getNote() : null;
    }

    @Override
    public String getCategory() {
        return measurement != null ? measurement.getCategory().getText(activity.getResources()) : null;
    }

    @Override
    public Drawable getCategoryIcon() {
        return measurement != null ? measurement.getCategory().getIcon(activity.getResources()) : null;
    }

    private String formatDateTime(Date date) {
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(activity);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(activity);
        return dateFormat.format(date) + ", " + timeFormat.format(date);
    }
}
