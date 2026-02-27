package gp.single_tree;

import gp.evaluators.IndividualEvaluator;
import gp.fitness.Goal;
import gp.individual.EvaluatedIndividual;

import java.util.function.Function;

/**
 * An evaluator for single-objective optimization on single tree individuals.
 * @param <T> The terminal type
 * @param evaluator The function that evaluates individuals to produce fitness
 */
public record SingleObjectiveEvaluator<T>(
        Function<
                SingleTreeIndividual<T, Double>,
                SingleObjectiveFitness
        > evaluator
) implements IndividualEvaluator<
        T,
        Double,
        SingleTreeIndividual<T, Double>,
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
    public static <T> SingleObjectiveEvaluator<T> of(
            final Function<
                    SingleTreeIndividual<T, Double>,
                    Double
            > evaluator,
            final Goal goal
    ) {
        return new SingleObjectiveEvaluator<>(
                ind -> new SingleObjectiveFitness(evaluator.apply(ind), goal)
        );
    }

    @Override
    public EvaluatedIndividual<
            T,
            Double,
            SingleTreeIndividual<T, Double>,
            SingleObjectiveFitness
    > evaluate(
            final SingleTreeIndividual<T, Double> individual
    ) {
        return new EvaluatedIndividual<>(
                individual,
                evaluator.apply(individual)
        );
    }

    @Override
    public boolean shouldParallelize() {
        return true;
    }
}
