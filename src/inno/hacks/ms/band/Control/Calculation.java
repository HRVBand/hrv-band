package inno.hacks.ms.band.Control;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import inno.hacks.ms.band.Fourier.IFourierTransformation;
import inno.hacks.ms.band.Interpolation.IInterpolation;
import inno.hacks.ms.band.RRInterval.Interval;

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

    public void Calculate(Interval rrinterval)
    {
        double[] y = rrinterval.GetRRInterval();
        double[] x = new double[y.length];

        for(int i = 1; i < y.length; i++)
        {
            x[i] = x[i-1] + y[i];
        }

        PolynomialSplineFunction interpolFunction = interpolation.calculate(x,y);
        int numInterpolVals = 2048;
        double[] x1 = new double[numInterpolVals];
        double[] y1 = new double[numInterpolVals];

        double lastTimeVal = x[x.length - 1];
        double stepSize = lastTimeVal / numInterpolVals;

        for(int i = 0; i < numInterpolVals; i++)
        {
            x1[i] = stepSize * i;
            y1[i] = interpolFunction.value(x1[i]);
        }

        double[] complexPart = new double[numInterpolVals];
        double[] realPart = new double[numInterpolVals];
        for(int i = 0; i < numInterpolVals; i++)
            realPart[i] = y1[i];

        fourierTransf.calculate(realPart, complexPart);

        double[] betrag = new double[x1.length];
        for(int i = 0; i < x1.length; i++)
        {
            betrag[i] = realPart[i] * realPart[i] + complexPart[i] * complexPart[i];
        }

        //
        double maxAbtastFrequenz = 0.5 * (1 / stepSize);
        double frequencySteps = maxAbtastFrequenz / (numInterpolVals - 1);
        double[] frequencies = new double[numInterpolVals];

        for(int i = 0; i < numInterpolVals; i++)
        {
            frequencies[i] = frequencySteps * i;
        }


        double hfPow = HfPow(frequencies, betrag, stepSize);
        double lfPow = LfPow(frequencies, betrag, stepSize);

        double lfhfRatio = lfPow / hfPow;
        double a = lfhfRatio;
    }

    public double HfPow(double[] frequencies, double[] betrag, double stepSize)
    {
        int firstElementOver015 = FirstElementOverValue(frequencies, 0.15);
        int firstElementOver04 = FirstElementOverValue(frequencies, 0.4);

        double hfPow = LinearIntegration(betrag, stepSize, firstElementOver015, firstElementOver04);
        return hfPow;
    }

    public double LfPow(double[] frequencies, double[] betrag, double stepSize)
    {
        int firstElementOver004 = FirstElementOverValue(frequencies, 0.04);
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

    double LinearIntegration(double[] values, double stepSize, int a, int b)
    {
        double integral = 0;
        for(int i = a; i <= b; i++)
            integral += values[i] * stepSize;

        return integral;
    }
}
