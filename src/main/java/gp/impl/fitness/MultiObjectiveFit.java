package gp.impl.fitness;

import gp.core.fitness.MultiObjectiveFitness;
import gp.core.fitness.SingleObjectiveFitness;

import java.util.Collections;
import java.util.List;

/**
 * Record representing multi-objective fitness with multiple
 * single-objective scores.
 * @param fitnesses The list of single-objective fitness values
 */
public record MultiObjectiveFit(
        List<SingleObjectiveFitness> fitnesses
) implements MultiObjectiveFitness<MultiObjectiveFit> {
        /**
        * Factory method to create a MultiObjectiveFit
        *      from a list of SingleObjectiveFitnesses.
        * @param fitnesses The list of single-objective fitness values
        * @return A MultiObjectiveFit instance
        */
        public static MultiObjectiveFit of(List<SingleObjectiveFitness> fitnesses) {
            return new MultiObjectiveFit(
                    Collections.unmodifiableList(fitnesses)
            );
        }
}
