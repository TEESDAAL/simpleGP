package gp.breeder;

import gp.statistics.Selector;

import java.util.Collection;

public interface SelectorBuilder<I> {
    Selector<I> prime(Collection<I> items);
}
