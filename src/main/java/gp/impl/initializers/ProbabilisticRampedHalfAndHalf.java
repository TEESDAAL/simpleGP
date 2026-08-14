package gp.impl.initializers;

import gp.core.initializer.IndividualInitialiser;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.selectors.random.RandomSampler;
import utils.random.RandomSource;

import java.util.List;
import java.util.function.IntFunction;

/**
 * An Initializer that tends towards true ramped half-and-half.
 * Uniformly picks a random depth between [2, maxDepth], and either grow or full to build the tree with that depth.
 * @param <T> The terminal set/input of the created individuals.
 * @param <Out> The output of the created individuals
 */
public class ProbabilisticRampedHalfAndHalf<T, Out> implements IndividualInitialiser<SingleTreeIndividual<T, Out>> {
    final int maxDepth;
    final RandomSource random;
    private final IntFunction<
            IndividualInitialiser<SingleTreeIndividual<T, Out>>
    > grow;

    private final IntFunction<
            IndividualInitialiser<SingleTreeIndividual<T, Out>>
    > full;
    
    public ProbabilisticRampedHalfAndHalf(
            int maxDepth, RandomSource random,
            IntFunction<IndividualInitialiser<SingleTreeIndividual<T, Out>>> grow,
            IntFunction<IndividualInitialiser<SingleTreeIndividual<T, Out>>> full
    ) {
        this.maxDepth = maxDepth;
        this.random = random;
        this.grow = grow;
        this.full = full;
    }

    @Override
    public SingleTreeIndividual<T, Out> createIndividual() {
        final int depth = random.nextInt(2, maxDepth+1);
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
