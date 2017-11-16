package hrv.band.app.ui.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

import hrv.band.app.model.Measurement;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */
public class MeasurementDetailsPresenter implements IMeasurementDetailsPresenter {

    private Measurement measurement;
    private Context context;
    //private Activity activity;

    public MeasurementDetailsPresenter(Measurement measurement, Context context) {
        this.measurement = measurement;
        this.context = context;
    }

    @Override
    public String getDate() {
        return formatDateTime(measurement != null ? measurement.getDate() : null);
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
        return measurement != null ? measurement.getCategory().getText(context.getResources()) : null;
    }

    @Override
    public Drawable getCategoryIcon() {
        return measurement != null ? measurement.getCategory().getIcon(context.getResources()) : null;
    }

    private String formatDateTime(Date date) {
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        return dateFormat.format(date) + ", " + timeFormat.format(date);
    }
}
