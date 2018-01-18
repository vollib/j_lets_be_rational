package org.vollib.j_lets_be_rational;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LetsBeRationalTest {

    private void assertAlmostEqual(Double expected, Double actual) {
        double epsilon = 1.0e-12;

        if (actual == null || expected == null) {
            fail(String.format("%.12f != %.12f", actual, expected));
        } else {
            assertTrue(String.format("%.12f != %.12f", actual, expected), Math.abs(actual - expected) < epsilon);
        }
    }

    @Test
    public void testBlack() throws Exception {
        double F = 100;
        double K = 100;
        double sigma = .2;
        double T = .5;
        double q = 1;  // CALL = 1 PUT = -1

        double actual = LetsBeRational.black(F, K, sigma, T, q);
        double expected = 5.637197779701664;
        assertAlmostEqual(expected, actual);
    }

    @Test
    public void testImplied_volatility_from_a_transformed_rational_guess() throws Exception {
        double F = 100;
        double K = 100;
        double sigma = .2;
        double T = .5;
        double q = 1;  // CALL = 1 PUT = -1

        double price = 5.637197779701664;
        double actual = LetsBeRational.implied_volatility_from_a_transformed_rational_guess(price, F, K, T, q);
        double expected = 0.2;
        assertAlmostEqual(expected, actual);
    }

    @Test
    public void testImplied_volatility_from_a_transformed_rational_guess_with_limited_iterations() throws Exception {
        double F = 100;
        double K = 100;
        double sigma = .232323232;
        double T = .5;
        double q = 1;  // CALL = 1 PUT = -1
        int N = 1;

        double price = 6.54635543387;
        double actual = LetsBeRational.implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(price, F, K, T, q, N);
        double expected = 0.232323232;
        assertAlmostEqual(expected, actual);
    }

    @Test
    public void testNormalised_black() throws Exception {
        double F = 100;
        double K = 95;
        double T = 0.5;
        double sigma = 0.3;

        double x = Math.log(F/K);
        double s = sigma * Math.sqrt(T);

        double q = -1;  // CALL = 1 PUT = -1
        double actual_put = LetsBeRational.normalised_black(x, s, q);
        double expected_put = 0.061296663817558904;
        assertAlmostEqual(actual_put, expected_put);

        q = 1;  // CALL = 1 PUT = -1
        double actual_call = LetsBeRational.normalised_black(x, s, q);
        double expected_call = 0.11259558142181655;
        assertAlmostEqual(actual_call, expected_call);
    }

    @Test
    public void testNormalised_black_call() throws Exception {
        double F = 100;
        double K = 95;
        double T = 0.5;
        double sigma = 0.3;

        double x = Math.log(F / K);
        double s = sigma * Math.sqrt(T);

        double actual = LetsBeRational.normalised_black_call(x, s);
        double expected = 0.11259558142181655;
        assertAlmostEqual(expected, actual);
    }

    @Test
    public void testNormalised_implied_volatility_from_a_transformed_rational_guess() throws Exception {
        double x = 0.0;
        double s = 0.2;
        double q = 1;  // CALL = 1 PUT = -1
        double beta_call = LetsBeRational.normalised_black(x, s, q);
        double actual = LetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess(beta_call, x, q);
        double expected = 0.2;
        assertAlmostEqual(expected, actual);

        x = 0.1;
        s = 0.23232323888;
        q = -1;  // CALL = 1 PUT = -1
        double beta_put = LetsBeRational.normalised_black(x, s, q);
        actual = LetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess(beta_put, x, q);
        expected = 0.23232323888;
        assertAlmostEqual(expected, actual);
    }

    @Test
    public void testNormalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations() throws Exception {
        double x = 0.0;
        double s = 0.2;
        double q = 1;  // CALL = 1 PUT = -1
        int N = 1;
        double beta_call = LetsBeRational.normalised_black(x, s, q);
        double actual = LetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(beta_call, x, q, N);
        double expected = 0.2;
        assertAlmostEqual(expected, actual);

        x = 0.1;
        s = 0.23232323888;
        q = -1;  // CALL = 1 PUT = -1
        N = 1;
        double beta_put = LetsBeRational.normalised_black(x, s, q);
        actual = LetsBeRational.normalised_implied_volatility_from_a_transformed_rational_guess_with_limited_iterations(beta_put, x, q, N);
        expected = 0.23232323888;
        assertAlmostEqual(expected, actual);
    }

    @Test
    public void testNormalised_vega() throws Exception {
        double x = 0.0;
        double s = 0.0;
        double actual = LetsBeRational.normalised_vega(x, s);
        double expected = 0.3989422804014327;
        assertAlmostEqual(expected, actual);

        x = 0.0;
        s = 2.937528694999807;
        actual = LetsBeRational.normalised_vega(x, s);
        expected = 0.13566415614561067;
        assertAlmostEqual(expected, actual);

        x = 0.0;
        s = 0.2;
        actual = LetsBeRational.normalised_vega(x, s);
        expected = 0.3969525474770118;
        assertAlmostEqual(expected, actual);
    }

    @Test
    public void testNormCdf() throws Exception {
        double z = 0.302569738839;
        double actual = NormalDistribution.norm_cdf(z);
        double expected = 0.618891110513;
        assertAlmostEqual(expected, actual);

        z = 0.161148382602;
        actual = NormalDistribution.norm_cdf(z);
        expected = 0.564011732814;
        assertAlmostEqual(expected, actual);
    }



}