package gp.impl.statistics;

import utils.Pair;

import java.util.Collection;
import java.util.List;

/**
 * Common evaluation functions for paired values.
 */
public enum EvaluationFunctions implements CollectToDouble<Pair<Double, Double>> {
    /** Mean absolute error. */
    MAE {
        @Override
        public double applyAsDouble(Collection<Pair<Double, Double>> pairs) {
            return meanAbsoluteError(pairs);
        }
    },
    /** Mean squared error. */
    MSE {
        @Override
        public double applyAsDouble(Collection<Pair<Double, Double>> pairs) {
            return meanSquaredError(pairs);
        }
    },
    /** Root mean squared error. */
    RMSE {
        @Override
        public double applyAsDouble(Collection<Pair<Double, Double>> value) {
            return rootMeanSquaredError(value);
        }
    };

    /**
     * Returns the mean of the squared value of x - y.
     * @param pairs The list of input points (x, y)
     * @return mean((x-y)^2)
     */
    public static double meanSquaredError(Collection<Pair<Double, Double>> pairs) {
        double squaredError = 0;
        for (Pair<Double, Double> pair : pairs) {
            squaredError += pair.reduce((a, b) -> Math.pow(a - b, 2));
        }
        return squaredError / pairs.size();
    }

    /**
     * Returns the square root of the mean squared error.
     * Useful as this error is in the same units as the input.
     * @param pairs The list of input points (x, y)
     * @return sqrt(mean((x-y)^2))
     */
    public static double rootMeanSquaredError(
        Collection<Pair<Double, Double>> pairs
    ) {
        return Math.sqrt(meanSquaredError(pairs));
    }

    /**
     * Returns the mean of the absolute value of x - y.
     * @param pairs The list of input points (x, y)
     * @return mean(|x-y|)
     */
    public static double meanAbsoluteError(
            Collection<Pair<Double, Double>> pairs
    ) {
        double absoluteError = 0;
        for (Pair<Double, Double> pair : pairs) {
            absoluteError += pair.reduce((a, b) -> Math.abs(a - b));
        }
        return absoluteError / pairs.size();
    }

    /**
     * Returns the sum of the absolute value of x - y.
     * @param pairs The list of input points (x, y)
     * @return sum(|x-y|)
     */
    public static double totalAbsoluteError(
            List<Pair<Double, Double>> pairs
    ) {
        double absoluteError = 0;
        for (Pair<Double, Double> pair : pairs) {
            absoluteError += pair.reduce((a, b) -> Math.abs(a - b));
        }
        return absoluteError;
    }
}
