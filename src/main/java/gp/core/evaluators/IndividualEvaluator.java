package gp.core.evaluators;

import gp.Population;
import utils.Parallelizeable;
import gp.core.fitness.Fitness;
import gp.core.individual.EvaluatedIndividual;
import gp.core.individual.Individual;

/**
 * An evaluator interface, which evaluates individuals one at a time.
 * @param <R> The return type
 * @param <T> The terminal type
 * @param <I> The individual type
 * @param <F> The fitness type
 */
@FunctionalInterface
public interface IndividualEvaluator<
        T, R,
        I extends Individual<T, R>,
        F extends Fitness<F>
> extends Evaluator<T, R, I, F>, Parallelizeable {
    /**
     * Evaluates a single individual.
     * @param individual The individual to evaluate
     * @return The evaluated individual with fitness
     */
    F evaluate(I individual);

    @Override
    default Population<EvaluatedIndividual<T, R, I, F>> evaluate(
            final Population<I> population
    ) {
        return Population.of(
                this.parallelize(
                        population.individuals().stream(),
                        i -> EvaluatedIndividual.of(i, evaluate(i))
                ).toList()
        );
    }

    @Override
    default boolean shouldParallelize() {
        return true;
    }
}
