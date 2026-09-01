package gp.core.assessor;

import gp.Population;
import gp.core.fitness.Fitness;
import gp.core.individual.AssessedIndividual;
import gp.core.individual.Individual;

/**
 * Interface for evaluating a population of individuals.
 *
 * @param <R> The return type
 * @param <T> The terminal type
 * @param <I> The individual type
 * @param <F> The fitness type
 */
@FunctionalInterface
public interface Assessor<
        R, T,
        I extends Individual<R, T>,
        F extends Fitness<F>
> {
    /**
     * Evaluates all individuals in a population.
     *
     * @param population The population to evaluate
     * @return A new population of evaluated individuals
     */
    Population<AssessedIndividual<R, T, I, F>> assess(
            Population<I> population
    );
}

