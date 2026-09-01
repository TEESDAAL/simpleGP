package gp.impl.initializer;

import gp.core.initializer.IndividualInitialiser;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.selector.random.RandomSampler;
import util.random.RandomSource;

import java.util.List;
import java.util.function.IntFunction;


/**
 * An Initializer that tends towards true ramped half-and-half.
 * Uniformly picks a random depth between [2, maxDepth],
 * and either grow or full to build the tree with that depth.
 *
 * @param maxDepth The maximum depth of the trees.
 * @param random   The source of randomness.
 * @param grow     The function which will create a grow initializer for a given depth.
 * @param full     The function which will create a full initializer for a given depth.
 * @param <T>      The terminal set/input of the created individuals.
 * @param <Out>    The output of the created individuals
 */
public record ProbabilisticRampedHalfAndHalf<T, Out>(
        int maxDepth, RandomSource random,
        IntFunction<IndividualInitialiser<SingleTreeIndividual<T, Out>>> grow,
        IntFunction<IndividualInitialiser<SingleTreeIndividual<T, Out>>> full
) implements IndividualInitialiser<SingleTreeIndividual<T, Out>> {

    @Override
    public SingleTreeIndividual<T, Out> createIndividual() {
        final int depth = random.nextInt(2, maxDepth + 1);
        return RandomSampler.sampleOrThrow(List.of(full, grow), random)
                .apply(depth)
                .createIndividual();
    }

    @Override
    public int populationSize() {
        return 0;
    }

    @Override
    public boolean shouldParallelize() {
        return false;
    }
}
