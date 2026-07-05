package gp.impl.statistics;

import utils.Pair;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public enum EvaluationFunctions implements Function<Collection<Pair<Double, Double>>, Double>, ToDoubleFunction<Collection<Pair<Double, Double>>> {
    MAE {
        @Override
        public Double apply(Collection<Pair<Double, Double>> pairs) {
            return MAE(pairs);
        }

        @Override
        public double applyAsDouble(Collection<Pair<Double, Double>> pairs) {
            return MAE(pairs);
        }
    },
    MSE {
        @Override
        public Double apply(Collection<Pair<Double, Double>> pairs) {
            return MSE(pairs);
        }

        @Override
        public double applyAsDouble(Collection<Pair<Double, Double>> pairs) {
            return MSE(pairs);
        }
    },
    RMSE {
        @Override
        public Double apply(Collection<Pair<Double, Double>> pairs) {
            return RMSE(pairs);
        }

        @Override
        public double applyAsDouble(Collection<Pair<Double, Double>> value) {
            return RMSE(value);
        }
    };

    public static double MSE(Collection<Pair<Double, Double>> pairs) {
        double squaredError = 0;
        for (Pair<Double, Double> pair : pairs) {
            squaredError += pair.reduce((a, b) -> Math.pow(a - b, 2));
        }
        return squaredError / pairs.size();
    }

    static double RMSE(Collection<Pair<Double, Double>> pairs) {
        return Math.sqrt(MSE(pairs));
    }

    static double MAE(Collection<Pair<Double, Double>> pairs) {
        double absoluteError = 0;
        for (Pair<Double, Double> pair : pairs) {
            absoluteError += pair.reduce((a, b) -> Math.abs(a - b));
        }
        return absoluteError / pairs.size();
    }
}
