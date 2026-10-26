package gp.impl.individual.lineage;


import gp.core.individual.Individual;
import gp.core.individual.LineagedIndividual;
import util.operator.Operator;

import java.util.List;
import java.util.Optional;

public non-sealed interface Descendant<
        Ind extends LineagedIndividual<?, ?, ?, ?>,
        O extends Operator<P, List<Ind>>,
        P extends Individual<?, ?>
> extends Tracked<Ind> {
    O operator();
    List<P> parents();

    @Override
    default Optional<Progenitor<?, ?>> asProgenitor() {
        return Optional.empty();
    }
    @Override
    default Optional<Descendant<?, ?, ?>> asDescendant() {
        return Optional.of(this);
    }
}

record DescendantImpl<
        Ind extends Individual<?, ?>,
        O extends Operator<?,?>,
        P extends Individual<?, ?>
>(Ind current, O operator, List<P> parents) implements Descendant<Ind, O, P> {}
