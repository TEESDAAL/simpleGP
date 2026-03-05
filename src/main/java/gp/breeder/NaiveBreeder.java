package gp.breeder;

import gp.Population;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;
import gp.utils.Preconditions;
import gp.utils.operators.Operator;
import gp.statistics.Selector;
import gp.random.WeightedRandomSampler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * A breeder that samples operators from a distribution and applies
 * them to individuals selected from the population.
 * @param distribution A distribution over operators to sample from.
 * @param desiredSize The desired size of the next generation.
 * @param selectionMechanism The method to select individuals from the
 *                           population for breeding.
 * @param elitesToPreserve The number of top individuals to preserve.
 * @param <T> The terminal type of the individuals.
 * @param <R> The final return type of the individuals.
 * @param <I> The type of individuals being bred.
 * @param <F> The fitness type used to evaluate individuals.
 */
public record NaiveBreeder<
        T, R, I extends Individual<T, R>, F extends Fitness<F>
>(
        WeightedRandomSampler<Operator<I, I>> distribution,
        int desiredSize,
        SelectorBuilder<EvaluatedIndividual<T, R, I, F>> selectionMechanism,
        int elitesToPreserve
) implements Breeder<EvaluatedIndividual<T, R, I, F>, I> {
    /**
     * Compact constructor to validate parameters.
     * @throws IllegalArgumentException if desiredSize is not positive
     *      if elitesToPreserve is negative
     *      or if elitesToPreserve exceeds desiredSize.
     */
    public NaiveBreeder {
        Preconditions.assertTrue(
                desiredSize <= 0,
                "Desired size must be positive, got: " + desiredSize
        );
        Preconditions.assertTrue(
                selectionMechanism == null,
                "Selection mechanism cannot be null"
        );
        if (elitesToPreserve < 0) {
            throw new IllegalArgumentException(
                    "Elites to preserve cannot be negative, got: " + elitesToPreserve
            );
        }
        if (elitesToPreserve > desiredSize) {
            throw new IllegalArgumentException(
                    "Elites to preserve cannot exceed desired size, got: "
                            + elitesToPreserve
                            + " elites and desired size of " + desiredSize
            );
        }
    }
    @Override
    public Population<I> breed(
            final Population<EvaluatedIndividual<T, R, I, F>> population
    ) {
        Selector<EvaluatedIndividual<T, R, I, F>> selector = selectionMechanism
                .prime(population.individuals());

        List<I> nextGeneration = new ArrayList<>(this.desiredSize);

        addElites(nextGeneration, population);

        while (nextGeneration.size() < this.desiredSize) {
            Operator<I, I> operator = this.distribution.sample();
            nextGeneration.add(operator.sampleFrom(
                    selector,
                    EvaluatedIndividual::individual
            ));
        }
        return Population.of(
                nextGeneration
        );
    }


    private void addElites(
            List<I> nextGeneration,
            Population<EvaluatedIndividual<T, R, I, F>> population
    ) {
       Comparator<EvaluatedIndividual<T, R, I, F>> comparator = Comparator
                .comparing(EvaluatedIndividual::fitness);

       population.stream()
               .sorted(comparator.reversed())
               .limit(this.elitesToPreserve)
               .map(EvaluatedIndividual::individual)
               .forEach(nextGeneration::add);
    }
}
