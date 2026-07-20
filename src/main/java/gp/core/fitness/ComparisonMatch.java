package gp.core.fitness;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Interface for matching on the result of a comparison.
 * @param <R> The type of the result for each case
 */
public interface ComparisonMatch<R> {
    /**
     * Returns the result for an equal comparison.
     *
     * @return the equal result
     */
    R equal();

    /**
     * Returns the result for a better comparison.
     *
     * @return the better result
     */
    R better();

    /**
     * Returns the result for a worse comparison.
     *
     * @return the worse result
     */
    R worse();

    /**
     * Creates a new matcher based on the given suppliers for each case.
     *
     * @param equal the supplier to call when the comparison is equal
     * @param better the supplier to call when the comparison is better
     * @param worse the supplier to call when the comparison is worse
     * @param <R> the return type
     * @return the result of the appropriate supplier based on the comparison result
     */
    static <R> ComparisonMatch<R> of(
            final Supplier<R> equal,
            final Supplier<R> better,
            final Supplier<R> worse
    ) {
        return new ComparisonMatch<>() {
            @Override
            public R equal() {
                return equal.get();
            }

            @Override
            public R better() {
                return better.get();
            }

            @Override
            public R worse() {
                return worse.get();
            }
        };
    }

    /**
     * Apply some transformation to the return type.
     *
     * @param mapper the transformation to apply to the return type
     * @param <U> the new return type
     * @return the mapped supplier value
     */
    default <U> ComparisonMatch<U> map(Function<R, U> mapper) {
        return ComparisonMatch.of(
                () -> mapper.apply(equal()),
                () -> mapper.apply(better()),
                () -> mapper.apply(worse())
        );
    }
}
