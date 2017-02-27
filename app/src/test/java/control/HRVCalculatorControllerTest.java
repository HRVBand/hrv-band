package control;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import hrv.RRData;
import hrv.band.app.BuildConfig;
import hrv.band.app.control.HRVCalculatorController;
import hrv.calc.frequency.psd.PowerSpectrum;
import hrv.calc.frequency.psd.StandardPowerSpectralDensityEstimator;
import units.TimeUnitConverter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Copyright (c) 2017
 * Created by Julian on 22.02.2017.
 */
@Config(constants = BuildConfig.class, sdk = {Build.VERSION_CODES.LOLLIPOP/*, Build.VERSION_CODES.KITKAT*/})
@RunWith(RobolectricTestRunner.class)
public class HRVCalculatorControllerTest {

    @Test
    public void testCalculation() {
        double sinHz = 1; //Frequency of the sin function
        int sampleFrequency = 8; //Sample Frequency in Hz
        double xLength = 2; //Length of the data.
        double[] sinY = generateSinArray(xLength, sampleFrequency, sinHz);

        //Generate X-Axis
        double[] sinX = new double[sinY.length];
        for(int i = 0; i < sinX.length; i++) {
            sinX[i] = i * (1.0 / sampleFrequency);
        }

        RRData rr = new RRData(sinX, TimeUnitConverter.TimeUnit.SECOND, sinY, TimeUnitConverter.TimeUnit.SECOND);

        HRVCalculatorController controller = new HRVCalculatorController(rr);
        assertNotNull(controller.getBaevsky());
        assertNotNull(controller.getHF());
        assertNotNull(controller.getLF());
        assertNotNull(controller.getMean());
        assertNotNull(controller.getNN50());
        assertNotNull(controller.getPNN50());
        assertNotNull(controller.getRMSSD());
        assertNotNull(controller.getSD1());
        assertNotNull(controller.getSD2());
        assertNotNull(controller.getSD1SD2());
        assertNotNull(controller.getSDNN());
        assertNotNull(controller.getSDSD());

        StandardPowerSpectralDensityEstimator estimator = new StandardPowerSpectralDensityEstimator();
        PowerSpectrum ps = estimator.calculateEstimate(rr);
        assertNotNull(ps);

        double[] freqResult = ps.getFrequency();
        double[] powerResult = ps.getPower();

        assertEquals(16, freqResult.length);
        assertEquals(0.0, freqResult[0], 0.0001);
        assertEquals(1.0, freqResult[2], 0.0001);

        assertEquals(16, powerResult.length);
        assertEquals(0.0, powerResult[0], 0.001);
        assertEquals(1.0, powerResult[2], 0.001);
        assertEquals(1.0, powerResult[14], 0.001);
    }

    private double[] generateSinArray(double xMax, int sampleFrequency, double sinHz) {
        double[] sin = new double[(int)xMax * sampleFrequency];

        for(int i = 0; i < sin.length; i++) {
            sin[i] = Math.sin(2 * Math.PI * sinHz * i * (1.0 / sampleFrequency));
        }

        return sin;
    }
}
