package gp.impl.selectors.random;

import utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Record representing an element with an associated probability.
 * @param probability The probability of selecting this element (must
 *     be between 0 and 1)
 * @param element The element value
 * @param <E> The type of element
 */
public record ProbabilisticElement<E>(Double probability, E element) {
    /**
     * Compact constructor validating the probability.
     * @throws IllegalArgumentException if probability is not finite or
     *     outside [0, 1]
     */
    public ProbabilisticElement {
        if (!Double.isFinite(probability)
                || probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException(
                    probability + " is not a valid probability");
        }
    }

    /**
     * Creates a new probabilistic element.
     * @param <E> The element type
     * @param probability The probability value
     * @param element The element
     * @return A new probabilistic element
     * @throws IllegalArgumentException if probability is invalid
     */
    public static <E> ProbabilisticElement<E> of(
            final Double probability,
            final E element
    ) throws IllegalArgumentException {
        return new ProbabilisticElement<>(probability, element);
    }

        /**
         * Appends a fallback element with the remaining probability mass.
         *
         * @param fallback the fallback element
         * @param others the existing probabilistic elements
         * @param <E> the element type
         * @return the combined list including the fallback
         */
        public static <E> List<ProbabilisticElement<E>> withFallback(
            final E fallback,
            final List<ProbabilisticElement<E>> others
        ) {
        final double sum = others.stream().mapToDouble(e -> e.probability).sum();
        Preconditions.assertTrue(
            sum <= 1.0,
            "Probabilities sum to " + sum + ", they should be <= 1.0"
        );
        final List<ProbabilisticElement<E>> list =
            new ArrayList<>(others.size() + 1);
        list.addAll(others);
        list.add(new ProbabilisticElement<>(1.0 - sum, fallback));
        return list;
    }

}
