package hrv.band.app.ui.presenter;

import java.util.List;

import hrv.calc.parameter.HRVParameter;

/**
 * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */

public interface IHRVParameterPresenter {
    List<HRVParameter> getHRVParameters();
    void calculateParameters();
}
