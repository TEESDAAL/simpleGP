package gp.core.selector;

import java.util.function.Function;

/**
 * Interface for supplying T's.
 * @param <T> The type of element to select
 */
public interface Sampler<T> {
    /**
     * Samples and returns an element.
     * @return A selected element
     */
    T sample();

    /**
     * Convert this sampler into a sampler of R,
     *  by passing all outputs of this through converter.
     * @param converter The mapping function from T -> R
     * @return A sampler of R
     * @param <R> The return type of the new sampler.
     */
    default <R> Sampler<R> wrap(Function<T, R> converter) {
        return () -> converter.apply(this.sample());
    }
}

