package example.function_approximation.parameters;

import gp.core.breeder.Breeder;
import gp.core.evaluators.Evaluator;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.EvaluatedIndividual;
import gp.core.individual.Individual;
import gp.core.initializers.Initialiser;
import gp.core.statistics.Statistic;

import java.io.Serializable;

/// GP params for performing function approximation.
/// All individuals take a (x, y) input and return a double
public interface FunctionApproximationParameters<
    X, Y,
    Ind extends Individual<X, Y>,
    E extends Evaluator<X, Y, Ind, SingleObjectiveFitness>
> extends Serializable {
    Initialiser<Ind> initializer();
    Breeder<EvaluatedIndividual<X, Y, Ind, SingleObjectiveFitness>, Ind> breeder();

    E trainEvaluator();

    E testEvaluator();

    Statistic<EvaluatedIndividual<X, Y, Ind, SingleObjectiveFitness>, ?> scoreLogger();

    int numGenerations();
}


