package gp.impl.statistics;

import gp.core.statistics.Statistic;
import utils.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public enum CommonFunctions implements Function<List<Double>, Double>, ToDoubleFunction<List<Double>> {
    MEAN {
        @Override
        public double applyAsDouble(List<Double> numbers) {
            return MEAN(numbers);
        }

        @Override
        public Double apply(List<Double> numbers) {
            return MEAN(numbers);
        }
    },
    STD {
        @Override
        public Double apply(List<Double> numbers) {
            return STD(numbers);
        }
        @Override
        public double applyAsDouble(List<Double> numbers) {
            return STD(numbers);
        }
    },
    MEDIAN {
        @Override
        public Double apply(List<Double> value) {
            return MEDIAN(value);
        }

        @Override
        public double applyAsDouble(List<Double> numbers) {
            return MEDIAN(numbers);
        }
    };

    public static double MEAN(Collection<? extends Number> numbers) {
        return numbers.stream()
            .mapToDouble(Number::doubleValue)
            .average().orElse(0.0);
    }
    public static double STD(Collection<? extends Number> numbers) {
        double average = MEAN(numbers);
        return Math.sqrt(
            numbers.stream()
                    .mapToDouble(n -> Math.pow(n.doubleValue() - average, 2))
                    .sum()
        );
    }
    public static double MEDIAN(Collection<? extends Number> numbers) {
        Preconditions.assertFalse(numbers.isEmpty(), "Cannot get the median of an empty collection");
        ArrayList<? extends Number> sortedValue = new ArrayList<>(numbers);
        sortedValue.sort(Comparator.comparingDouble(Number::doubleValue));
        int midPoint = sortedValue.size() / 2;
        if (sortedValue.size() % 2 == 1) {
            return sortedValue.get(midPoint).doubleValue();
        }
        double left = sortedValue.get(midPoint - 1).doubleValue();
        double right = sortedValue.get(midPoint).doubleValue();
        return (left + right) / 2;
    }

    public <I> Statistic<I, Double> toStatistic(Function<I, Double> converter) {
        return p -> this.apply(p.stream().map(converter).toList());
    }
}