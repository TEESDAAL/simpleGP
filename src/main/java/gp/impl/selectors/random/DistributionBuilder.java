package gp.impl.selectors.random;

import utils.Preconditions;
import utils.random.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A builder for a probability distribution.
 * @param <E> The type of the elements in the distribution.
 */
public final class DistributionBuilder<E> {
    E defaultElement = null;
    private double accumulatedProb = 0.0;
    private final ArrayList<ProbabilisticElement<E>> distribution = new ArrayList<>();

    private DistributionBuilder() {}

    /**
     * Create an empty distribution builder.
     * @return An empty distribution builder.
     * @param <E> The type of the elements in the distribution.
     */
    public static <E> DistributionBuilder<E> empty() {
        return new DistributionBuilder<>();
    }

    /**
     * Create a distribution builder starting with a given element.
     * @param probability The probability of the first element.
     * @param element The first element to add to the distribution.
     * @return A distribution builder starting with the given element.
     * @param <E> The type of the elements in the distribution.
     */
    public static <E> DistributionBuilder<E> startingWith(double probability, E element) {
        return DistributionBuilder.<E>empty()
            .addElement(probability, element);
    }

    /**
     * Add a probabilistic element to the distribution.
     * @param element The element to add to the distribution.
     * @return this builder.
     * @throws IllegalArgumentException If the distribution would sum to > 1.0.
     */
    public DistributionBuilder<E> addElement(ProbabilisticElement<E> element) {
        if (this.accumulatedProb + element.probability() > 1.0) {
            throw new IllegalArgumentException(
                "After adding element " + element + "the distribution would sum to > 1.0"
            );
        }
        this.accumulatedProb += element.probability();
        this.distribution.add(element);
        return this;
    }

    /**
     * Add an element to the distribution with a given probability.
     * @param probability The probability of the element.
     * @param element The element to add to the distribution.
     * @return this builder.
     * @throws IllegalArgumentException If the distribution would sum to > 1.0.
     */
    public DistributionBuilder<E> addElement(double probability, E element) {
        this.distribution.add(ProbabilisticElement.of(probability, element));
        return this;
    }

    /**
     * Add a default element to the distribution.
     * @param element The default element to add to the distribution.
     * @return this builder.
     * @throws IllegalArgumentException If a default element has already been added.
     */
    public DistributionBuilder<E> addDefault(E element) {
        Preconditions.assertTrue(
            this.defaultElement == null,
            "Each distribution can only have one default element"
        );
        this.defaultElement = element;
        return this;
    }

    /**
     * Convert the distribution to a weighted random sampler.
     * @param random The source of randomness.
     * @return A weighted random sampler.
     * @throws IllegalStateException If the distribution does not sum to 1.0.
     */
    public WeightedRandomSampler<E> toSampler(RandomSource random) {
        return WeightedRandomSampler.of(this.toList(), random);
    }

    /**
     * Convert the distribution to a list of probabilistic elements.
     * @return A list of probabilistic elements.
     * @throws IllegalStateException If the distribution does not sum to 1.0.
     */
    public List<ProbabilisticElement<E>> toList() {
        Preconditions.assertTrue(
            this.accumulatedProb == 1.0 || this.defaultElement != null,
            "Tried to construct a probability distribution which doesn't sum to one."
        );

        return Stream.concat(
            this.distribution.stream(),
            Stream.of(ProbabilisticElement.of(
                1.0 - accumulatedProb, this.defaultElement
            ))
        ).toList();
    }
}
