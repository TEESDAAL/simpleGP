package gp.impl.selectors;

import gp.Population;
import gp.core.breeder.SimpleSelectionMechanism;
import gp.core.fitness.Comparer;
import gp.core.fitness.Fitness;
import gp.core.individual.AssessedIndividual;
import gp.core.individual.Individual;
import gp.core.selector.Sampler;
import gp.impl.selectors.random.RandomSampler;
import utils.Preconditions;
import utils.random.RandomSource;

import java.util.List;
import java.util.function.BinaryOperator;
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
    int tournamentSize,
    Fitness<F> fitnessComparator
) implements SimpleSelectionMechanism<AssessedIndividual<T, R, I, F>> {

    /**
     * Create a new Tournament selection with positive tournament size.
     * @param random the source of randomness
     * @param tournamentSize The desired tournament size, must be positive.
     */
    public TournamentSelection {
        Preconditions.assertTrue(
            tournamentSize > 0,
            "Tournament size must be greater than 0"
        );
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
        final int tournamentSize,
        Fitness<F> fitnessComparator
    ) {
        return new TournamentSelection<>(random, tournamentSize, fitnessComparator);
    }

    /**
     * Creates a sampler from the provided collection.
     *
     * @param individuals the individuals to select from
     * @return a sampler that performs tournament selection
     */
    @Override
    public Sampler<AssessedIndividual<T, R, I, F>> selectorFrom(
        final List<AssessedIndividual<T, R, I, F>> individuals
    ) {
        final Comparer<F> localComparer = fitnessComparator.fromPopulation(
            Population.of(individuals).map(AssessedIndividual::fitness)
        );
        if (individuals.isEmpty()) {
            throw new IllegalArgumentException("Must be able to select an individual.");
        }
        return () -> IntStream.range(0, tournamentSize)
            .mapToObj(ignored -> RandomSampler.sampleOrThrow(
                individuals, random
            )).reduce(this.selectBetter(localComparer))
            .orElseThrow();
    }

    private BinaryOperator<AssessedIndividual<T, R, I, F>> selectBetter(
        Comparer<F> comparer
    ) {
        return (i1, i2) -> selectBetter(i1, i2, comparer);
    }

    private AssessedIndividual<T, R, I, F> selectBetter(
        AssessedIndividual<T, R, I, F> bestSoFar,
        AssessedIndividual<T, R, I, F> challenger,
        Comparer<F> fitnessComparator
    ) {
        return switch (fitnessComparator.compareWith(
            challenger.fitness(),
            bestSoFar.fitness()
        )) {
            case BETTER -> challenger;
            case EQUAL, WORSE -> bestSoFar;
        };
    }
}
