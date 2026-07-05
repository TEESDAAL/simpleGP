package utils.operators;

import gp.core.selectors.Sampler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    Integer arity();

    /**
     * Produces output by sampling parents from a selector.
     * @param sampler The selector to sample parents from
     * @return The produced output
     */
    default O sampleFrom(final Sampler<I> sampler) {
        return produce(IntStream.range(0, arity())
                .mapToObj(ignored -> sampler.sample())
                .toList()
        );
    }

    /**
     * Produces output by sampling from a selector and mapping the
     * results.
     * @param <T> The type of elements in the selector
     * @param sampler The selector to sample from
     * @param mapper A function to map selected elements to the input
     *     type
     * @return The produced output
     */
    default <T> O sampleFrom(
            final Sampler<T> sampler,
            final Function<T, I> mapper
    ) {
        return produce(IntStream.range(0, arity())
                    .mapToObj(ignored -> sampler.sample())
                .map(mapper)
                .toList()
        );
    }

    /**
     * Show a binary operator is a BinaryOperator.
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
            final UnaryOperator<I, O> operator
    ) {
        return operator;
    }

    default Operator<I, O> cached() {
        return CacherCache.cached(this);
    }

}

class CacherCache {
    private static final Map<Operator<?, ?>, Operator<?, ?>> cacheCache = new ConcurrentHashMap<>();
    private static final Map<UnaryOperator<?, ?>, UnaryOperator<?, ?>> unCache = new ConcurrentHashMap<>();
    private static final Map<BinaryOperator<?, ?>, BinaryOperator<?, ?>> biCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    static <I, O> Operator<I, O> cached(Operator<I, O> operator) {
        return (Operator<I, O>) cacheCache.computeIfAbsent(
            operator,
            op -> new Operator<I, O>() {
                final Map<List<I>, O> cache = new ConcurrentHashMap<>();

                @Override
                public O produce(List<I> parents) {
                    return cache.computeIfAbsent(
                        parents,
                        k -> ((Operator<I, O>) op).produce(parents)
                    );
                }

                @Override
                public Integer arity() {
                    return op.arity();
                }
            }
        );
    }

    @SuppressWarnings("unchecked")
    static <I, O> UnaryOperator<I, O> unaryCached(UnaryOperator<I, O> operator) {
        return (UnaryOperator<I, O>) unCache.computeIfAbsent(
            operator,
            op -> (UnaryOperator<I, O>) parent -> ((UnaryOperator<I, O>) op).produce(parent)
        );
    }

    @SuppressWarnings("unchecked")
    static <I, O> BinaryOperator<I, O> biCached(BinaryOperator<I, O> operator) {
        return (BinaryOperator<I, O>) biCache.computeIfAbsent(
            operator,
            op -> (BinaryOperator<I, O>) operator::produce
        );
    }

}