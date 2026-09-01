package gp.core.breeder;

import gp.core.selector.Sampler;

import java.util.List;

/**
 * Interface for building selectors from collections.
 *
 * @param <From> The type of items to select from
 * @param <To>   The type of items to produce from the selector
 */
public interface SelectionMechanism<From, To> {
    /**
     * Creates a selector that samples elements mapped from the given collection.
     *
     * @param items The items to select from
     * @return A primed selector
     */
    Sampler<To> selectorFrom(List<From> items);

    /**
     * Primes the selector with items.
     *
     * @param items The items to select from
     * @return A primed selector
     */
    default Sampler<To> selectorFrom(From[] items) {
        return this.selectorFrom(List.of(items));
    }
}

