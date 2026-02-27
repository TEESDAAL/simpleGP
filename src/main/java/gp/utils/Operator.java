package gp.utils;

import gp.statistics.Selector;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * A generic operator that takes a list[O] of size `size()` and
 * produces an output.
 * @param <I> The input type of the parents
 * @param <O> The output type
 */
public interface Operator<I, O> {
    /**
     * Produces output from a list of parents.
     * @param parents The list of parents to operate on
     * @return The produced output
     */
    O produce(List<I> parents);

    /**
     * Returns the number of parents this operator requires.
     * @return The expected size of the parents list
     */
    Integer size();

    /**
     * Produces output by sampling parents from a selector.
     * @param selector The selector to sample parents from
     * @return The produced output
     */
    default O sampleFrom(final Selector<I> selector) {
        return produce(IntStream.range(0, size())
                .mapToObj(ignored -> selector.sample())
                .toList()
        );
    }

    /**
     * Produces output by sampling from a selector and mapping the
     * results.
     * @param <T> The type of elements in the selector
     * @param selector The selector to sample from
     * @param mapper A function to map selected elements to the input
     *     type
     * @return The produced output
     */
    default <T> O sampleFrom(
            final Selector<T> selector,
            final Function<T, I> mapper
    ) {
        return produce(IntStream.range(0, size())
                    .mapToObj(ignored -> selector.sample())
                .map(mapper)
                .toList()
        );
    }

    /**
     * Casts an operator to a BinaryOperator type.
     * @param <I> The input type
     * @param <O> The output type
     * @param operator The binary operator
     * @return The same operator
     */
    static <I, O> BinaryOperator<I, O> bin(
            final BinaryOperator<I, O> operator
    ) {
        return operator;
    }

    /**
     * Casts an operator to a UnaryOperator type.
     * @param <I> The input type
     * @param <O> The output type
     * @param operator The unary operator
     * @return The same operator
     */
    static <I, O> UnaryOperator<I, O> unary(
            final UnaryOperator<I, O> operator) {
        return operator;
    }
}
