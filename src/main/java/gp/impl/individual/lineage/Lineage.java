package gp.impl.individual.lineage;


import gp.core.individual.Individual;
import gp.core.individual.LineagedIndividual;
import gp.core.initializer.Initialiser;
import util.operator.Operator;

import java.util.List;
import java.util.Optional;

public sealed interface Lineage permits Tracked, Untracked {
    static Untracked untracked() {
        return Untracked.instance;
    }

    static <
            Ind extends Individual<?,?>,
            Init extends Initialiser<Ind>
    > Progenitor<Ind, Init> progenitor(Ind individual, Init source) {
        return new ProgenitorImpl<>(individual, source);
    }

    static <
            Ind extends LineagedIndividual<?,?,?,?>,
            P extends LineagedIndividual<?,?>,
            Op extends Operator<P, Ind>
    > Descendant<Ind, Op, P> descendant(Ind individual, Op operator, List<P> parents) {
        return new DescendantImpl<>(individual, operator, parents);
    }
    Optional<Progenitor<?, ?>> asProgenitor();
    Optional<Descendant<?, ?, ?>> asDescendant();
}


