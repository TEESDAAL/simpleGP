package utils.stream_utils;


import utils.Mutable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

/**
 * A class to fold a stream until a certain condition is met, is a gatherer rather
 * than collector to support early termination.
 * Expected use: `Stream.of(1,2,3,4).gather(FoldUntil.of(0, Integer::sum, sum -> sum > 5)).findFirst();`
 * @param <T> The type of the stream
 * @param <R> The type of the value to fold to.
 */
public class FoldUntil<T, R> implements Gatherer<T, Mutable<R>, R> {
    private final R initialValue;
    private final BiFunction<R, T, R> accumulator;
    private final Predicate<R> until;
    private final BinaryOperator<R> tieResolver;


    FoldUntil(R initialValue, BiFunction<R, T, R> accumulator, Predicate<R> until, BinaryOperator<R> tieResolver) {
        this.initialValue = initialValue;
        this.accumulator = accumulator;
        this.until = until;
        this.tieResolver = tieResolver;
    }

    public static <T, R> FoldUntil<T, R> of(R initialValue, BiFunction<R, T, R> accumulator, Predicate<R> until) {
        return new FoldUntil<>(
                initialValue,
                accumulator,
                until,
                (_, _) -> {throw new UnsupportedOperationException("Cannot resolve ties");}
        );
    }

    public static <T, R> FoldUntil<T, R> parallel(
            R initialValue,
            BiFunction<R, T, R> accumulator,
            Predicate<R> until,
            BinaryOperator<R> combiner
    ) {
        return new FoldUntil<>(
                initialValue,
                accumulator,
                until,
                combiner
        );
    }


    @Override
    public Supplier<Mutable<R>> initializer() {
        return () -> Mutable.of(initialValue);
    }

    @Override
    public Integrator<Mutable<R>, T, R> integrator() {
        return (state, element, _) -> {
            state.update(r -> accumulator.apply(r, element));
            return !until.test(state.get());
        };
    }

    @Override
    public BinaryOperator<Mutable<R>> combiner() {
        return (a, b) -> {
            if (until.test(a.get())) {
                return a;
            }
            if (until.test(b.get())) {
                return b;
            }
            return Mutable.of(tieResolver.apply(a.get(), b.get()));
        };
    }

    @Override
    public BiConsumer<Mutable<R>, Downstream<? super R>> finisher() {
        return (state, downstream) -> {
            if (downstream.isRejecting()) {return;}
            downstream.push(state.get());
        };
    }
}
