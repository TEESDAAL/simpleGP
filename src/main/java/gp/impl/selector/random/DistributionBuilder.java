package gp.impl.selector.random;

import util.Preconditions;
import util.random.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A builder for a probability operatorDistribution.
 *
 * @param <E> The type of the elements in the operatorDistribution.
 */
public final class DistributionBuilder<E> {
    E defaultElement = null;
    private double accumulatedProb = 0.0;
    private final ArrayList<ProbabilisticElement<E>> distribution = new ArrayList<>();

    private DistributionBuilder() {}

    /**
     * Create an empty operatorDistribution builder.
     *
     * @param <E> The type of the elements in the operatorDistribution.
     * @return An empty operatorDistribution builder.
     */
    public static <E> DistributionBuilder<E> empty() {
        return new DistributionBuilder<>();
    }

    /**
     * Create a operatorDistribution builder starting with a given element.
     *
     * @param probability The probability of the first element.
     * @param element     The first element to add to the operatorDistribution.
     * @param <E>         The type of the elements in the operatorDistribution.
     * @return A operatorDistribution builder starting with the given element.
     */
    public static <E> DistributionBuilder<E> startingWith(double probability, E element) {
        return DistributionBuilder.<E>empty()
                .addElement(probability, element);
    }

    /**
     * Create a operatorDistribution builder starting with a given default element.
     *
     * @param element The default element of the operatorDistribution.
     * @param <E>     The type of the elements in the operatorDistribution.
     * @return A operatorDistribution builder starting with the given element.
     */
    public static <E> DistributionBuilder<E> withDefault(E element) {
        return DistributionBuilder.<E>empty()
                .addDefault(element);
    }

    /**
     * Add a probabilistic element to the operatorDistribution.
     *
     * @param element The element to add to the operatorDistribution.
     * @return this builder.
     * @throws IllegalArgumentException If the operatorDistribution would sum to > 1.0.
     */
    public DistributionBuilder<E> addElement(ProbabilisticElement<E> element) {
        Preconditions.assertTrue(
                element.probability() >= 0,
                "Cannot provide a negative probability"
        );
        Preconditions.assertTrue(
                this.accumulatedProb + element.probability() <= 1.0,
                "Adding element would put the total probability above 1.0"
        );

        this.accumulatedProb += element.probability();
        this.distribution.add(element);
        return this;
    }

    /**
     * Add an element to the operatorDistribution with a given probability.
     *
     * @param probability The probability of the element.
     * @param element     The element to add to the operatorDistribution.
     * @return this builder.
     * @throws IllegalArgumentException If the operatorDistribution would sum to > 1.0.
     */
    public DistributionBuilder<E> addElement(double probability, E element) {
        return this.addElement(ProbabilisticElement.of(probability, element));
    }

    /**
     * Add a default element to the operatorDistribution.
     *
     * @param element The default element to add to the operatorDistribution.
     * @return this builder.
     * @throws IllegalArgumentException If a default element has already been added.
     */
    public DistributionBuilder<E> addDefault(E element) {
        Preconditions.assertTrue(
                this.defaultElement == null,
                "Each operatorDistribution can only have one default element"
        );
        this.defaultElement = element;
        return this;
    }

    /**
     * Convert the operatorDistribution to a weighted random sampler.
     *
     * @param random The source of randomness.
     * @return A weighted random sampler.
     * @throws IllegalStateException If the operatorDistribution does not sum to 1.0.
     */
    public WeightedRandomSampler<E> toSampler(RandomSource random) {
        return WeightedRandomSampler.of(this.toList(), random);
    }

    /**
     * Convert the operatorDistribution to a list of probabilistic elements.
     *
     * @return A list of probabilistic elements.
     * @throws IllegalStateException If the operatorDistribution does not sum to 1.0.
     */
    public List<ProbabilisticElement<E>> toList() {
        Preconditions.assertTrue(
                this.accumulatedProb == 1.0 || this.defaultElement != null,
                "Tried to construct a probability operatorDistribution which doesn't sum to one."
        );

        return Stream.concat(
                this.distribution.stream(),
                Stream.of(ProbabilisticElement.of(
                        1.0 - accumulatedProb, this.defaultElement
                )).filter(e -> e.element() != null)
        ).toList();
    }
}
