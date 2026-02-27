package gp.single_tree;

import gp.initializers.BaseInitializer;
import gp.utils.IndividualInitializer;

/**
 * An initializer for single tree individuals that wraps a base initializer.
 * @param <T> The terminal type
 * @param <Out> The output type
 * @param initializer The base tree initializer
 */
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
