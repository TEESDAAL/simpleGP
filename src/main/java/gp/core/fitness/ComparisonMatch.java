package gp.core.fitness;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Interface for matching on the result of a comparison.
 * @param <R> The type of the result for each case
 */
public interface ComparisonMatch<R> {
    R equal();
    R better();
    R worse();

    /**
     * Creates a new matcher based on the given suppliers for each case.
     * @param equal The supplier to call when the comparison is equal.
     * @param better The supplier to call when the comparison is better.
     * @param worse The supplier to call when the comparison is worse.
     * @return The result of the appropriate supplier based on the comparison result.
     * @param <R> The return type
     */
    static <R> ComparisonMatch<R> of(
        Supplier<R> equal,
        Supplier<R> better,
        Supplier<R> worse
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
     * @param mapper The tranformation to apply to the return type.
     * @return The mapped supplier value.
     * @param <U> The new return type.
     */
    default <U> ComparisonMatch<U> map(Function<R, U> mapper) {
        return ComparisonMatch.of(
            () -> mapper.apply(equal()),
            () -> mapper.apply(better()),
            () -> mapper.apply(worse())
        );
    }
}
