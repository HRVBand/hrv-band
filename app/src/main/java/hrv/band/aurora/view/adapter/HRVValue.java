package hrv.band.aurora.view.adapter;

import android.content.res.Resources;

import hrv.band.aurora.R;

/**
 * Created by Thomas on 29.06.2016.
 */
public enum HRVValue {
    LFHF("LFHF", "%", R.string.LFHF_desc),
    SDNN("SDNN", "ms", R.string.SDNN_desc),
    SD1("SD1", "ms", R.string.SD1_desc),
    SD2("SD2", "ms", R.string.SD2_desc),
    RMSSD("RMSSD", "ms", R.string.RMSSD_desc),
    BAEVSKY("Baevsky", "%", R.string.Baevsky_desc)
    ;

    private final String text;
    private final String unit;
    private final int descId;

    HRVValue(final String text, final String unit, final int descId) {
        this.text = text;
        this.unit = unit;
        this.descId = descId;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getUnit() {
        return unit;
    }

    public String getString(Resources resources) {
        return resources.getString(descId);
    }
}