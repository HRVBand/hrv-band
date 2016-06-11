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

        double lastTimeVal = y[y.length - 1];
        double stepSize = lastTimeVal / numInterpolVals;

        for(int i = 0; i < numInterpolVals; i++)
        {
            x1[i] = stepSize * i;
            y1[i] = interpolFunction.value(x1[i]);
        }

    }
}
