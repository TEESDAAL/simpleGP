package gp.impl.initializers;

import gp.Population;
import gp.core.initializers.IndividualInitialiser;
import gp.core.initializers.Initialiser;
import utils.Preconditions;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * A utility class for creating population initializers from a seed population.
 */
public final class SeededInitializer {
    private SeededInitializer() {}
    /**
     * Create population initializer from a seed population,
     *  padded by an individual initializer.
     * @param desiredPopulationSize The size of the new population.
     * @param seed The initial seed population.
     * @param initialiser The initialiser to flesh out the full population.
     * @return An initializer.
     * @param <I> The individual type.
     */
    public static <I> Initialiser<I> fromSeed(
        int desiredPopulationSize,
        Population<I> seed,
        IndividualInitialiser<I> initialiser
    ) {
        return () -> {
            final int neededIndividuals = desiredPopulationSize - seed.size();
            if (neededIndividuals <= 0) {
                return seed.subPopulation(0, desiredPopulationSize);
            }
            return seed.combine(IntStream.range(0, desiredPopulationSize)
                .mapToObj(_ -> initialiser.createIndividual())
                .collect(Population.toPopulation()));
        };
    }

    /**
     * Create population initializer from a seed population,
     *  padded by an initializer that produces a specific number of individuals.
     * @param desiredPopulationSize The size of the new population.
     * @param seed The initial seed population.
     * @param initialiserFromDesiredSize A way to construct an initializer that
     *                                   produces n individuals.
     * @return An initializer.
     * @param <I> The individual type.
     */
    public static <I> Initialiser<I> fromSeed(
        int desiredPopulationSize,
        Population<I> seed,
        IntFunction<Initialiser<I>> initialiserFromDesiredSize
    ) {
        return () -> {
            final int neededIndividuals = desiredPopulationSize - seed.size();
            if (neededIndividuals <= 0) {
                return seed.subPopulation(0, desiredPopulationSize);
            }
            final Population<I> remainingPop = Preconditions.assertTrue(
                initialiserFromDesiredSize.apply(desiredPopulationSize).initialize(),
                p -> p.size() == neededIndividuals,
                p -> "Intializer didn't produce correct number "
                    + "of individuals, expected " + neededIndividuals
                    + ", but got " + p.size()
            );

            return seed.combine(remainingPop);
        };
    }
}
