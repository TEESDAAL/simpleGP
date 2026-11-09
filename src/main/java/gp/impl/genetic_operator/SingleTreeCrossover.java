package gp.impl.genetic_operator;

import gp.impl.genetic_operator.crossover.Crossover;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.individual.lineage.Lineage;
import gp.impl.individual.typed_tree.ImmutableNode;
import util.random.RandomSource;

public final class SingleTreeCrossover<T, Out> extends Crossover<
        T, Out,
        SingleTreeIndividual<T, Out>,
        SingleTreeCrossover<T, Out>
> {

    public SingleTreeCrossover(int maxTries, int maxDepth, RandomSource random) {
        super(maxTries, maxDepth, random);
    }


    @Override
    protected SingleTreeIndividual<T, Out> wrap(
            ImmutableNode<T, Out, ?, ?> intermediateForm,
            Lineage lineage
    ) {
        return SingleTreeIndividual.of(intermediateForm, lineage);
    }
}
