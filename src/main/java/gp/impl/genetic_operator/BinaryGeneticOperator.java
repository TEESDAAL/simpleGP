package gp.impl.genetic_operator;

import gp.core.individual.Individual;
import util.sequence.Sequence;

import java.util.List;

public abstract class BinaryGeneticOperator<
        T, R,
        P extends Individual<?,?>,
        PIntermediate,
        Intermediate,
        I extends Individual<T,R>,
        Self extends BinaryGeneticOperator<T, R, P, PIntermediate, Intermediate, I, Self>
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
        assert parents.size() == 2;
        return produceUntracked(
                parents.getFirst(),
                parents.get(1)
        );
    }

    abstract protected Sequence<Intermediate> produceUntracked(
            PIntermediate first,
            PIntermediate second
    );

    @Override
    public final int arity() {
        return 2;
    }
}
