package hrv.band.app.model;

import java.util.EnumSet;
import java.util.Set;

import hrv.calc.parameter.HRVParameterEnum;

/**
 * Copyright (c) 2017
 * Created by Julian on 16.03.2017.
 */

public class HRVParameterSettings {

    public static final HRVParameterSettings DefaultSettings = new HRVParameterSettings(EnumSet.of(HRVParameterEnum.BAEVSKY,
            HRVParameterEnum.SD1,
            HRVParameterEnum.SD2,
            HRVParameterEnum.SD1SD2,
            HRVParameterEnum.SDNN,
            HRVParameterEnum.SDSD,
            HRVParameterEnum.PNN50,
            HRVParameterEnum.NN50,
            HRVParameterEnum.LF,
            HRVParameterEnum.HF,
            HRVParameterEnum.LFHF));
    public final Set<HRVParameterEnum> visibleHRVParameters;

    public HRVParameterSettings(Set<HRVParameterEnum> visibleHRVParameters) {
        this.visibleHRVParameters = visibleHRVParameters;
    }
}
