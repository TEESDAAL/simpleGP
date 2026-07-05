package gp.impl.statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumericUtils {
    public static double round(double value, int places) {
        assert places >= 0 : "Decimal places must be non-negative";

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

