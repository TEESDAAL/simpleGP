package gp.core.individual;

import gp.core.fitness.Fitness;

/**
 * An individual with an evaluated fitness value.
 *
 * @param <T> the terminal type
 * @param <R> the return type
 * @param <I> the individual type
 * @param <F> the fitness type
 */
public interface EvaluatedIndividual<
        T, R,
        I extends Individual<T, R>,
        F extends Fitness<F>
> extends Individual<T, R> {
    /**
     * Creates an evaluated individual wrapper.
     *
     * @param individual the wrapped individual
     * @param fitness the evaluated fitness
     * @param <T> the terminal type
     * @param <R> the return type
     * @param <I> the individual type
     * @param <F> the fitness type
     * @return an evaluated individual
     */
    static <T, R, I extends Individual<T, R>, F extends Fitness<F>>
            EvaluatedIndividual<T, R, I, F> of(
            final I individual,
            final F fitness
    ) {
        return new EvaluatedIndividualImpl<>(individual, fitness);
    }

    /**
     * Gets the wrapped individual.
     *
     * @return the wrapped individual
     */
    I individual();

    /**
     * Gets the evaluated fitness.
     *
     * @return the evaluated fitness
     */
    F fitness();

    /**
     * Evaluates the wrapped individual.
     *
     * @param terminals the terminal input
     * @return the evaluation result
     */
    default R evaluate(final T terminals) {
        return this.individual().evaluate(terminals);
    }
}


/**
 * Record representing an individual with its evaluated fitness.
 *
 * @param <T> the terminal type
 * @param <R> the return type
 * @param <I> the individual type
 * @param <F> the fitness type
 * @param individual the individual
 * @param fitness the evaluated fitness
 */
record EvaluatedIndividualImpl<
    T, R,
    I extends Individual<T, R>,
    F extends Fitness<F>
>(I individual, F fitness) implements EvaluatedIndividual<T, R, I, F> {
    @Override
    public R evaluate(final T terminals) {
        return this.individual.evaluate(terminals);
    }
}

