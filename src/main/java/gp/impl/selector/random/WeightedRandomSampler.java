package gp.impl.selector.random;

import gp.core.selector.Sampler;
import util.ArrayUtils;
import util.random.RandomSource;

import java.util.List;

/**
 * A sampler that selects elements according to a probability
 * distribution.
 *
 * @param <T>          The type of element to sample
 * @param distribution The probability distribution of elements
 * @param rand         The random number generator
 */
public record WeightedRandomSampler<T>(
        RandomSource rand,
        List<ProbabilisticElement<T>> distribution
) implements Sampler<T> {
    /**
     * Compact constructor that ensures the probabilities sum to 1.0
     * and makes a defensive copy.
     */
    public WeightedRandomSampler {
        assert distribution.stream()
                .mapToDouble(ProbabilisticElement::probability)
                .sum() == 1.0
                : "Probability does not sum to one, instead: " + distribution.stream()
                .mapToDouble(ProbabilisticElement::probability)
                .sum();

        distribution = List.copyOf(distribution);
    }

    /**
     * Creates a weighted random sampler from a distribution and seed.
     *
     * @param <T>          The element type
     * @param distribution The probability distribution
     * @param seed         The random seed
     * @return A new sampler
     */
    public static <T> WeightedRandomSampler<T> of(
            final List<ProbabilisticElement<T>> distribution,
            final int seed
    ) {
        return new WeightedRandomSampler<>(RandomSource.of(seed), distribution);
    }


    /**
     * Creates a weighted random sampler from a distribution and seed.
     *
     * @param <T>          The element type
     * @param distribution The probability distribution
     * @param random       The source of randomness
     * @return A new sampler
     */
    public static <T> WeightedRandomSampler<T> of(
            final List<ProbabilisticElement<T>> distribution,
            RandomSource random
    ) {
        return new WeightedRandomSampler<>(random, distribution);
    }

    /**
     * Creates a weighted random sampler from a distribution and seed.
     *
     * @param <T>            The element type
     * @param defaultElement The default fall-back element of the distribution
     * @param remaining      The probability distribution of the remaining elements.
     * @param random         The source of randomness
     * @return A new sampler
     */
    @SafeVarargs
    public static <T> WeightedRandomSampler<T> of(
            RandomSource random,
            T defaultElement,
            ProbabilisticElement<T>... remaining
    ) {
        final DistributionBuilder<T> distribution = DistributionBuilder.withDefault(
                defaultElement
        );
        ArrayUtils.forEach(remaining, distribution::addElement);
        return new WeightedRandomSampler<>(random, distribution.toList());
    }


    /**
     * Samples an element according to the probability distribution.
     *
     * @return A randomly selected element based on probabilities
     */
    public T sample() {
        double sum = rand.nextDouble(0.0, 1.0);

        for (final ProbabilisticElement<T> probabilisticElement : distribution) {
            sum -= probabilisticElement.probability();
            // So an element with prob 0 is never selected
            if (sum < 0.0) {
                return probabilisticElement.element();
            }
        }
        // For this to happen either the sum has to != 1 or distribution is empty
        throw new IllegalStateException("Failed to sample probability distribution");
    }
}

