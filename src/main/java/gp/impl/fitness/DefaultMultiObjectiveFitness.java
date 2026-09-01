package gp.impl.fitness;

import gp.Population;
import gp.core.fitness.Comparer;
import gp.core.fitness.MultiObjectiveFitness;
import gp.core.fitness.SingleObjectiveFitness;

import java.util.Collections;
import java.util.List;

/**
 * Record representing multi-objective fitness with multiple
 * single-objective scores.
 *
 * @param fitnesses The list of single-objective fitness values
 */
public record DefaultMultiObjectiveFitness(
        List<SingleObjectiveFitness> fitnesses
) implements MultiObjectiveFitness<DefaultMultiObjectiveFitness> {
    /**
     * Factory method to create a MultiObjectiveFit
     * from a list of SingleObjectiveFitnesses.
     *
     * @param fitnesses The list of single-objective fitness values
     * @return A MultiObjectiveFit instance
     */
    public static DefaultMultiObjectiveFitness of(
            List<SingleObjectiveFitness> fitnesses
    ) {
        return new DefaultMultiObjectiveFitness(
                Collections.unmodifiableList(fitnesses)
        );
    }

    @Override
    public Comparer<DefaultMultiObjectiveFitness> fromPopulation(
            Population<DefaultMultiObjectiveFitness> fitnesses
    ) {
        return Comparer.of(MultiObjectiveFitness.ordering(
                fitnesses.individuals(),
                f -> f
        ));
    }
}
