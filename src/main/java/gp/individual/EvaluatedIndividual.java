package gp.individual;

import gp.fitness.Fitness;

/**
 * Record representing an individual with its evaluated fitness.
 * @param <T> The terminal type
 * @param <R> The return type
 * @param <I> The individual type
 * @param <F> The fitness type
 * @param individual The individual
 * @param fitness The evaluated fitness
 */
public record EvaluatedIndividual<
        T,
        R,
        I extends Individual<T, R>,
        F extends Fitness<F>
>(I individual, F fitness) implements Individual<T, R> {
    @Override
    public R evaluate(final T terminals) {
        return this.individual.evaluate(terminals);
    }
}

