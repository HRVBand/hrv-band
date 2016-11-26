package hrv.band.app.view.adapter;

/**
 * Created by Thomas on 29.06.2016.
 */
public enum HRVValue {
    LFHF("LFHF", "%"),
    SDNN("SDNN", "ms"),
    SD1("SD1", "ms"),
    SD2("SD2", "ms"),
    RMSSD("RMSSD", "ms"),
    BAEVSKY("Baevsky", "%")
    ;

    private final String text;
    private final String unit;

    HRVValue(final String text, final String unit) {
        this.text = text;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getUnit() {
        return unit;
    }

}