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
public class PointThreeAndPoint5RandomSampleDataFactory implements ISampleDataFactory {
    @Override
    public ArrayList<HRVParameters> create(int sampleSize) {
        CubicSplineInterpolation inter = new CubicSplineInterpolation();
        FastFourierTransform fft = new FastFourierTransform(4096);
        Calculation calc = new Calculation(fft, inter);

        ArrayList<HRVParameters> list = new ArrayList<HRVParameters>();
        for(int i = 0; i < sampleSize; i++)
        {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(1991, 7, 20 + i);
            Interval rr = new Interval(gregorianCalendar.getTime(), createIntervalData( i % 2 == 0 ));

            list.add(calc.Calculate(rr));
        }

        return list;
    }

    private double[] createIntervalData(Boolean zeros)
    {
        Random rand =  new Random();
        double[] rr = new double[100];

        double value = zeros ? 0.3 : 0.5;

        for (int i = 0; i < 100; i++)
        {
            rr[i] = value + rand.nextDouble() / 10;
        }

        return rr;
    }
}
