package gp.impl.breeder;

import gp.Population;
import gp.core.breeder.Breeder;
import gp.core.breeder.SimpleSelectionMechanism;
import gp.core.fitness.Fitness;
import gp.core.individual.EvaluatedIndividual;
import gp.core.individual.Individual;
import gp.impl.selectors.Elitism;
import utils.Preconditions;
import utils.operators.Operator;
import gp.core.selectors.Sampler;

import java.util.ArrayList;
import java.util.List;


/**
 * A breeder that samples operators from a distribution and applies
 * them to individuals selected from the population.
 * @param distribution A distribution over operators to sample from.
 * @param newPopulationSize The desired size of the next generation.
 * @param selectionMechanism The method to select individuals from the
 *                           population for breeding.
 * @param elitism How elitism is applied to the population.
 * @param <T> The terminal type of the individuals.
 * @param <R> The final return type of the individuals.
 * @param <I> The type of individuals being bred.
 * @param <F> The fitness type used to evaluate individuals.
 */
public record NaiveBreeder<
        T, R, I extends Individual<T, R>, F extends Fitness<F>
>(
        Sampler<Operator<I, List<I>>> distribution,
        int newPopulationSize,
        SimpleSelectionMechanism<EvaluatedIndividual<T, R, I, F>> selectionMechanism,
        Elitism<T, R, I, F> elitism
) implements Breeder<EvaluatedIndividual<T, R, I, F>, I> {
    /**
     * Compact constructor to validate parameters.
     *
     * @throws IllegalArgumentException if newPopulationSize is not positive
     * if elitesToPreserve is negative or if elitesToPreserve exceeds newPopulationSize.
     */
    public NaiveBreeder {
        Preconditions.assertTrue(
                newPopulationSize >= 0,
                "Desired size must be positive, got: " + newPopulationSize
        );
        Preconditions.assertTrue(
                selectionMechanism != null,
                "Selection mechanism cannot be null"
        );
    }


    @Override
    public Population<I> breed(
            final Population<EvaluatedIndividual<T, R, I, F>> population
    ) {
        Sampler<EvaluatedIndividual<T, R, I, F>> sampler = selectionMechanism
                .selectorFrom(population.individuals());

        List<I> nextGeneration = new ArrayList<>(this.newPopulationSize);

        addElites(nextGeneration, population);

        while (nextGeneration.size() < this.newPopulationSize) {
            Operator<I, List<I>> operator = this.distribution.sample();
            nextGeneration.addAll(operator.sampleFrom(
                sampler,
                    EvaluatedIndividual::individual
            ));
        }

        if (nextGeneration.size() > this.newPopulationSize) {
            nextGeneration = nextGeneration.subList(0, this.newPopulationSize);
        }

        return Population.of(
                nextGeneration
        );
    }

    private void addElites(
            List<I> nextGeneration,
            Population<EvaluatedIndividual<T, R, I, F>> population
    ) {
        nextGeneration.addAll(
                this.elitism.selectorFrom(population.individuals()).sample()
                        .stream()
                        .map(EvaluatedIndividual::individual)
                        .toList()
        );
    }
}
