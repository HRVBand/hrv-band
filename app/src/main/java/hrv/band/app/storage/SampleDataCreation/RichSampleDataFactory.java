package hrv.band.app.storage.SampleDataCreation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import hrv.band.app.Control.Calculation;
import hrv.band.app.Control.HRVParameters;
import hrv.band.app.Fourier.FastFourierTransform;
import hrv.band.app.Interpolation.CubicSplineInterpolation;
import hrv.band.app.RRInterval.Interval;

/**
 * Created by Julian on 01.07.2016.
 */
public class RichSampleDataFactory implements ISampleDataFactory {
    @Override
    public ArrayList<HRVParameters> create(int sampleSize) {

        CubicSplineInterpolation inter = new CubicSplineInterpolation();
        FastFourierTransform fft = new FastFourierTransform(4096);
        Calculation calc = new Calculation(fft, inter);

        ArrayList<HRVParameters> list = new ArrayList<HRVParameters>();
        for(int i = 0; i < sampleSize; i++)
        {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.add(Calendar.DATE, -i);

            Random rand = new Random();
            int entriesPerDay = rand.nextInt(5);
            for(int j = 0; j < entriesPerDay; j++)
            {
                Interval rr = new Interval(gregorianCalendar.getTime(), createIntervalData());
                list.add(calc.Calculate(rr));
            }
        }

        return list;
    }

    private double[] createIntervalData()
    {
        Random rand =  new Random();
        double[] rr = new double[100];
        for (int i = 0; i < 100; i++)
        {
            rr[i] = 0.45 + rand.nextDouble() / 10;
        }

        return rr;
    }
}
