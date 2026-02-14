package example.function_approximation.parameters;

import gp.breeder.Breeder;
import gp.breeder.Initializer;
import gp.individual.EvaluatedIndividual;
import gp.single_tree.SingleObjectiveEvaluator;
import gp.single_tree.SingleObjectiveFitness;
import gp.single_tree.SingleTreeIndividual;
import gp.statistics.Statistic;

public interface FunctionApproximationParams<T> {

    Initializer<SingleTreeIndividual<T, Double>> initializer();

    SingleObjectiveEvaluator<T> trainEvaluator();

    Breeder<EvaluatedIndividual<T, Double, SingleTreeIndividual<T, Double>, SingleObjectiveFitness>, SingleTreeIndividual<T, Double>> breeder();

    SingleObjectiveEvaluator<T> testEvaluator();

    Statistic<EvaluatedIndividual<T, Double, SingleTreeIndividual<T, Double>, SingleObjectiveFitness>> scoreLogger();
}


