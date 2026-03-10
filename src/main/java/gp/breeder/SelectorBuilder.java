package gp.breeder;

import gp.statistics.Selector;

import java.util.Collection;

public interface SelectorBuilder<I> extends SelectorFrom<I, I> {
    /**
     * Primes the selector with items.
     * @param items The items to select from
     * @return A primed selector
     */
    @Override
    Selector<I> prime(Collection<I> items);
}
