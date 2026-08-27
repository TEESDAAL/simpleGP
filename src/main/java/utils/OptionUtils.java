package utils;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public enum OptionUtils {;

    public static <A, B, R> Optional<R> and(
        Optional<A> first,
        Supplier<Optional<B>> second,
        BiFunction<A, B, R> f
    ) {
        return first.flatMap(
            a -> second.get().map(b -> f.apply(a, b))
        );
    }

    public static <A, B, C, R> Optional<R> and(
        Optional<A> first,
        Supplier<Optional<B>> second,
        Supplier<Optional<C>> third,
        TriFunction<A, B, C, R> f
    ) {
        return first.flatMap(
            a -> second.get().flatMap(b -> third.get().map(c -> f.apply(a, b, c)))
        );
    }

    public static <A, B, C, D, R> Optional<R> and(
        Optional<A> first,
        Supplier<Optional<B>> second,
        Supplier<Optional<C>> third,
        Supplier<Optional<D>> fourth,
        QuadFunction<A, B, C, D, R> f
    ) {
        return first.flatMap(
            a -> second.get().flatMap(b -> third.get().flatMap(
                c -> fourth.get().map(d -> f.apply(a, b, c, d))
            ))
        );
    }
}
