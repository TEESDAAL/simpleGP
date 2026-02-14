package gp.random;

import gp.statistics.Selector;

import java.util.List;
import java.util.Random;

public record WeightedRandomSampler<T>(List<ProbabilisticElement<T>> distribution, Random rand) implements Selector<T> {
    public static <T> WeightedRandomSampler<T> of(List<ProbabilisticElement<T>> distribution, int seed) {
        return new WeightedRandomSampler<>(distribution, new Random(seed));
    }

    public WeightedRandomSampler {
        assert distribution.stream()
                .mapToDouble(ProbabilisticElement::probability)
                .sum() == 1.0;

        distribution = List.copyOf(distribution);
    }

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

