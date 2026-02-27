package gp.breeder;

import gp.statistics.Selector;

import java.util.Collection;

/**
 * Interface for building selectors from collections.
 * @param <I> The individual type
 */
public interface SelectorBuilder<I> {
    /**
     * Primes the selector with items.
     * @param items The items to select from
     * @return A primed selector
     */
    Selector<I> prime(Collection<I> items);
}
