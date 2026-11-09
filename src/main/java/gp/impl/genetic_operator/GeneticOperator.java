package gp.impl.genetic_operator;

import gp.core.individual.Individual;
import gp.impl.individual.lineage.Lineage;
import util.operator.Operator;
import util.sequence.Sequence;

import java.util.ArrayList;
import java.util.List;

public abstract class GeneticOperator<
        T, R,
        P extends Individual<?,?>,
        PIntermediate,
        Intermediate,
        I extends Individual<T,R>,
        Self extends GeneticOperator<T, R, P, PIntermediate, Intermediate, I, Self>
> implements Operator<P, Sequence<I>> {
    /**
     * Produces output from a list of parents.
     *
     * @param parents The list of parents to operate on
     * @return The produced output
     */
    public final Sequence<I> produce(List<P> parents) {
        boolean shouldTrack = false;
        final List<PIntermediate> intermediateParents = new ArrayList<>(parents.size());
        for (final P parent : parents) {
            shouldTrack |= parent.lineage().isTracked();
            intermediateParents.add(unwrap(parent));
        }
        if (shouldTrack) {
            return produceUntracked(intermediateParents).map(
                i -> wrap(i, Lineage.untracked)
            );
        }
        return produceUntracked(intermediateParents).map(
                i -> this.wrap(i, Lineage.descendant(
                        self(), parents
                ))
        );
    }

    /**
     * @return this, allows for the recovery of the original type/generic.
     */
    abstract protected Self self();

    /**
     * The abstract method that converts the parent type into whatever intermediate type.
     * @param parent The parent individual
     * @return The returned individual
     */
    abstract protected PIntermediate unwrap(P parent);

    /**
     * The abstract method that converts the base output into the desired type.
     * Also allows wrapping up the linage.
     * @param intermediateForm The intermediate form of the operation,
     *   usually this will end up looking like being the underlying tree.
     * @param lineage The Linage of this produced individual.
     *   Will be {@link Lineage.untracked} if *all* the parents are untracked
     * @return The desired final individual.
     */
    abstract protected I wrap(Intermediate intermediateForm, Lineage lineage);

    /**
     * The production method the implementor will call.
     * The future implementations of this method do not have to handle tracking lineage.
     * @param parents The parents that produced the given individual.
     * @return The sequence of produced individuals.
     */
    abstract protected Sequence<Intermediate> produceUntracked(List<PIntermediate> parents);
}

