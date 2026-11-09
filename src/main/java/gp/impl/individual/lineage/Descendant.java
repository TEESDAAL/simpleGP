package gp.impl.individual.lineage;


import gp.core.individual.Individual;
import gp.core.individual.LineagedIndividual;
import util.operator.Operator;
import util.sequence.Sequence;

import java.util.List;
import java.util.Optional;

public non-sealed interface Descendant<
        I,
        P extends Individual<?, ?>,
        Op extends Operator<P, Sequence<I>>
> extends Tracked {
    Op operator();
    List<P> parents();

    @Override
    default Optional<Progenitor<?>> asProgenitor() {
        return Optional.empty();
    }
    @Override
    default Optional<Descendant<?, ?, ?>> asDescendant() {
        return Optional.of(this);
    }
}

record DescendantImpl<
        I,
        P extends Individual<?, ?>,
        Op extends Operator<P, Sequence<I>>
>(Op operator, List<P> parents) implements Descendant<I, P, Op> {}
