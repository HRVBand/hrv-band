package hrv.band.aurora.storage.SampleDataCreation;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import hrv.band.aurora.Control.Calculation;
import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.Fourier.FastFourierTransform;
import hrv.band.aurora.Interpolation.CubicSplineInterpolation;
import hrv.band.aurora.RRInterval.Interval;

/**
 * Created by Julian on 27.06.2016.
 */
public class StaticSampleDataFactory implements ISampleDataFactory {
    @Override
    public ArrayList<HRVParameters> create(int sampleSize) {

        CubicSplineInterpolation inter = new CubicSplineInterpolation();
        FastFourierTransform fft = new FastFourierTransform(4096);
        Calculation calc = new Calculation(fft, inter);

        ArrayList<HRVParameters> list = new ArrayList<HRVParameters>();
        for(int i = 0; i < sampleSize; i++)
        {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(1991 + i, 7, 28);
            Interval rr = new Interval(gregorianCalendar.getTime(), createIntervalData());

            list.add(calc.Calculate(rr));
        }

        return list;
    }

    double[] createIntervalData()
    {
        double[] rrdata = new double[10];
        for(int i = 0; i < rrdata.length; i++)
        {
            rrdata[i] = 0.5 + ((i % 2 == 0) ? 0.0 : 0.1);
        }
        return rrdata;
    }
}
