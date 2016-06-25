package hrv.band.aurora.Control;

import android.support.v4.content.res.TypedArrayUtils;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;
import java.util.List;

import hrv.band.aurora.Fourier.IFourierTransformation;
import hrv.band.aurora.Interpolation.IInterpolation;
import hrv.band.aurora.RRInterval.Interval;

/**
 * Created by Julian on 11.06.2016.
 */
public class Calculation {

    IFourierTransformation fourierTransf;
    IInterpolation interpolation;

    public Calculation(IFourierTransformation fTrans, IInterpolation interpol)
    {
        fourierTransf = fTrans;
        interpolation = interpol;
    }

    private double[] toPrimitveArray(List<Double> list)
    {
        double[] target = new double[list.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = list.get(i);
        }
        return target;
    }

    public HRVParameters Calculate(Interval rrinterval)
    {
        double[] unfiltered_y = rrinterval.GetRRInterval();
        double[] y= filter(unfiltered_y, calcmedian(unfiltered_y));
        double[] x = new double[y.length];

        for(int i = 1; i < y.length; i++)
        {
            x[i] = x[i-1] + y[i];
        }

        PolynomialSplineFunction interpolFunction = interpolation.calculate(x,y);
        int numInterpolVals = 4096;
        double[] xInterpolated = new double[numInterpolVals];
        double[] yInterpolated = new double[numInterpolVals];

        double lastTimeVal = x[x.length - 1];
        double stepSize = lastTimeVal / numInterpolVals;

        for(int i = 0; i < numInterpolVals; i++)
        {
            xInterpolated[i] = stepSize * i;
            yInterpolated[i] = interpolFunction.value(xInterpolated[i]);
        }

        double[] complexPart = new double[numInterpolVals];
        double[] realPart = new double[numInterpolVals];
        for(int i = 0; i < numInterpolVals; i++)
            realPart[i] = yInterpolated[i];

        fourierTransf.calculate(realPart, complexPart);

        double[] betrag = new double[xInterpolated.length];
        for(int i = 0; i < xInterpolated.length; i++)
        {
            betrag[i] = realPart[i] * realPart[i] + complexPart[i] * complexPart[i];
        }

        double maxAbtastFrequenz = 0.5 * (1 / stepSize);
        double frequencySteps = maxAbtastFrequenz / (numInterpolVals - 1);
        double[] frequencies = new double[numInterpolVals];

        double last = betrag[4095];
        double first = betrag[0];

        for(int i = 0; i < numInterpolVals; i++)
        {
            frequencies[i] = (frequencySteps) * i;
        }

        return createHRVParameter(y, frequencies, betrag);
    }

    private HRVParameters createHRVParameter(double[] y, double[] frequencies, double[] betrag) {
        double sdnn = SDNN(y) * 1000;
        double sdsd = SDSD(y) * 1000;
        double sd1 = SD1(sdnn);
        double sd2 = SD2(sdnn,sdsd);
        double lfpow1 = LfPow(frequencies, betrag, frequencies[1]);
        double hfpow1 = HfPow(frequencies, betrag, frequencies[1]);
        double lf=lfpow1/(lfpow1+hfpow1) * 100;
        double hf=hfpow1/(lfpow1+hfpow1) * 100;
        double rmssd = RMSSD(y) * 1000;
        double baevsky = Baevsky(y) * 100;

        return new HRVParameters(new Date(), sdsd, sd1, sd2, lf, hf, rmssd, sdnn, baevsky, y);
    }

    private double Baevsky(double[] rrinterval)
    {
        double mode  = calcmode(rrinterval);
        double min = min(rrinterval);
        double max = max(rrinterval);

        double baevsky = StatistischeHäufigkeit(rrinterval, mode, 0.05) / (2 * mode * (max - min));
        return baevsky;
    }

    private double StatistischeHäufigkeit(double[] rrinterval, double erwartungswert, double range)
    {
        int counter = 0;
        for(int i = 0; i < rrinterval.length; i++)
        {
            if(rrinterval[i] < erwartungswert + range && rrinterval[i] > erwartungswert - range)
                counter++;
        }
        return counter / (double) rrinterval.length;
    }

