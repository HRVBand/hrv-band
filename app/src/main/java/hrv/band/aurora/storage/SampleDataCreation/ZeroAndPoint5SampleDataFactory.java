package hrv.band.aurora.storage.SampleDataCreation;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import hrv.band.aurora.Control.Calculation;
import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.Fourier.FastFourierTransform;
import hrv.band.aurora.Interpolation.CubicSplineInterpolation;
import hrv.band.aurora.RRInterval.Interval;

/**
 * Created by Julian on 25.06.2016.
 */
public class ZeroAndPoint5SampleDataFactory implements ISampleDataFactory {
    @Override
    public ArrayList<HRVParameters> create(int sampleSize) {
        CubicSplineInterpolation inter = new CubicSplineInterpolation();
        FastFourierTransform fft = new FastFourierTransform(4096);
        Calculation calc = new Calculation(fft, inter);

        ArrayList<HRVParameters> list = new ArrayList<HRVParameters>();
        for(int i = 0; i < sampleSize; i++)
        {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(1991, 7, 28 + i);
            Interval rr = new Interval(gregorianCalendar.getTime(), createIntervalData( i == 2 ));

            list.add(calc.Calculate(rr));
        }

        return list;
    }

    private double[] createIntervalData(Boolean zeros)
    {
        Random rand =  new Random();
        double[] rr = new double[100];

        if(zeros)
            return rr;

        for (int i = 0; i < 100; i++)
        {
            rr[i] = 0.5;
        }

        return rr;
    }
}
