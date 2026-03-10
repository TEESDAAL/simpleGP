package gp.breeder;

import gp.statistics.Selector;

import java.util.Collection;

/**
 * Interface for building selectors from collections.
 * @param <U> The type of items to select from
 * @param <V> The type of items to produce from the selector
 */
public interface SelectorFrom<U, V> {
    /**
     * Primes the selector with items.
     * @param items The items to select from
     * @return A primed selector
     */
    Selector<V> prime(Collection<U> items);
}

