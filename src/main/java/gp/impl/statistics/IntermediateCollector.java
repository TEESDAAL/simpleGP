package gp.impl.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;

interface CollectToDouble<E> extends Collector<E, Collection<E>, Double>,
        Function<Collection<E>, Double>,
        ToDoubleFunction<Collection<E>> {
    @Override
    default Supplier<Collection<E>> supplier() {
        return ArrayList::new;
    }

    @Override
    default BiConsumer<Collection<E>, E> accumulator() {
        return Collection::add;
    }

    @Override
    default BinaryOperator<Collection<E>> combiner() {
        return (a, b) -> {
            a.addAll(b);
            return a;
        };
    }

    @Override
    default Function<Collection<E>, Double> finisher() {
        return this;
    }

    @Override
    default Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED, Characteristics.CONCURRENT);
    }

    @Override
    default Double apply(final Collection<E> doubles) {
        return this.applyAsDouble(doubles);
    }
}
