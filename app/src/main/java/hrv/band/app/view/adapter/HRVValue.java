package hrv.band.app.view.adapter;

import hrv.calc.parameter.HRVParameterEnum;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This enum holds all available HRV values.
 */
public enum HRVValue {
    LFHF("LFHF", "%", HRVParameterEnum.LFHF),
    HF("HF", "ms²", HRVParameterEnum.HF),
    LF("LF", "ms²", HRVParameterEnum.LF),
    SDNN("SDNN", "ms", HRVParameterEnum.SDNN),
    SD1("SD1", "ms", HRVParameterEnum.SD1),
    SD2("SD2", "ms", HRVParameterEnum.SD2),
    RMSSD("RMSSD", "ms", HRVParameterEnum.RMSSD),
    BAEVSKY("Baevsky", "", HRVParameterEnum.BAEVSKY)
    ;

    /** Name of the value **/
    private final String text;
    /** Unit in which the value is described. **/
    private final String unit;

    private final HRVParameterEnum hrvParam;

    HRVValue(final String text, final String unit, final HRVParameterEnum hrvParam) {
        this.text = text;
        this.unit = unit;
        this.hrvParam = hrvParam;
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

    public HRVParameterEnum getHRVparam() { return hrvParam; }
}