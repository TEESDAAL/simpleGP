package gp.utils;

import java.util.function.Function;

/**
 * A record representing a pair of values.
 * @param <A> The type of the first value
 * @param <B> The type of the second value
 * @param first The first value in the pair
 * @param second The second value in the pair
 */
public record Pair<A, B>(A first, B second) {
    /**
     * Creates a new pair with the given values.
     * @param <A> The type of the first value
     * @param <B> The type of the second value
     * @param first The first value
     * @param second The second value
     * @return A new pair containing the provided values
     */
    public static <A, B> Pair<A, B> of(final A first, final B second) {
        return new Pair<>(first, second);
    }

    /**
     * Maps the values in this pair to new values using the provided
     * mapping functions.
     * @param <U> The type of the new first value
     * @param <V> The type of the new second value
     * @param firstMapper The function to map the first value
     * @param secondMapper The function to map the second value
     * @return The new pair containing the mapped values
     */
    public <U, V> Pair<U, V> map(
            final Function<? super A, ? extends U> firstMapper,
            final Function<? super B, ? extends V> secondMapper
    ) {
        return new Pair<>(
                firstMapper.apply(this.first),
                secondMapper.apply(this.second)
        );
    }

    /**
     * Maps the first value in this pair to a new value using the
     * provided mapping function, while keeping the second value
     * unchanged.
     * @param <U> The type of the new first value
     * @param firstMapper The function to map the first value
     * @return The new pair containing the mapped first value and the
     *     unchanged second value
     */
    public <U> Pair<U, B> mapFirst(
            final Function<? super A, ? extends U> firstMapper) {
        return new Pair<>(firstMapper.apply(this.first), this.second);
    }

    /**
     * Maps the second value in this pair to a new value using the
     * provided mapping function, while keeping the first value
     * unchanged.
     * @param <V> The type of the new second value
     * @param secondMapper The function to map the second value
     * @return The new pair containing the unchanged first value and
     *     the mapped second value
     */
    public <V> Pair<A, V> mapSecond(
            final Function<? super B, ? extends V> secondMapper) {
        return new Pair<>(this.first, secondMapper.apply(this.second));
    }
}
