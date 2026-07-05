package gp.core.selectors;

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
}

