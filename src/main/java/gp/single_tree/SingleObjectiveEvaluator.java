package gp.single_tree;

import gp.evaluators.IndividualEvaluator;
import gp.fitness.Goal;
import gp.individual.EvaluatedIndividual;

import java.util.function.Function;

public record SingleObjectiveEvaluator<T>(
        Function<SingleTreeIndividual<T, Double>, SingleObjectiveFitness> evaluator
) implements IndividualEvaluator<T, Double, SingleTreeIndividual<T, Double>, SingleObjectiveFitness> {
    public static <T, I> SingleObjectiveEvaluator<T> of(Function<SingleTreeIndividual<T, Double>, Double> evaluator, Goal goal) {
        return new SingleObjectiveEvaluator<>(
                ind -> new SingleObjectiveFitness(evaluator.apply(ind), goal)
        );
    }

    @Override
    public EvaluatedIndividual<T, Double, SingleTreeIndividual<T, Double>, SingleObjectiveFitness> evaluate(
            SingleTreeIndividual<T, Double> individual
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
