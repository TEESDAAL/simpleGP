package gp.evaluators;

import gp.Population;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;

/**
 * Interface for evaluating a population of individuals.
 * @param <R> The return type
 * @param <T> The terminal type
 * @param <I> The individual type
 * @param <F> The fitness type
 */
public interface Evaluator<
        R,
        T,
        I extends Individual<R, T>,
        F extends Fitness<F>
> {
    /**
     * Evaluates all individuals in a population.
     * @param population The population to evaluate
     * @return A new population of evaluated individuals
     */
    Population<EvaluatedIndividual<R, T, I, F>> evaluate(
            Population<I> population
    );
}

