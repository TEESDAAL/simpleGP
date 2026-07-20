package gp.impl.statistics;

import gp.core.statistics.Statistic;
import utils.Preconditions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Common collection-to-double aggregations.
 */
public enum CommonFunctions implements CollectToDouble<Double> {
    /** Mean aggregation. */
    MEAN {
        @Override
        public double applyAsDouble(Collection<Double> numbers) {
            return mean(numbers);
        }
    },
    /** Standard deviation aggregation. */
    STD {
        @Override
        public double applyAsDouble(Collection<Double> numbers) {
            return std(numbers);
        }
    },
    /** Median aggregation. */
    MEDIAN {
        @Override
        public double applyAsDouble(Collection<Double> numbers) {
            return median(numbers);
        }
    },
    /** Mode aggregation. */
    MODE {
        @Override
        public double applyAsDouble(Collection<Double> numbers) {
            return mode(numbers);
        }
    };

    /**
     * Computes the mean of the provided numbers.
     *
     * @param numbers the numbers to average
     * @return the arithmetic mean
     */
    public static double mean(Collection<? extends Number> numbers) {
        return numbers.stream()
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Computes the standard deviation of the provided numbers.
     *
     * @param numbers the numbers to measure
     * @return the standard deviation
     */
    public static double std(Collection<? extends Number> numbers) {
        final double average = mean(numbers);
        return Math.sqrt(
                numbers.stream()
                        .mapToDouble(n -> Math.pow(n.doubleValue() - average, 2))
                        .sum()
        );
    }

    /**
     * Computes the median of the provided numbers.
     *
     * @param numbers the numbers to measure
     * @return the median value
     */
    public static double median(Collection<? extends Number> numbers) {
        Preconditions.assertFalse(
                numbers.isEmpty(),
                "Cannot get the median of an empty collection"
        );
        final double[] sortedValues = numbers.stream()
                .mapToDouble(Number::doubleValue)
                .sorted()
                .toArray();

        final int midPoint = sortedValues.length / 2;
        if (sortedValues.length % 2 == 1) {
            return sortedValues[midPoint];
        }

        final double left = sortedValues[midPoint - 1];
        final double right = sortedValues[midPoint];
        return (left + right) / 2;
    }

    /**
     * Computes the most common number in the collection.
     *
     * @param numbers the numbers to count
     * @param <N> the number type
     * @return the mode value
     */
    public static <N extends Number> N mode(Collection<N> numbers) {
        Preconditions.assertFalse(
                numbers.isEmpty(),
                "Cannot get the mode of an empty collection"
        );

        final Map<N, Integer> frequencyMap = new HashMap<>();
        int maxFrequency = 0;
        N mode = null;

        for (N number : numbers) {
            final int frequency = frequencyMap.getOrDefault(number, 0) + 1;
            frequencyMap.put(number, frequency);

            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                mode = number;
            }
        }
        return mode;
    }

    /**
     * Converts the aggregation into a statistic over a population.
     *
     * @param converter the value extractor
     * @param <I> the population item type
     * @return a statistic that applies this aggregation
     */
    public <I> Statistic<I, Double> toStatistic(Function<I, Double> converter) {
        return population -> this.apply(population.stream().map(converter).toList());
    }
}
