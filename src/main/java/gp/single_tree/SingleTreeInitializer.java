package gp.single_tree;

import gp.initializers.BaseInitializer;
import gp.utils.IndividualInitializer;

public record SingleTreeInitializer<T, Out>(
        BaseInitializer<T, Out> initializer
) implements IndividualInitializer<SingleTreeIndividual<T, Out>> {
    @Override
    public SingleTreeIndividual<T, Out> createIndividual() {
        return SingleTreeIndividual.of(initializer.createIndividual());
    }

    @Override
    public int populationSize() {
        return initializer.populationSize();
    }

    @Override
    public boolean shouldParallelize() {
        return true;
    }
}
