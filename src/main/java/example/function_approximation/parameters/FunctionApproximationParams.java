package example.function_approximation.parameters;

import gp.breeder.Breeder;
import gp.breeder.Initializer;
import gp.individual.EvaluatedIndividual;
import gp.single_tree.SingleObjectiveEvaluator;
import gp.single_tree.SingleObjectiveFitness;
import gp.single_tree.SingleTreeIndividual;
import gp.statistics.Statistic;

/**
 * The base-parameters for a Function Approximation GP run.
 * @param <T> The Terminal type for the model.
 */
public interface FunctionApproximationParams<T> {

    /**
     * @return The basic GP initializer, sets up the population.
     */
    Initializer<SingleTreeIndividual<T, Double>> initializer();


    /**
     * @return The GP breeder.
     */
    Breeder<
            EvaluatedIndividual<T, Double, SingleTreeIndividual<T, Double>,
                    SingleObjectiveFitness
            >, SingleTreeIndividual<T, Double>
    > breeder();


    /**
     * @return The training evaluator.
     */
    SingleObjectiveEvaluator<T> trainEvaluator();

    /**
     * @return The testing evaluator.
     */
    SingleObjectiveEvaluator<T> testEvaluator();

    /**
     * @return The side effect that logs the score of the individual.
     */
    Statistic<
            EvaluatedIndividual<
                    T, Double,
                    SingleTreeIndividual<T, Double>,
                    SingleObjectiveFitness
            >
    > scoreLogger();
}


