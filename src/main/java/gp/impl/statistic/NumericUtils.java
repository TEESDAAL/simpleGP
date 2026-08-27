package gp.impl.statistic;

import utils.typed_functions.TypedBiFunction;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Numeric helper methods.
 */
public enum NumericUtils implements TypedBiFunction<Double, Double, Double> {
    /**
     * An operator for safe division. Returns 1 if b == 0.0
     * otherwise performs normal division.
     */
    SAFE_DIVISION {
        @Override
        public Double apply(Double a, Double b) {
            if (b == 0.0) {
                return 1.0;
            }
            return a / b;
        }
    };

    @Override
    public Class<Double> returnType() {
        return Double.class;
    }

    @Override
    public Class<Double> leftType() {
        return Double.class;
    }

    @Override
    public Class<Double> rightType() {
        return Double.class;
    }
    /**
     * Rounds a value to the requested number of decimal places.
     *
     * @param value the value to round
     * @param places the number of decimal places
     * @return the rounded value
     */
    public static double round(double value, int places) {
        assert places >= 0 : "Decimal places must be non-negative";

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

