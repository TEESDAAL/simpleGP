package gp.impl.evaluators;

import gp.core.evaluators.IndividualEvaluator;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.Individual;
import gp.impl.fitness.MultiObjectiveFit;
import gp.impl.fitness.SingleObjectiveFit;

import java.util.List;
import java.util.function.Function;

/**
 * An evaluator for single-objective optimization on single tree individuals.
 * @param <T> The terminal type
 * @param evaluator The function that evaluates individuals to produce fitness
 */
public record MultiObjectiveEvaluator<T, R, I extends Individual<T, R>>(
        Function<I, MultiObjectiveFit> evaluator
) implements IndividualEvaluator<T, R, I, MultiObjectiveFit> {

    public static  <T, R, I extends Individual<T, R>> MultiObjectiveEvaluator<
        T, R, I
    > of(
        List<Function<I, SingleObjectiveFit>> evaluators
    ) {
        return new MultiObjectiveEvaluator<>(
                ind -> MultiObjectiveFit.of(
                        evaluators.stream()
                                .map(ev -> (SingleObjectiveFitness) ev.apply(ind))
                                .toList()
                )
        );
    }

    @Override
    public MultiObjectiveFit evaluate(I individual) {
        return evaluator.apply(individual);
    }
}
