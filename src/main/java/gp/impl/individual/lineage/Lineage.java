package gp.impl.individual.lineage;


import gp.core.individual.Individual;
import gp.core.initializer.Initialiser;
import util.operator.Operator;
import util.sequence.Sequence;

import java.util.List;
import java.util.Optional;

public sealed interface Lineage permits Tracked, Untracked {
    /**
     * The untracked lineage singleton.
     */
    Untracked untracked = Untracked.instance;

    static <
            Init extends Initialiser<?>
    > Progenitor<Init> progenitor(Init source) {
        return new ProgenitorImpl<>(source);
    }

    static <
            I,
            P extends Individual<?,?>,
            Op extends Operator<P, Sequence<I>>
    > Descendant<I, P, Op> descendant(Op operator, List<P> parents) {
        return new DescendantImpl<>(operator, parents);
    }
    Optional<Progenitor<?>> asProgenitor();
    Optional<Descendant<?, ?, ?>> asDescendant();

    boolean isTracked();
}
