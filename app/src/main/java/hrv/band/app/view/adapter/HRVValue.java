package hrv.band.app.view.adapter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This enum holds all available HRV values.
 */
public enum HRVValue {
    LFHF("LFHF", "%"),
    SDNN("SDNN", "ms"),
    SD1("SD1", "ms"),
    SD2("SD2", "ms"),
    RMSSD("RMSSD", "ms"),
    BAEVSKY("Baevsky", "%")
    ;

    /** Name of the value **/
    private final String text;
    /** Unit in which the value is described. **/
    private final String unit;

    HRVValue(final String text, final String unit) {
        this.text = text;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return text;
    }

    /**
     * Returns unit in which the value is described.
     * @return unit in which the value is described.
     */
    public String getUnit() {
        return unit;
    }

}