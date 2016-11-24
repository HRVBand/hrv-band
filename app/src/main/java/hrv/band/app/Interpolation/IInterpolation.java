package hrv.band.app.Interpolation;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

/**
 * Created by Julian on 11.06.2016.
 */
public interface IInterpolation {
    PolynomialSplineFunction calculate(double[] x, double[] y);
}
