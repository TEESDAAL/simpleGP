package example.function_approximation.parameters.initial;

import example.function_approximation.parameters.FunctionApproximationParameters;
import gp.core.breeder.Breeder;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.AssessedIndividual;
import gp.core.initializer.Initialiser;
import gp.core.statistic.Statistic;
import gp.impl.assessor.SingleObjectiveAssessor;
import gp.impl.individual.SingleTreeIndividual;
import utils.Pair;


public class Parameters implements FunctionApproximationParameters<
        Pair<Double, Double>, Double,
        SingleTreeIndividual<Pair<Double, Double>, Double>,
        SingleObjectiveAssessor<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>>
> {

    @Override
    public Initialiser<SingleTreeIndividual<Pair<Double, Double>, Double>> initializer() {
        return null;
    }

    @Override
    public Breeder<AssessedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>, SingleTreeIndividual<Pair<Double, Double>, Double>> breeder() {
        return null;
    }

    @Override
    public SingleObjectiveAssessor<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>> trainEvaluator() {
        return null;
    }

    @Override
    public SingleObjectiveAssessor<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>> testEvaluator() {
        return null;
    }

    @Override
    public Statistic<AssessedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>, ?> scoreLogger() {
        return null;
    }

    @Override
    public int numGenerations() {
        return 0;
    }
}

