package hrv.band.app.control;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import hrv.RRData;
import hrv.calc.manipulator.HRVCleanRRDataByLimits;
import hrv.calc.manipulator.HRVCutToPowerTwoDataManipulator;
import hrv.calc.manipulator.HRVMultiDataManipulator;
import hrv.calc.manipulator.HRVSplineInterpolator;
import hrv.calc.manipulator.HRVSubstractMeanManipulator;
import hrv.calc.parameter.BaevskyCalculator;
import hrv.calc.parameter.HRVParameter;
import hrv.calc.parameter.HRVParameterEnum;
import hrv.calc.parameter.NN50Calculator;
import hrv.calc.parameter.PNN50Calculator;
import hrv.calc.parameter.RMSSDCalculator;
import hrv.calc.parameter.SD1Calculator;
import hrv.calc.parameter.SD1SD2Calculator;
import hrv.calc.parameter.SD2Calculator;
import hrv.calc.parameter.SDSDCalculator;
import hrv.calc.psd.PowerSpectrum;
import hrv.calc.psd.PowerSpectrumIntegralCalculator;
import hrv.calc.psd.StandardPowerSpectralDensityEstimator;

/**
 * Copyright (c) 2017
 * Created by Julian on 22.02.2017.
 */

public class HRVCalculatorFacade {

    private double lfLowerBound = 0.04;
    private double lfUpperBound = 0.15;
    private double hfLowerBound = 0.15;
    private double hfUpperBound = 0.4;

    private RRData data;
    private PowerSpectrum ps;

    public HRVCalculatorFacade(RRData data) {
        this.data = data;
    }

    public HRVParameter getLF() {
        PowerSpectrumIntegralCalculator calcLF = new PowerSpectrumIntegralCalculator(lfLowerBound, lfUpperBound);
        return new HRVParameter(HRVParameterEnum.LF, calcLF.process(getPowerSpectrum()).getValue() * 1000000, "ms * ms");
    }

    public HRVParameter getHF() {
        PowerSpectrumIntegralCalculator calcHF = new PowerSpectrumIntegralCalculator(hfLowerBound, hfUpperBound);
        return new HRVParameter(HRVParameterEnum.HF, calcHF.process(getPowerSpectrum()).getValue() * 1000000, "ms * ms");
    }

    public HRVParameter getMean() {
        Mean m = new Mean();
        return new HRVParameter(HRVParameterEnum.MEAN, m.evaluate(data.getValueAxis()), data.getValueAxisUnit().toString());
    }

    public HRVParameter getSDNN() {
        StandardDeviation d = new StandardDeviation();
        return new HRVParameter(HRVParameterEnum.SDNN, d.evaluate(data.getValueAxis()), "non");
    }

    public HRVParameter getBaevsky() {
        BaevskyCalculator baevskyCalc = new BaevskyCalculator();
        return baevskyCalc.process(data);
    }

    public HRVParameter getNN50() {
        NN50Calculator nn50Calc = new NN50Calculator();
        return nn50Calc.process(data);
    }

    public HRVParameter getPNN50() {
        PNN50Calculator pnn50Calc = new PNN50Calculator();
        return pnn50Calc.process(data);
    }

    public HRVParameter getRMSSD() {
        RMSSDCalculator rmssdCalc = new RMSSDCalculator();
        return rmssdCalc.process(data);
    }

    public HRVParameter getSD1() {
        SD1Calculator sd1Calc = new SD1Calculator();
        return sd1Calc.process(data);
    }

    public HRVParameter getSD2() {
        SD2Calculator sd2Calc = new SD2Calculator();
        return sd2Calc.process(data);
    }

    public HRVParameter getSD1SD2() {
        SD1SD2Calculator sd1sd2Calc = new SD1SD2Calculator();
        return sd1sd2Calc.process(data);
    }

    public HRVParameter getSDSD() {
        SDSDCalculator sdsdCalc = new SDSDCalculator();
        return sdsdCalc.process(data);
    }

    public PowerSpectrum getPowerSpectrum() {
        if(this.ps == null) {
            HRVMultiDataManipulator mani = new HRVMultiDataManipulator();
            mani.addManipulator(new HRVCleanRRDataByLimits());
            mani.addManipulator(new HRVSplineInterpolator(4));
            mani.addManipulator(new HRVCutToPowerTwoDataManipulator());
            mani.addManipulator(new HRVSubstractMeanManipulator());
            RRData manipulatedData = mani.manipulate(data);

            StandardPowerSpectralDensityEstimator estimator = new StandardPowerSpectralDensityEstimator();
            this.ps = estimator.calculateEstimate(manipulatedData);
        }

        return ps;
    }
}
