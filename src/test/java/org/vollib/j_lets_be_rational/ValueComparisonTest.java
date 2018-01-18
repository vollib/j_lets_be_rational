package org.vollib.j_lets_be_rational;

import org.junit.Test;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Maricris on 07/02/2017.
 */
public class ValueComparisonTest {

    PyLetsBeRational pyLetsBeRational = new PyLetsBeRational();

    private double TestCases = 5000;

    @Test
    public void test_black() {

        double q = 1;  // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();
            double expected = pyLetsBeRational.black(F, K, sigma, T, q);
            double actual = LetsBeRational.black(F, K, sigma, T, q);
            assertEquals(expected, actual, Constants.DBL_EPSILON);
        }
        q = -1;  // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();
            double expected = pyLetsBeRational.black(F, K, sigma, T, q);
            double actual = LetsBeRational.black(F, K, sigma, T, q);
            assertEquals(expected, actual, Constants.DBL_EPSILON);
        }

    }

    @Test
    public void test_implied_volatility_from_a_transformed_rational_guess() {

        double q = 1;  // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();
            double price = pyLetsBeRational.black(F, K, sigma, T, q);
            double expected = pyLetsBeRational.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q);
            price = LetsBeRational.black(F, K, sigma, T, q);


            StringBuilder sb = new StringBuilder();
            sb.append("F = ").append(F).append("\n");
            sb.append("K = ").append(K).append("\n");
            sb.append("T = ").append(T).append("\n");
            sb.append("q = ").append(q).append("\n");
            sb.append("price = ").append(price).append("\n");
            sb.append("implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q)").append("\n");

            try {
                double actual = LetsBeRational.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q);
                assertEquals(sb.toString(), expected, actual, Constants.DBL_EPSILON);

            } catch (VolatilityValueException e) {
//                sb.append(e.getMessage()).append("\n");
//                System.out.println(sb.toString());
                assertVolatilityValueException(e, expected);
            }

        }
        q = -1;  // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();
            double price = pyLetsBeRational.black(F, K, sigma, T, q);
            double expected = pyLetsBeRational.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q);
            price = LetsBeRational.black(F, K, sigma, T, q);

            StringBuilder sb = new StringBuilder();
            sb.append("F = ").append(F).append("\n");
            sb.append("K = ").append(K).append("\n");
            sb.append("T = ").append(T).append("\n");
            sb.append("q = ").append(q).append("\n");
            sb.append("price = ").append(price).append("\n");
            sb.append("implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q)").append("\n");

            try {
                double actual = LetsBeRational.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q);

                assertEquals(sb.toString(), expected, actual, Constants.DBL_EPSILON);
            } catch (VolatilityValueException e) {
//                sb.append(e.getMessage()).append("\n");
//                System.out.println(sb.toString());
                assertVolatilityValueException(e, expected);
            }
        }

    }

    private void assertVolatilityValueException(VolatilityValueException exception, double expected) {
        if (exception instanceof VolatilityValueException.BelowIntrinsicException || exception instanceof VolatilityValueException.AboveMaximumException) {
            assertEquals(exception.getMessage(), expected, exception.getValue(), Constants.DBL_EPSILON);
        } else {
            fail("Unhandled exception.");
        }
    }

    @Test
    public void test_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations() {

        double q = 1;  // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();
            int N = getRandomN();
            double price = pyLetsBeRational.black(F, K, sigma, T, q);
            double expected = pyLetsBeRational.implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(price, F, K, T, q, N);
            price = LetsBeRational.black(F, K, sigma, T, q);

            StringBuilder sb = new StringBuilder();
            sb.append("F = ").append(F).append("\n");
            sb.append("K = ").append(K).append("\n");
            sb.append("sigma = ").append(sigma).append("\n");
            sb.append("T = ").append(T).append("\n");
            sb.append("N = ").append(N).append("\n");
            sb.append("q = ").append(q).append("\n");
            sb.append("price = ").append(price).append("\n");
            sb.append("implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(price, F, K, T, q, N)").append("\n");

            try {
                double actual = LetsBeRational.implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(price, F, K, T, q, N);
                assertEquals(expected, actual, Constants.DBL_EPSILON);
            } catch (VolatilityValueException e) {
//                sb.append(e.getMessage()).append("\n");
//                System.out.println(sb.toString());
                assertVolatilityValueException(e, expected);
            }
        }
        q = -1;  // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();
            int N = getRandomN();
            double price = pyLetsBeRational.black(F, K, sigma, T, q);
            double expected = pyLetsBeRational.implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(price, F, K, T, q, N);
            price = LetsBeRational.black(F, K, sigma, T, q);

            StringBuilder sb = new StringBuilder();
            sb.append("F = ").append(F).append("\n");
            sb.append("K = ").append(K).append("\n");
            sb.append("sigma = ").append(sigma).append("\n");
            sb.append("T = ").append(T).append("\n");
            sb.append("N = ").append(N).append("\n");
            sb.append("q = ").append(q).append("\n");
            sb.append("price = ").append(price).append("\n");
            sb.append("implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(price, F, K, T, q, N)").append("\n");

            try {
                double actual = LetsBeRational.implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(price, F, K, T, q, N);
                assertEquals(expected, actual, Constants.DBL_EPSILON);
            } catch (VolatilityValueException e) {
//                sb.append(e.getMessage()).append("\n");
//                System.out.println(sb.toString());
                assertVolatilityValueException(e, expected);
            }
        }

    }

    @Test
    public void test_normalised_black() {

        double q = 1; // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();

            double x = Math.log(F / K);
            double s = sigma * Math.sqrt(T);

            double expected = pyLetsBeRational.normalised_black(x, s, q);
            double actual = LetsBeRational.normalised_black(x, s, q);
            assertEquals(expected, actual, Constants.DBL_EPSILON);
        }
        q = -1; // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();

            double x = Math.log(F / K);
            double s = sigma * Math.sqrt(T);

            double expected = pyLetsBeRational.normalised_black(x, s, q);
            double actual = LetsBeRational.normalised_black(x, s, q);
            assertEquals(expected, actual, Constants.DBL_EPSILON);
        }

    }

    @Test
    public void test_normalised_black_call() {

        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();

            double x = Math.log(F / K);
            double s = sigma * Math.sqrt(T);

            double expected = pyLetsBeRational.normalised_black_call(x, s);
            double actual = LetsBeRational.normalised_black_call(x, s);
            assertEquals(expected, actual, Constants.DBL_EPSILON);
        }

    }

    @Test
    public void test_normalised_implied_volatility_from_a_transformed_rational_guess() {

        double q = 1; // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();

            double x = Math.log(F / K);
            double s = sigma * Math.sqrt(T);

            double beta = pyLetsBeRational.normalised_black(x, s, q);
            double expected = pyLetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q);
            beta = LetsBeRational.normalised_black(x, s, q);

            StringBuilder sb = new StringBuilder();
            sb.append("x = ").append(x).append("\n");
            sb.append("q = ").append(q).append("\n");
            sb.append("beta = ").append(beta).append("\n");
            sb.append("normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q)").append("\n");

            try {
                double actual = LetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q);
                assertEquals(expected, actual, Constants.DBL_EPSILON);
            } catch (VolatilityValueException e) {
//                sb.append(e.getMessage()).append("\n");
//                System.out.println(sb.toString());
                assertVolatilityValueException(e, expected);
            }
        }
        q = -1; // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();

            double x = Math.log(F / K);
            double s = sigma * Math.sqrt(T);

            double beta = pyLetsBeRational.normalised_black(x, s, q);
            double expected = pyLetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q);
            beta = LetsBeRational.normalised_black(x, s, q);

            StringBuilder sb = new StringBuilder();
            sb.append("x = ").append(x).append("\n");
            sb.append("q = ").append(q).append("\n");
            sb.append("beta = ").append(beta).append("\n");
            sb.append("normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q)").append("\n");

            try {
                double actual = LetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q);
                assertEquals(expected, actual, Constants.DBL_EPSILON);
            } catch (VolatilityValueException e) {
//                sb.append(e.getMessage()).append("\n");
//                System.out.println(sb.toString());
                assertVolatilityValueException(e, expected);
            }
        }

    }

    @Test
    public void test_normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations() {

        double q = 1; // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();
            int N = getRandomN();

            double x = Math.log(F / K);
            double s = sigma * Math.sqrt(T);

            double beta = pyLetsBeRational.normalised_black(x, s, q);
            double expected = pyLetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(beta, x, q, N);
            beta = LetsBeRational.normalised_black(x, s, q);

            StringBuilder sb = new StringBuilder();
            sb.append("N = ").append(N).append("\n");
            sb.append("x = ").append(x).append("\n");
            sb.append("q = ").append(q).append("\n");
            sb.append("beta = ").append(beta).append("\n");
            sb.append("normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(beta, x, q, N)").append("\n");

            try {
                double actual = LetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(beta, x, q, N);
                assertEquals(expected, actual, Constants.DBL_EPSILON);
            } catch (VolatilityValueException e) {
//                sb.append(e.getMessage()).append("\n");
//                System.out.println(sb.toString());
                assertVolatilityValueException(e, expected);
            }
        }
        q = -1; // CALL = 1 PUT = -1
        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();
            int N = getRandomN();

            double x = Math.log(F / K);
            double s = sigma * Math.sqrt(T);

            double beta = pyLetsBeRational.normalised_black(x, s, q);
            double expected = pyLetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(beta, x, q, N);
            beta = LetsBeRational.normalised_black(x, s, q);

            StringBuilder sb = new StringBuilder();
            sb.append("N = ").append(N).append("\n");
            sb.append("x = ").append(x).append("\n");
            sb.append("q = ").append(q).append("\n");
            sb.append("beta = ").append(beta).append("\n");
            sb.append("normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(beta, x, q, N)").append("\n");

            try {
                double actual = LetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(beta, x, q, N);
                assertEquals(expected, actual, Constants.DBL_EPSILON);
            } catch (VolatilityValueException e) {
//                sb.append(e.getMessage()).append("\n");
//                System.out.println(sb.toString());
                assertVolatilityValueException(e, expected);
            }
        }

    }

    @Test
    public void test_normalised_vega() {

        for (int i = 0; i < TestCases; i++) {
            double F = getRandomF();
            double K = getRandomK();
            double sigma = getRandomSigma();
            double T = getRandomT();

            double x = Math.log(F / K);
            double s = sigma * Math.sqrt(T);

            double expected = pyLetsBeRational.normalised_vega(x, s);
            double actual = LetsBeRational.normalised_vega(x, s);
            assertEquals(expected, actual, Constants.DBL_EPSILON);
        }

    }

    @Test
    public void test_norm_cdf() {

        for (int i = 0; i < TestCases; i++) {
            double z = getRandomz();
            double expected = pyLetsBeRational.norm_cdf(z);
            double actual = NormalDistribution.norm_cdf(z);
            assertEquals(expected, actual, Constants.DBL_EPSILON);
        }
    }

    @Test
    public void test_below_volatility_exceptions() {
        double F = 100.0;
        double K = 100.0;
        double T = 0.5;
        double q = 1;

        double price = -1.0;
        try {
            // below intrinsic
                // intrinsic = abs(max(q < 0 ? K-F : F-K, 0.0))
            LetsBeRational.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q);
            fail("BelowInstrinsicException should be thrown.");
        } catch (VolatilityValueException ex) {
            assertEquals(ex.getClass(), VolatilityValueException.BelowIntrinsicException.class);
            double python_value = pyLetsBeRational.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q);
            assertEquals(python_value, ex.getValue(), Constants.DBL_EPSILON);
        }

        double beta = 0.2;
        double x = Math.log(300/100);
        try {
            LetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q);
            fail("BelowInstrinsicException should be thrown.");
        } catch (VolatilityValueException ex) {
            assertEquals(ex.getClass(), VolatilityValueException.BelowIntrinsicException.class);
            double python_value = pyLetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q);
            assertEquals(python_value, ex.getValue(), Constants.DBL_EPSILON);
        }
    }

    @Test
    public void test_above_volatility_exceptions() {
        double F = 100.0;
        double K = 100.0;
        double T = 0.5;
        double q = 1;

        double price = 200;
        try {
            // above maximum
            // max_price = K if q < 0 else F
            LetsBeRational.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q);
            fail("AboveMaximumException should be thrown.");
        } catch (VolatilityValueException ex) {
            assertEquals(ex.getClass(), VolatilityValueException.AboveMaximumException.class);
            double python_value = pyLetsBeRational.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q);
            assertEquals(python_value, ex.getValue(), Constants.DBL_EPSILON);
        }

        double beta = 200;
        double x = Math.log(300/100);
        try {
            LetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q);
            fail("AboveMaximumException should be thrown.");
        } catch (VolatilityValueException ex) {
            assertEquals(ex.getClass(), VolatilityValueException.AboveMaximumException.class);
            double python_value = pyLetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess(beta, x, q);
            assertEquals(python_value, ex.getValue(), Constants.DBL_EPSILON);
        }
    }

    private double getRandomF() {
        return ThreadLocalRandom.current().nextInt(10, 2000);
    }

    private double getRandomK() {
        return ThreadLocalRandom.current().nextInt(10, 2000);
    }

    private double getRandomSigma() {
        return ThreadLocalRandom.current().nextDouble(0.05, 1);
    }

    private double getRandomT() {
        DecimalFormat df = new DecimalFormat("#.##");
        double t = ThreadLocalRandom.current().nextDouble(0.2, 2);
        return Double.valueOf(df.format(t));
    }

    private int getRandomN() {
        return ThreadLocalRandom.current().nextInt(0, 4);
    }

    private double getRandomz() {
        return ThreadLocalRandom.current().nextDouble(0, 2);
    }

}
