package inno.hacks.ms.band.Interpolation;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

/**
 * Created by Julian on 11.06.2016.
 */
public class CubicSplineInterpolation implements IInterpolation {

    public PolynomialSplineFunction calculate(double x[], double y[])
            throws DimensionMismatchException, NumberIsTooSmallException, NonMonotonicSequenceException
    {
        if (x.length != y.length) {
            throw new DimensionMismatchException(x.length, y.length);
        }

        if (x.length < 3) {
            throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS,
                    x.length, 3, true);
        }

        // Number of intervals.  The number of data points is n + 1.
        final int n = x.length - 1;

        //MathArrays.checkOrder(x);

        // Differences between knot points
        final double dx[] = new double[n];
        final double dy[] = new double[n];
        for (int i = 0; i < n; i++) {
            dx[i] = x[i + 1] - x[i];
            dy[i] = y[i + 1] - y[i];
        }

        final double f1[] = new double[n+1]; // F'(x[i])
        for (int i=1; i<n; i++) {
            double slope = dy[i-1]*dy[i];
            if (slope > 0d) {
                // doesn't change sign
                f1[i] = 2d / (dx[i]/dy[i] + dx[i-1]/dy[i-1]);
            } else if (slope <= 0d) {
                // changes sign
                f1[i] = 0d;
            }
        }
        f1[0] = 3d * dy[0] / (2d * (dx[0])) - f1[1]/2d;
        f1[n] = 3d * dy[n-1] / (2d * (dx[n-1])) - f1[n-1]/2d;

        // cubic spline coefficients -- a contains constants, b is linear, c quadratic, d is cubic
        final double a[] = new double[n+1];
        final double b[] = new double[n+1];
        final double c[] = new double[n+1];
        final double d[] = new double[n+1];

        for (int i = 1; i <= n; i++) {
            final double f2a = - 2d * (f1[i] + 2 * f1[i-1]) / dx[i-1] + 6d * dy[i-1] / (dx[i-1]*dx[i-1]);
            final double f2b = 2d * (2d * f1[i] + f1[i-1]) / dx[i-1] - 6d * dy[i-1] / (dx[i-1]*dx[i-1]);
            d[i] = (f2b - f2a) / (6d * dx[i-1]);
            c[i] = (x[i] * f2a - x[i-1] * f2b) / (2d * dx[i-1]);
            b[i] = (dy[i-1] -
                    c[i] * (x[i]*x[i] - x[i-1]*x[i-1]) -
                    d[i] * (x[i]*x[i]*x[i] - x[i-1]*x[i-1]*x[i-1])
            ) / dx[i-1];
            a[i] = y[i-1] - b[i]*x[i-1] - c[i]*x[i-1]*x[i-1] - d[i]*x[i-1]*x[i-1]*x[i-1];
        }

        final PolynomialFunction polynomials[] = new PolynomialFunction[n];
        final double coefficients[] = new double[4];
        for (int i = 1; i <= n; i++) {
            coefficients[0] = a[i];
            coefficients[1] = b[i];
            coefficients[2] = c[i];
            coefficients[3] = d[i];
            final double x0 = x[i-1];
            polynomials[i-1] = new PolynomialFunction(coefficients) {
                @Override
                public double value(double x) {
                    // bypass the standard Apache Commons behavior
                    return super.value(x + x0);
                }
            };
        }

        return new PolynomialSplineFunction(x, polynomials);
    }
}
