package example.function_approximation.parameters;

import gp.core.breeder.Breeder;
import gp.core.assessor.Assessor;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.AssessedIndividual;
import gp.core.individual.Individual;
import gp.core.initializer.Initialiser;
import gp.core.statistic.Statistic;

import java.io.Serializable;

/// GP params for performing function approximation.
/// All individuals take a (x, y) input and return a double
public interface FunctionApproximationParameters<
    X, Y,
    Ind extends Individual<X, Y>,
    E extends Assessor<X, Y, Ind, SingleObjectiveFitness>
> extends Serializable {
    Initialiser<Ind> initializer();
    Breeder<AssessedIndividual<X, Y, Ind, SingleObjectiveFitness>, Ind> breeder();

    E trainEvaluator();

    E testEvaluator();

    Statistic<AssessedIndividual<X, Y, Ind, SingleObjectiveFitness>, ?> scoreLogger();

    int numGenerations();
}


