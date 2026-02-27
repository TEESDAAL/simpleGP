package gp.evaluators;

import gp.Population;
import gp.breeder.Parallelizeable;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;

/**
 * An evaluator interface, which evaluates individuals one at a time.
 * @param <R> The return type
 * @param <T> The terminal type
 * @param <I> The individual type
 * @param <F> The fitness type
 */
public interface IndividualEvaluator<
        R,
        T,
        I extends Individual<R, T>,
        F extends Fitness<F>
>
        extends Evaluator<R, T, I, F>, Parallelizeable {
    /**
     * Evaluates a single individual.
     * @param individual The individual to evaluate
     * @return The evaluated individual with fitness
     */
    EvaluatedIndividual<R, T, I, F> evaluate(I individual);

    @Override
    default Population<EvaluatedIndividual<R, T, I, F>> evaluate(
            final Population<I> population
    ) {
        return Population.of(
                this.parallelize(
                        population.individuals().stream(),
                        this::evaluate
                ).toList()
        );
    }
}
