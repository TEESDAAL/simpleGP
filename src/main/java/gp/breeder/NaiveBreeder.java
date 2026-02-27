package gp.breeder;

import gp.Population;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;
import gp.utils.Operator;
import gp.statistics.Selector;
import gp.random.WeightedRandomSampler;

import java.util.ArrayList;
import java.util.List;


/**
 * A breeder that samples operators from a distribution and applies
 * them to individuals selected from the population.
 * @param distribution A distribution over operators to sample from.
 * @param desiredSize The desired size of the next generation.
 * @param selectionMechanism The method to select individuals from the
 *                           population for breeding.
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
        SelectorBuilder<EvaluatedIndividual<T, R, I, F>> selectionMechanism
) implements Breeder<EvaluatedIndividual<T, R, I, F>, I> {

    @Override
    public Population<I> breed(
            final Population<EvaluatedIndividual<T, R, I, F>> population
    ) {
        Selector<EvaluatedIndividual<T, R, I, F>> selector = selectionMechanism
                .prime(population.individuals());

        List<I> nextGeneration = new ArrayList<>(this.desiredSize);
        for (int i = 0; i < this.desiredSize; i++) {
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
}
