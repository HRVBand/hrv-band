package hrv.band.app.view.adapter;

import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

import hrv.band.app.Control.HRVParameters;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Abstract adapter for displaying HRV values.
 */
public abstract class AbstractValueAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return HRVValue.values().length;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Sets the text of the given textView with the given value.
     * @param view the given textView to set text.
     * @param value the given text value.
     */
    void setTextView(TextView view, String value) {
        view.setText(value);
    }

    /**
     * Trim given value the value to fit the format #.##.
     * @param value the value to trim.
     * @return the trimmed value.
     */
    String trimValue(double value) {
        return new DecimalFormat("#.##").format(value);
    }

    /**
     * Returns the value for the given HRV value from the given parameter.
     * @param value the HRV value to extract from parameter.
     * @param parameter to extract the value from.
     * @return the extracted value.
     */
    public double getHRVValue(HRVValue value, HRVParameters parameter) {
        switch(value) {
            case LFHF: return parameter.getLfhfRatio();
            case SDNN: return parameter.getSdnn();
            case SD1: return parameter.getSd1();
            case SD2: return parameter.getSd2();
            case BAEVSKY: return parameter.getBaevsky();
            case RMSSD: return parameter.getRmssd();
        }
        return 0;
    }


}
