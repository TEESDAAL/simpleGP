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
 * @param <R> The return type
 * @param <T> The terminal type
 * @param <I> The individual type
 * @param <F> The fitness type
 * @param random The random number generator
 * @param tournamentSize The number of individuals in each tournament
 */
public record TournamentSelection<R, T,
        I extends Individual<T, R>, F extends Fitness<F>>(
        RandomSource random,
        int tournamentSize
) implements SimpleSelectionMechanism<EvaluatedIndividual<T, R, I, F>> {
    /**
     * Compact constructor validating tournament size.
     * @param random The random number generator
     * @param tournamentSize The tournament size
     */
    public TournamentSelection {
        if (tournamentSize <= 0) {
            throw new IllegalArgumentException(
                    "tournament size must be greater than 0.");
        }
    }

    /**
     * Create a new TournamentSelection
     * @param random The source of randomness used to select individuals from the tournament.
     * @param tournamentSize The tournament size.
     * @return A new tournament selection.
     * @param <R> The return type of an individual
     * @param <T> The terminal/input type of the individuals.
     * @param <I> The individual
     * @param <F> The fitness that is compared by.
     */
    public static <
        R, T,
        I extends Individual<T, R>,
        F extends Fitness<F>
    > TournamentSelection<R, T, I, F> of(RandomSource random, int tournamentSize) {
        return new TournamentSelection<>(random, tournamentSize);
    }

    @Override
    public Sampler<EvaluatedIndividual<T, R, I, F>> selectorFrom(
            final Collection<EvaluatedIndividual<T, R, I, F>> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Must be able to select an individual.");
        }
        return () -> IntStream.range(0, tournamentSize)
            .mapToObj(ignored -> RandomSampler.sample(
                items, random
            ).orElseThrow())
            .reduce((bestIndividual, currentIndividual) ->
                switch (currentIndividual.fitness().compareWith(bestIndividual.fitness())) {
                    case BETTER -> currentIndividual;
                    case EQUAL, WORSE -> bestIndividual;
                }
            ).orElseThrow();
    }
}
