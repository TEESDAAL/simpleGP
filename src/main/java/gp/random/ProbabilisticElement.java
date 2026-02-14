package gp.random;

public record ProbabilisticElement<E>(Double probability, E element) {
    public ProbabilisticElement {
        if (!Double.isFinite(probability) || probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException(probability + " is not a valid probability");
        }
    }

    public static <E> ProbabilisticElement<E> of(Double probability, E element) {
        return new ProbabilisticElement<>(probability, element);
    }
}
