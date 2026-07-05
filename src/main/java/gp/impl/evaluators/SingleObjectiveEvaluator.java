package gp.impl.evaluators;

import gp.core.evaluators.IndividualEvaluator;
import gp.core.fitness.Goal;
import gp.core.individual.Individual;
import gp.impl.fitness.SingleObjectiveFit;
import gp.core.fitness.SingleObjectiveFitness;
import gp.impl.individual.SingleTreeIndividual;

import java.util.function.Function;

/**
 * An evaluator for single-objective optimization on single tree individuals.
 * @param <T> The terminal type
 * @param evaluator The function that evaluates individuals to produce fitness
 */
public record SingleObjectiveEvaluator<T, R, I extends Individual<T, R>>(
        Function<I, SingleObjectiveFitness> evaluator
) implements IndividualEvaluator<
        T, R, I,
        SingleObjectiveFitness
> {
    /**
     * Creates a single objective evaluator from an evaluation
     * function and goal.
     * @param <T> The terminal type
     * @param evaluator The function that evaluates individuals to
     *     produce scores
     * @param goal The optimization goal
     * @return A new single objective evaluator
     */
    public static <T, R> SingleObjectiveEvaluator<
        T, R, SingleTreeIndividual<T, R>
    > of(
        Function<SingleTreeIndividual<T, R>, Double> evaluator,
        Goal goal
    ) {
        return new SingleObjectiveEvaluator<>(
                ind -> SingleObjectiveFit.of(evaluator.apply(ind), goal)
        );
    }

    @Override
    public SingleObjectiveFitness evaluate(I individual) {
        return evaluator.apply(individual);
    }
}
