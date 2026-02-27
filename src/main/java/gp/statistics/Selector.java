package gp.statistics;

/**
 * Interface for supplying T's.
 * @param <T> The type of element to select
 */
public interface Selector<T> {
    /**
     * Samples and returns an element.
     * @return A selected element
     */
    T sample();
}

