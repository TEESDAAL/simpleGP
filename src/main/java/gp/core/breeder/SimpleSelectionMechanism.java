package gp.core.breeder;

import gp.core.selectors.Sampler;

import java.util.Collection;

/**
 * A selector builder that creates selectors from collections of items.
 * @param <E> The type of items to select from
 */
public interface SimpleSelectionMechanism<E> extends SelectionMechanism<E, E> {
    /**
     * Primes the selector with items.
     * @param items The items to select from
     * @return A primed selector
     */
    @Override
    Sampler<E> selectorFrom(Collection<E> items);
}

