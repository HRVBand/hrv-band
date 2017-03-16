package hrv.band.app.control;

import java.util.EnumMap;
import java.util.List;

import hrv.calc.parameter.HRVParameter;
import hrv.calc.parameter.HRVParameterEnum;

/**
 * Copyright (c) 2017
 * Created by Julian on 16.03.2017.
 *
 * The Unit System in HRV-Lib is currently not optimal, therefore a adapter is needed.
 */
public class HRVParameterUnitAdapter {

    private final EnumMap<HRVParameterEnum, HRVParamAdaptionSettings> unitConversion;

    public HRVParameterUnitAdapter() {
        unitConversion = new EnumMap<>(HRVParameterEnum.class);
        unitConversion.put(HRVParameterEnum.BAEVSKY, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.AMPLITUDEMODE, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.AVG_SAMPLE_SIZE, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.HF, new HRVParamAdaptionSettings(1, "ms²"));
        unitConversion.put(HRVParameterEnum.LF, new HRVParamAdaptionSettings(1, "ms²"));
        unitConversion.put(HRVParameterEnum.LFHF, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.HFNU, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.LFNU, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.MEAN, new HRVParamAdaptionSettings(1, "s"));
        unitConversion.put(HRVParameterEnum.MODE, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.MXDMN, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.NN50, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.PNN50, new HRVParamAdaptionSettings(1, "%"));
        unitConversion.put(HRVParameterEnum.NON, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.RMSSD, new HRVParamAdaptionSettings(1, "ms"));
        unitConversion.put(HRVParameterEnum.SD1, new HRVParamAdaptionSettings(1000, "ms"));
        unitConversion.put(HRVParameterEnum.SD2, new HRVParamAdaptionSettings(1000, "ms"));
        unitConversion.put(HRVParameterEnum.SD1SD2, new HRVParamAdaptionSettings(1, ""));
        unitConversion.put(HRVParameterEnum.SDNN, new HRVParamAdaptionSettings(1000, "ms"));
        unitConversion.put(HRVParameterEnum.SDSD, new HRVParamAdaptionSettings(1, ""));
    }

    private HRVParamAdaptionSettings getAdapter(HRVParameterEnum param) {
        if(unitConversion.containsKey(param)){
            return unitConversion.get(param);
        }

        return unitConversion.get(HRVParameterEnum.NON);
    }

    public void adaptParameters(List<HRVParameter> params) {
        for(HRVParameter param : params) {
            HRVParamAdaptionSettings adapter = getAdapter(param.getType());
            param.setUnit(adapter.getUnit());
            param.setValue(param.getValue() * adapter.getConversionFactor());
        }
    }

    private class HRVParamAdaptionSettings {

        private String unit;
        private double conversionFactor;

        HRVParamAdaptionSettings(double conversionFactor, String unit) {
            this.unit = unit;
            this.conversionFactor = conversionFactor;
        }

        String getUnit() {
            return unit;
        }

        double getConversionFactor() {
            return conversionFactor;
        }
    }
}
