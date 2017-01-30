package hrv.band.app.view.adapter;

import hrv.band.app.Control.HRVParameters;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This enum holds all available HRV values.
 */
public enum HRVValue {
    LFHF("LFHF", "%"),
    HF("HF", "s²"),
    LF("LF", "s²"),
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

    /**
     * Returns the value for the given HRV value from the given parameter.
     * @param value the HRV value to extract from parameter.
     * @param parameter to extract the value from.
     * @return the extracted value.
     */
    public static double getHRVValue(HRVValue value, HRVParameters parameter) {
        switch(value) {
            case LFHF: return parameter.getLfhfRatio();
            case SDNN: return parameter.getSdnn();
            case SD1: return parameter.getSd1();
            case SD2: return parameter.getSd2();
            case BAEVSKY: return parameter.getBaevsky();
            case RMSSD: return parameter.getRmssd();
            case LF: return parameter.getLf();
            case HF: return parameter.getHf();
        }
        return 0;
    }

}