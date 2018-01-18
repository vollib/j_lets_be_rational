package org.vollib.j_lets_be_rational;

/**
 * Created by Maricris on 17/02/2017.
 */
public class VolatilityValueException extends Exception {

    private VolatilityValueException() {
        super("Volatility Value is out of range.");
    }

    private VolatilityValueException(String message) {
        super(message);
    }

    public Double getValue() {
        return null;
    }

    public static class BelowIntrinsicException extends VolatilityValueException {
        public BelowIntrinsicException() {
            super("The volatility is below the intrinsic value.");
        }

        @Override
        public Double getValue() {
            return Constants.VOLATILITY_VALUE_TO_SIGNAL_PRICE_IS_BELOW_INTRINSIC;
        }
    }

    public static class AboveMaximumException extends VolatilityValueException {
        public AboveMaximumException() {
            super("The volatility is above the maximum value.");
        }

        @Override
        public Double getValue() {
            return Constants.VOLATILITY_VALUE_TO_SIGNAL_PRICE_IS_ABOVE_MAXIMUM;
        }
    }

}