package gp.random;

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
            final Double probability, final E element)
            throws IllegalArgumentException {
        return new ProbabilisticElement<>(probability, element);
    }
}
