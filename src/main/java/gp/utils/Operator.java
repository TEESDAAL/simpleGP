package gp.utils;

import gp.statistics.Selector;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Operator<I, O> {
    O produce(List<I> parents);
    Integer size();

    default O sampleFrom(Selector<I> selector) {
        return produce(IntStream.range(0, size())
                .mapToObj( ignored -> selector.sample())
                .toList()
        );
    }
    default <T> O sampleFrom(Selector<T> selector, Function<T, I> mapper) {
        return produce(IntStream.range(0, size())
                .mapToObj( ignored -> selector.sample())
                .map(mapper)
                .toList()
        );
    }

    static <I, O> BinaryOperator<I, O> bin(BinaryOperator<I, O> operator) {
        return operator;
    }
    static <I, O> UnaryOperator<I, O> unary(UnaryOperator<I, O> operator) {
        return operator;
    }
}