    private double min(double[] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            min = Math.min(min, array[i]);
        }
        return min;
    }

    private double max(double[] array) {
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        return max;
    }

    private double Erwartungswert(double[] values)
    {
        double sum = 0;
        for(int i = 0; i < values.length; i++)
        {
            sum += values[i];
        }

        double erwartungswert = sum / values.length;
        return erwartungswert;
    }

    private double SDNN(double[] rrinterval)
    {
        double erwartungswert = Erwartungswert(rrinterval);

        double sum2 = 0;
        for(int i = 0; i < rrinterval.length; i++)
        {
            sum2 += (rrinterval[i] - erwartungswert) * (rrinterval[i] - erwartungswert);
        }

        double sdnn = Math.sqrt(sum2 / rrinterval.length);
        return sdnn;
    }
    
    private double SDSD(double[] rrinterval)
    {
        double[] rrdiff = new double[rrinterval.length-1];
        
        for(int i = 0; i < rrinterval.length-1; i++)
        {
            rrdiff[i]= (rrinterval[i] - rrinterval[i+1]);
        }
        
        double erwartungswert = Erwartungswert(rrdiff);
        double sdsd = 0;
        for(int i = 0; i < rrdiff.length; i++)
        {
            sdsd += (rrdiff[i] - erwartungswert) * (rrdiff[i] - erwartungswert);
        }

        sdsd = Math.sqrt(sdsd / rrdiff.length);
        return sdsd;
    }
    
    
    

    private double SD1(double sdnnValue)
    {
        return Math.sqrt(0.5 * sdnnValue * sdnnValue);
    }

    private double SD2(double sdnnValue, double sdsd)
    {
        double val = 2 * sdsd * sdsd - 0.5 * sdnnValue * sdnnValue;
        return Math.sqrt(val);
    }

    private double SD1SD2(double sd1, double sd2)
    {
        return sd1 / sd2;
    }

    private double RMSSD(double[] rrinterval)
    {
        double sum = 0;
        for(int i = 1; i < rrinterval.length; i++)
        {
            sum += (rrinterval[i-1] - rrinterval[i]) * (rrinterval[i-1] - rrinterval[i]);
        }

        double rmssd = sum / (rrinterval.length - 1);
        return rmssd;
    }

    private double HfPow(double[] frequencies, double[] betrag, double stepSize)
    {
        int firstElementOver015 = FirstElementOverValue(frequencies, 0.15);
        int firstElementOver04 = FirstElementOverValue(frequencies, 0.4);

        double hfPow = LinearIntegration(betrag, stepSize, firstElementOver015, firstElementOver04);
        return hfPow;
    }

    private double LfPow(double[] frequencies, double[] betrag, double stepSize)
    {
        int firstElementOver004 = FirstElementOverValue(frequencies, 0.02);
        int firstElementOver015 = FirstElementOverValue(frequencies, 0.15);

        double lfPow = LinearIntegration(betrag, stepSize, firstElementOver004, firstElementOver015);
        return lfPow;
    }

    private int FirstElementOverValue(double[] values, double value)
    {
        for(int i = 0; i < values.length; i++)
        {
            if(values[i] > value)
                return i;
        }

        return values.length - 1;
    }

    private double LinearIntegration(double[] values, double stepSize, int a, int b)
    {
        double integral = 0;
        for(int i = a; i <= b; i++)
            integral += values[i] * stepSize;

        return integral;
    }
    
    private double[] filter(double[] rrint, double median)
    {
        ArrayList<Double> filteredrr = new ArrayList<>();
        for (int i = 0; i <rrint.length; i++)
        {
            if(!(rrint[i]/median<0.7 || rrint[i]/median>1.3 || (i < rrint.length - 1 && rrint[i] == rrint[i + 1])))
            {
                filteredrr.add(rrint[i]);
            }
        }

        return toPrimitveArray(filteredrr);
    }
    
    
    private double calcmedian(double[] numArray) {
        double[] newArr = Arrays.copyOf(numArray, numArray.length);
        Arrays.sort(newArr);
        double middle = ((newArr.length) / 2);
        double median = 0;
        if (newArr.length % 2 == 0) {
            double medianA = newArr[(int) middle];
            double medianB = newArr[(int) middle - 1];
            median = (medianA + medianB) / 2;
        } else {
            median = newArr[(int) middle + 1];
        }
        return median;
    }
    
    private double calcmode(double a[]) {
    double maxValue;
    int maxCount;

    for (int i = 0; i < a.length; ++i) {
        int count = 0;
        for (int j = 0; j < a.length; ++j) {
            if !((a[j]  > a[i] * 1.05 ) || (a[j]  < a[i] * 0.95)) ++count;
        }
        if (count > maxCount) {
            maxCount = count;
            maxValue = a[i];
        }
    }

    return maxValue;
    }
}
