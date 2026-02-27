package gp.selectors;

import gp.breeder.SelectorBuilder;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;
import gp.random.RandomSampler;
import gp.statistics.Selector;

import java.util.Collection;
import java.util.Random;
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
        Random random,
        int tournamentSize
) implements SelectorBuilder<EvaluatedIndividual<T, R, I, F>> {
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

    @Override
    public Selector<EvaluatedIndividual<T, R, I, F>> prime(
            final Collection<EvaluatedIndividual<T, R, I, F>> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Must be able to select an individual.");
        }
        return () -> IntStream.range(0, tournamentSize)
                    .mapToObj(ignored -> RandomSampler.sample(
                            items, random).orElseThrow())
                    .reduce((bestIndividual, currentIndividual)
                            -> switch (currentIndividual.fitness()
                                    .compareWith(bestIndividual.fitness())) {
                            case BETTER -> currentIndividual;
                            case EQUAL, WORSE -> bestIndividual;
                        }).orElseThrow();
    }
}
