package utils;

import java.util.function.Function;


/**
 * A record representing a pair of values.
 * @param <A> The type of the first value
 * @param <B> The type of the second value
 * @param first The first value in the pair
 * @param second The second value in the pair
 */
public record Triple<A, B, C>(A first, B second, C third) {

    /**
     * Creates a new pair with the given values.
     * @param <A> The type of the first value
     * @param <B> The type of the second value
     * @param first The first value
     * @param second The second value
     * @return A new pair containing the provided values
     */
    public static <A, B, C> Triple<A, B, C> of(A first, B second, C third) {
        return new Triple<>(first, second, third);
    }

    /**
     * Creates a mapper that folds a pair into a value.
     *
     * @param mapper the mapping function
     * @param <A> the first value type
     * @param <B> the second value type
     * @param <R> the result type
     * @return a function that folds a pair into a value
     */
    public static <A, B, C, R> Function<Triple<A, B, C>, R> fold(
            final TriFunction<A, B, C, R> mapper
    ) {
        return triple -> triple.reduce(mapper);
    }


    /**
     * Folds the values in this pair to a new value using the provided function.
     *
     * @param <T> The type of the new value
     * @param mapper a function that maps the first and second values to a new value
     * @return the result of applying the mapping function to the values in this pair
     */
    public <T> T reduce(final TriFunction<? super A, ? super B, ? super C, T> mapper) {
        return mapper.apply(this.first, this.second, this.third);
    }

    /**
     * Maps the values in this pair to new values using the provided
     * mapping functions.
     * @param <U> The type of the new first value
     * @param <V> The type of the new second value
     * @param <W> the type of the new third value
     * @param firstMapper The function to map the first value
     * @param secondMapper The function to map the second value
     * @param thirdMapper The function to map the third value
     * @return The new pair containing the mapped values
     */
    public <U, V, W> Triple<U, V, W> map(
            Function<? super A, ? extends U> firstMapper,
            Function<? super B, ? extends V> secondMapper,
            Function<? super C, ? extends W> thirdMapper
    ) {
        return new Triple<>(
            firstMapper.apply(this.first),
            secondMapper.apply(this.second),
            thirdMapper.apply(this.third)
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
    public <U> Triple<U, B, C> mapFirst(
            final Function<? super A, ? extends U> firstMapper
    ) {
        return new Triple<>(firstMapper.apply(this.first), this.second, this.third);
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
    public <V> Triple<A, V, C> mapSecond(
        Function<? super B, ? extends V> secondMapper
    ) {
        return new Triple<>(this.first, secondMapper.apply(this.second), this.third);
    }


    /**
     * Maps the third value in this pair to a new value using the
     * provided mapping function, while keeping the first value
     * unchanged.
     * @param <V> The type of the new second value
     * @param thirdMapper The function to map the second value
     * @return The new pair containing the unchanged first value and
     *     the mapped second value
     */
    public <V> Triple<A, B, V> mapThird(
        final Function<? super C, ? extends V> thirdMapper
    ) {
        return new Triple<>(this.first, this.second, thirdMapper.apply(this.third));
    }
}
