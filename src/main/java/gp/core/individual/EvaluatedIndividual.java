package gp.core.individual;

import gp.core.fitness.Fitness;

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
        T, R,
        I extends Individual<T, R>,
        F extends Fitness<F>
>(I individual, F fitness) implements Individual<T, R> {
    public static <T, R, I extends Individual<T, R>, F extends Fitness<F>> EvaluatedIndividual<T, R, I, F> of(
        I i, F fitness
    ) {
        return new EvaluatedIndividual<>(i, fitness);
    }

    @Override
    public R evaluate(final T terminals) {
        return this.individual.evaluate(terminals);
    }
}

