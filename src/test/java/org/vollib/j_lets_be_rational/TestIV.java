package org.vollib.j_lets_be_rational;

/**
 * Created by Maricris on 07/02/2017.
 */
public class TestIV {

    public static void main(String[] args) throws VolatilityValueException {
        int N = 10000000;

        double K = 100;
        long start = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            K = K + 1.0e-12;
            LetsBeRational.implied_volatility_from_a_transformed_rational_guess(1.0, 100, K, .5, -1);
        }
        long time = System.currentTimeMillis() - start;
        double java_run = time/1000.0;
        System.out.printf("j_lets_be_rational ran in %.6f seconds\n", java_run);

    }

}
