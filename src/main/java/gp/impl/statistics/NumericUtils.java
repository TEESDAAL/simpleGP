package gp.impl.statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Numeric helper methods.
 */
public final class NumericUtils {
    private NumericUtils() {
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

