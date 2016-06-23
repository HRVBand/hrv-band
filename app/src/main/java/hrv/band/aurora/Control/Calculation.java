package hrv.band.aurora.Control;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.Date;
import java.util.Arrays;
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

    public HRVParameters Calculate(Interval rrinterval)
    {
        double[] y = rrinterval.GetRRInterval();
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

        for(int i = 0; i < numInterpolVals; i++)
        {
            frequencies[i] = (frequencySteps/2) * i;
        }

        return createHRVParameter(y, frequencies, betrag);
    }

    private HRVParameters createHRVParameter(double[] y, double[] frequencies, double[] betrag) {
        double sdnn = SDNN(y);
        double sdsd = SDSD(y);
        double sd1 = SD1(sdnn);
        double sd2 = SD2(sdnn,sdsd);
        double lf = LfPow(frequencies, betrag, frequencies[1]);
        double hf = HfPow(frequencies, betrag, frequencies[1]);
        double rmssd = RMSSD(y);
        double baevsky = Baevsky(y);

        return new HRVParameters(new Date(), sdsd, sd1, sd2, lf, hf, rmssd, sdnn, baevsky, y);
    }

    private double Baevsky(double[] rrinterval)
    {
        double erwartungswert  = Erwartungswert(rrinterval);
        double min = min(rrinterval);
        double max = max(rrinterval);

        double baevsky = StatistischeHäufigkeit(rrinterval, erwartungswert, 0.05) / (2 * erwartungswert * (max - min));
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
        return Math.sqrt(2 * sdsd * sdsd - 0.5 * sdnnValue * sdnnValue);
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

        double hfPow = LinearIntegration(betrag, stepSize/2, firstElementOver015, firstElementOver04);
        return hfPow;
    }

    private double LfPow(double[] frequencies, double[] betrag, double stepSize)
    {
        int firstElementOver004 = FirstElementOverValue(frequencies, 0.02);
        int firstElementOver015 = FirstElementOverValue(frequencies, 0.15);

        double lfPow = LinearIntegration(betrag, stepSize/2, firstElementOver004, firstElementOver015);
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
    
    
    private double calcmedian(double[] numArray) {
        Arrays.sort(numArray);
        double middle = ((numArray.length) / 2);
        double median = 0;
        if (numArray.length % 2 == 0) {
            double medianA = numArray[(int) middle];
            double medianB = numArray[(int) middle - 1];
            median = (medianA + medianB) / 2;
        } else {
            median = numArray[(int) middle + 1];
        }
        return median;
    }
}
