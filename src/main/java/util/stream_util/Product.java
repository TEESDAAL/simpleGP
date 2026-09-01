package util.stream_util;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public enum Product {;

    /**
     * Create a stream by taking the product of two lists.
     *
     * @param l1  The first list to take the product of.
     * @param l2  The second list to take the product of.
     * @param f   The function to apply to each pair of elements.
     * @param <A> The type of the elements in the first list.
     * @param <B> The type of the elements in the second list.
     * @param <R> The type of the results.
     * @return A stream of the results.
     */
    static <A, B, R> Stream<R> of(List<A> l1, List<B> l2, BiFunction<A, B, R> f) {
        return l1.stream()
                .flatMap(a -> l2.stream().map(b -> f.apply(a, b)));
    }

    /**
     * Create an infinite stream by repeatedly taking the product of l1 and l2.
     *
     * @param l1  The first list to take the product of.
     * @param l2  The second list to take the product of.
     * @param f   The function to apply to each pair of elements.
     * @param <A> The type of the elements in the first list.
     * @param <B> The type of the elements in the second list.
     * @param <R> The type of the results.
     * @return A stream of the results.
     */
    public static <A, B, R> Stream<R> cycle(
            List<A> l1, List<B> l2,
            BiFunction<A, B, R> f
    ) {
        return Stream.generate(() -> Product.of(l1, l2, f))
                .flatMap(s -> s);
    }
}

