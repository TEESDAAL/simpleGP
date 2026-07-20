package utils.operators;

import gp.core.selectors.Sampler;
import utils.Pair;

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
     * @return The expected size of the parents list.
     */
    Integer arity();


    /**
     * Convert this operator from a function of I->O into I->R
     *  by wrapping it in a function that converts O->R.
     * @param wrapper the function to wrap the operator in
     * @return the new wrapped I->R function.
     * @param <R> the new return type of the function.
     */
    default <R> Operator<I, R> wrap(final Function<O, R> wrapper) {
        final Operator<I, O> self = this;
        return new Operator<>() {
            @Override
            public R produce(final List<I> parents) {
                return wrapper.apply(Operator.this.produce(parents));
            }

            @Override
            public Integer arity() {
                return self.arity();
            }
        };
    }

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

    /**
     * Create a version of this operator that caches it's inputs.
     * @return a version of this operator that caches it's inputs.
     */
    default Operator<I, O> cached() {
        return CacherCache.cached(this);
    }

}

/**
 * A class for handling the conversion of an singleton operator to
 *  a singleton cached operator.
 */
final class CacherCache {
    private CacherCache() {
    }
    /**
     * The cache to maintain singletons for general operators.
     */
    private static final Map<Operator<?, ?>, Operator<?, ?>> CACHE_CACHE =
        new ConcurrentHashMap<>();
    /**
     * The cache to maintain singletons for unary operators.
     */
    private static final Map<UnaryOperator<?, ?>, UnaryOperator<?, ?>> UNARY_CACHE =
        new ConcurrentHashMap<>();

    /**
     * The cache to maintain singletons for binary operators.
     */
    private static final Map<BinaryOperator<?, ?>, BinaryOperator<?, ?>> BI_CACHE =
        new ConcurrentHashMap<>();

    /**
     * Return a cached version of the operator.
     * @param operator The operator to make cached.
     * @return A general operator that caches it's inputs.
     * @param <I> The input type of the operator.
     * @param <O> The output type of the operator.
     */
    @SuppressWarnings("unchecked")
    static <I, O> Operator<I, O> cached(final Operator<I, O> operator) {
        return (Operator<I, O>) CACHE_CACHE.computeIfAbsent(
            operator,
            op -> new Operator<I, O>() {
                private final Map<List<I>, O> cache = new ConcurrentHashMap<>();

                @Override
                public O produce(final List<I> parents) {
                    return cache.computeIfAbsent(
                        parents,
                        p -> ((Operator<I, O>) op).produce(p)
                    );
                }

                @Override
                public Integer arity() {
                    return op.arity();
                }
            }
        );
    }

    /**
     * Return a cached version of the operator.
     * @param operator The operator to make cached.
     * @return A unary operator that caches it's inputs.
     * @param <I> The input type of the operator.
     * @param <O> The output type of the operator.
     */
    @SuppressWarnings("unchecked")
    static <I, O> UnaryOperator<I, O> unaryCached(
        final UnaryOperator<I, O> operator
    ) {
        return (UnaryOperator<I, O>) UNARY_CACHE.computeIfAbsent(
            operator,
            _ -> new  UnaryOperator<I, O>() {
                private final Map<I, O> cache = new ConcurrentHashMap<>();
                @Override
                public O produce(I parent) {
                    return cache.computeIfAbsent(
                        parent,
                        operator::produce
                    );
                }
            }
        );
    }

    /**
     * Return a cached version of the operator.
     * @param operator The operator to make cached.
     * @return A unary operator that caches it's inputs.
     * @param <I> The input type of the operator.
     * @param <O> The output type of the operator.
     */
    @SuppressWarnings("unchecked")
    static <I, O> BinaryOperator<I, O> biCached(
        final BinaryOperator<I, O> operator
    ) {
        return (BinaryOperator<I, O>) BI_CACHE.computeIfAbsent(
            operator,
            _ -> new BinaryOperator<I, O>() {
                private final Map<Pair<I, I>, O> cache = new ConcurrentHashMap<>();
                @Override
                public O produce(I parent1, I parent2) {
                    return cache.computeIfAbsent(
                        Pair.of(parent1, parent2),
                        _ -> operator.produce(parent1, parent2)
                    );
                }
            }
        );
    }
}
