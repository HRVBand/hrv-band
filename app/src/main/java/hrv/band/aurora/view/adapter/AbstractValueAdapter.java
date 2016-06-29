package hrv.band.aurora.view.adapter;

import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

import hrv.band.aurora.Control.HRVParameters;

/**
 * Created by Thomas on 25.06.2016.
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

    public void setTextView(TextView view, String value) {
        view.setText(value);
    }
    public String trimValue(double value) {
        return new DecimalFormat("#.##").format(value);
    }

    public String getHRVValue(HRVValue value, HRVParameters parameter) {
        switch(value) {
            case LFHF: return trimValue(parameter.getLfhfRatio());
            case SDNN: return trimValue(parameter.getSdnn());
            case SD1: return trimValue(parameter.getSd1());
            case SD2: return trimValue(parameter.getSd2());
            case BAEVSKY: return trimValue(parameter.getBaevsky());
            case RMSSD: return trimValue(parameter.getRmssd());
        }
        return "";
    }


}
