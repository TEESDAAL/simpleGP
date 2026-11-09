package gp.impl.genetic_operator;

import gp.core.individual.Individual;
import util.sequence.Sequence;

import java.util.List;

public abstract class UnaryGeneticOperator<
        T, R,
        P extends Individual<?,?>,
        PIntermediate,
        Intermediate,
        I extends Individual<T,R>,
        Self extends UnaryGeneticOperator<T, R, P, PIntermediate, Intermediate, I, Self>
> extends GeneticOperator<
        T, R,
        P,
        PIntermediate,
        Intermediate, I,
        Self
> {
    /**
     * The production method the implementor will call.
     * The future implementations of this method do not have to handle tracking lineage.
     * @param parents The parents that produced the given individual.
     * @return The sequence of produced individuals.
     */
    final protected Sequence<Intermediate> produceUntracked(List<PIntermediate> parents) {
        assert parents.size() == 1;
        return produceUntracked(parents.getFirst());
    }

    abstract protected Sequence<Intermediate> produceUntracked(PIntermediate parent);

    @Override
    public final int arity() {
        return 1;
    }
}
