package org.vollib.j_lets_be_rational;

import static org.vollib.j_lets_be_rational.Constants.*;
import static org.vollib.j_lets_be_rational.ErfCody.erfc_cody;

import static java.lang.Math.sqrt;
import static java.lang.Math.abs;
import static java.lang.Math.log;
import static java.lang.Math.exp;

public class NormalDistribution {

    // The asymptotic expansion  Φ(z) = φ(z)/|z|·[1-1/z^2+...],  Abramowitz & Stegun (26.2.12), suffices for Φ(z) to have
    // relative accuracy of 1.64E-16 for z<=-10 with 17 terms inside the square brackets (not counting the leading 1).
    // This translates to a maximum of about 9 iterations below, which is competitive with a call to erfc() and never
    // less accurate when z<=-10. Note that, as mentioned in section 4 (and discussion of figures 2 and 3) of George
    // Marsaglia's article "Evaluating the Normal Distribution" (available at http://www.jstatsoft.org/v11/a05/paper),
    // for values of x approaching -8 and below, the error of any cumulative normal function is actually dominated by
    // the hardware (or compiler implementation) accuracy of exp(-x²/2) which is not reliably more than 14 digits when
    // x becomes large. Still, we should switch to the asymptotic only when it is beneficial to do so.
    private static final double norm_cdf_asymptotic_expansion_first_threshold = -10.0;
    private static final double norm_cdf_asymptotic_expansion_second_threshold = -1 / sqrt(DBL_EPSILON);

    //
    // ALGORITHM AS241  APPL. STATIST. (1988) VOL. 37, NO. 3
    //
    // Produces the normal deviate Z corresponding to a given lower
    // tail area of u; Z is accurate to about 1 part in 10**16.
    // see http://lib.stat.cmu.edu/apstat/241
    //
    private static final double SPLIT_1 = 0.425;
    private static final double SPLIT_2 = 5.0;
    private static final double FINAL_1 = 0.180625;
    private static final double FINAL_2 = 1.6;

    // Coefficients for P close to 0.5
    private static final double A0 = 3.3871328727963666080E0;
    private static final double A1 = 1.3314166789178437745E+2;
    private static final double A2 = 1.9715909503065514427E+3;
    private static final double A3 = 1.3731693765509461125E+4;
    private static final double A4 = 4.5921953931549871457E+4;
    private static final double A5 = 6.7265770927008700853E+4;
    private static final double A6 = 3.3430575583588128105E+4;
    private static final double A7 = 2.5090809287301226727E+3;
    private static final double B1 = 4.2313330701600911252E+1;
    private static final double B2 = 6.8718700749205790830E+2;
    private static final double B3 = 5.3941960214247511077E+3;
    private static final double B4 = 2.1213794301586595867E+4;
    private static final double B5 = 3.9307895800092710610E+4;
    private static final double B6 = 2.8729085735721942674E+4;
    private static final double B7 = 5.2264952788528545610E+3;
    // Coefficients for P not close to 0, 0.5 or 1.
    private static final double C0 = 1.42343711074968357734E0;
    private static final double C1 = 4.63033784615654529590E0;
    private static final double C2 = 5.76949722146069140550E0;
    private static final double C3 = 3.64784832476320460504E0;
    private static final double C4 = 1.27045825245236838258E0;
    private static final double C5 = 2.41780725177450611770E-1;
    private static final double C6 = 2.27238449892691845833E-2;
    private static final double C7 = 7.74545014278341407640E-4;
    private static final double D1 = 2.05319162663775882187E0;
    private static final double D2 = 1.67638483018380384940E0;
    private static final double D3 = 6.89767334985100004550E-1;
    private static final double D4 = 1.48103976427480074590E-1;
    private static final double D5 = 1.51986665636164571966E-2;
    private static final double D6 = 5.47593808499534494600E-4;
    private static final double D7 = 1.05075007164441684324E-9;
    // Coefficients for P very close to 0 or 1
    private static final double E0 = 6.65790464350110377720E0;
    private static final double E1 = 5.46378491116411436990E0;
    private static final double E2 = 1.78482653991729133580E0;
    private static final double E3 = 2.96560571828504891230E-1;
    private static final double E4 = 2.65321895265761230930E-2;
    private static final double E5 = 1.24266094738807843860E-3;
    private static final double E6 = 2.71155556874348757815E-5;
    private static final double E7 = 2.01033439929228813265E-7;
    private static final double F1 = 5.99832206555887937690E-1;
    private static final double F2 = 1.36929880922735805310E-1;
    private static final double F3 = 1.48753612908506148525E-2;
    private static final double F4 = 7.86869131145613259100E-4;
    private static final double F5 = 1.84631831751005468180E-5;
    private static final double F6 = 1.42151175831644588870E-7;
    private static final double F7 = 2.04426310338993978564E-15;

    public static double norm_cdf(double z) {
        if (z <= norm_cdf_asymptotic_expansion_first_threshold) {
            // Asymptotic expansion for very negative z following (26.2.12) on page 408
            // in M. Abramowitz and A. Stegun, Pocketbook of Mathematical Functions, ISBN 3-87144818-4.
            double sum = 1;
            if (z >= norm_cdf_asymptotic_expansion_second_threshold) {
                double zsqr = z * z, i = 1, g = 1, x, y, a = DBL_MAX, lasta;
                do {
                    lasta = a;
                    x = (4 * i - 3) / zsqr;
                    y = x * ((4 * i - 1) / zsqr);
                    a = g * (x - y);
                    sum -= a;
                    g *= y;
                    ++i;
                    a = abs(a);
                } while (lasta > a && a >= abs(sum * DBL_EPSILON));
            }
            return -norm_pdf(z) * sum / z;
        }
        return 0.5 * erfc_cody(-z * ONE_OVER_SQRT_TWO);
    }

    public static double inverse_norm_cdf(double u) {

        if (u <= 0)
            return log(u);
        if (u >= 1)
            return log(1 - u);

        final double q = u - 0.5;
        if (abs(q) <= SPLIT_1) {
            final double r = FINAL_1 - q * q;
            return q * (((((((A7 * r + A6) * r + A5) * r + A4) * r + A3) * r + A2) * r + A1) * r + A0) /
                    (((((((B7 * r + B6) * r + B5) * r + B4) * r + B3) * r + B2) * r + B1) * r + 1.0);
        } else {
            double r = q < 0.0 ? u : 1.0 - u;
            r = sqrt(-log(r));
            double ret;
            if (r < SPLIT_2) {
                r = r - FINAL_2;
                ret = (((((((C7 * r + C6) * r + C5) * r + C4) * r + C3) * r + C2) * r + C1) * r + C0) /
                        (((((((D7 * r + D6) * r + D5) * r + D4) * r + D3) * r + D2) * r + D1) * r + 1.0);
            } else {
                r = r - SPLIT_2;
                ret = (((((((E7 * r + E6) * r + E5) * r + E4) * r + E3) * r + E2) * r + E1) * r + E0) /
                        (((((((F7 * r + F6) * r + F5) * r + F4) * r + F3) * r + F2) * r + F1) * r + 1.0);
            }
            return q < 0.0 ? -ret : ret;
        }
    }

    public static double norm_pdf(double x) {
        return ONE_OVER_SQRT_TWO_PI * exp(-.5 * x * x);
    }


}
