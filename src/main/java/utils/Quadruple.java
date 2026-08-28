package utils;

/**
 * A record representing four values.
 *
 * @param <A> the type of the first value
 * @param <B> the type of the second value
 * @param <C> the type of the third value
 * @param <D> the type of the fourth value
 */
public record Quadruple<A, B, C, D>(A first, B second, C third, D fourth) {
    public static <A, B, C, D> Quadruple<A, B, C, D> of(
        A first, B second, C third, D fourth
    ) {
        return new Quadruple<>(first, second, third, fourth);
    }

    public <R> R reduce(
        QuadFunction<? super A, ? super B, ? super C, ? super D, R> mapper
    ) {
        return mapper.apply(first, second, third, fourth);
    }
}
