package gp.impl.selectors;

import gp.core.breeder.SimpleSelectionMechanism;
import gp.core.fitness.Fitness;
import gp.core.individual.EvaluatedIndividual;
import gp.core.individual.Individual;
import gp.core.selectors.Sampler;
import gp.impl.selectors.random.RandomSampler;
import utils.random.RandomSource;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * A class that creates a tournament selector of a given size.
 *
 * @param <R> The return type
 * @param <T> The terminal type
 * @param <I> The individual type
 * @param <F> The fitness type
 * @param random The random number generator
 * @param tournamentSize The number of individuals in each tournament
 */
public record TournamentSelection<
        R, T, I extends Individual<T, R>, F extends Fitness<F>
>(
        RandomSource random,
        int tournamentSize
) implements SimpleSelectionMechanism<EvaluatedIndividual<T, R, I, F>> {
    /**
     * Compact constructor validating tournament size.
     *
     * @param random the random number generator
     * @param tournamentSize the tournament size
     */
    public TournamentSelection {
        if (tournamentSize <= 0) {
            throw new IllegalArgumentException(
                    "tournament size must be greater than 0.");
        }
    }

    /**
     * Creates a new tournament selection.
     *
     * @param random the source of randomness used to select individuals
     * @param tournamentSize the tournament size
     * @param <R> the return type of an individual
     * @param <T> the terminal/input type of the individuals
     * @param <I> the individual type
     * @param <F> the fitness type
     * @return a new tournament selection
     */
        public static <R, T, I extends Individual<T, R>, F extends Fitness<F>>
        TournamentSelection<R, T, I, F> of(
            final RandomSource random,
            final int tournamentSize
    ) {
        return new TournamentSelection<>(random, tournamentSize);
    }

    /**
     * Creates a sampler from the provided collection.
     *
     * @param items the items to select from
     * @return a sampler that performs tournament selection
     */
    @Override
    public Sampler<EvaluatedIndividual<T, R, I, F>> selectorFrom(
            final Collection<EvaluatedIndividual<T, R, I, F>> items
    ) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Must be able to select an individual.");
        }
        return () -> IntStream.range(0, tournamentSize)
                .mapToObj(ignored -> RandomSampler.sample(
                        items, random
                ).orElseThrow())
                .reduce((bestIndividual, currentIndividual) ->
                        switch (currentIndividual.fitness().compareWith(
                                bestIndividual.fitness()
                        )) {
                            case BETTER -> currentIndividual;
                            case EQUAL, WORSE -> bestIndividual;
                        }
                ).orElseThrow();
    }
}
