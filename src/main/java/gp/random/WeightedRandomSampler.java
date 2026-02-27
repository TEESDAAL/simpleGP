package gp.random;

import gp.statistics.Selector;

import java.util.List;
import java.util.Random;

/**
 * A sampler that selects elements according to a probability
 * distribution.
 * @param <T> The type of element to sample
 * @param distribution The probability distribution of elements
 * @param rand The random number generator
 */
public record WeightedRandomSampler<T>(
        Random rand,
        List<ProbabilisticElement<T>> distribution
) implements Selector<T> {
    /**
     * Compact constructor that ensures the probabilities sum to 1.0
     * and makes a defensive copy.
     */
    public WeightedRandomSampler {
        assert distribution.stream()
                .mapToDouble(ProbabilisticElement::probability)
                .sum() == 1.0;

        distribution = List.copyOf(distribution);
    }

    /**
     * Creates a weighted random sampler from a distribution and seed.
     * @param <T> The element type
     * @param distribution The probability distribution
     * @param seed The random seed
     * @return A new sampler
     */
    public static <T> WeightedRandomSampler<T> of(
            final List<ProbabilisticElement<T>> distribution,
            final int seed
) {
        return new WeightedRandomSampler<>(new Random(seed), distribution);
    }


    /**
     * Samples an element according to the probability distribution.
     * @return A randomly selected element based on probabilities
     */
    public T sample() {
        double sum = rand.nextDouble();

        for (ProbabilisticElement<T> probabilisticElement : distribution) {
            sum -= probabilisticElement.probability();
            // So an element with prob 0 is never selected
            if (sum < 0.0) {
                return probabilisticElement.element();
            }
        }
        // For this to happen either the sum has to > 1
        throw new IllegalStateException("Failed to sample probability distribution");
    }
}

