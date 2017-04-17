package hrv.band.app.view.presenter;

import java.util.ArrayList;
import java.util.List;

import hrv.HRVLibFacade;
import hrv.RRData;
import hrv.band.app.control.HRVParameterSettings;
import hrv.band.app.control.HRVParameterUnitAdapter;
import hrv.band.app.control.Measurement;
import hrv.calc.parameter.HRVParameter;
import units.TimeUnit;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */

public class HRVParameterPresenter implements IHRVParameterPresenter {

    private Measurement measurement;
    private List<HRVParameter> params;

    public HRVParameterPresenter(Measurement measurement) {
        this.measurement = measurement;
        params = new ArrayList<>();
    }

    @Override
    public List<HRVParameter> getHRVParameters() {
        return params;
    }

    @Override
    public void calculateParameters() {
        params = calculateHRVParametersFromMeasurement();
        setUnitOfParameters();
    }

    private List<HRVParameter> calculateHRVParametersFromMeasurement() {
        HRVLibFacade hrvCalc = new HRVLibFacade(RRData.createFromRRInterval(measurement != null ?
                measurement.getRRIntervals() : new double[0], TimeUnit.SECOND));

        hrvCalc.setParameters(HRVParameterSettings.DefaultSettings.visibleHRVParameters);
        return hrvCalc.calculateParameters();
    }

    private void setUnitOfParameters() {
        HRVParameterUnitAdapter unitAdapter = new HRVParameterUnitAdapter();
        unitAdapter.adaptParameters(params);
    }

}
