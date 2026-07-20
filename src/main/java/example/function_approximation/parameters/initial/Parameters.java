package example.function_approximation.parameters.initial;

import example.function_approximation.parameters.FunctionApproximationParameters;
import gp.core.breeder.Breeder;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.EvaluatedIndividual;
import gp.core.initializers.Initialiser;
import gp.core.statistics.Statistic;
import gp.impl.evaluators.SingleObjectiveEvaluator;
import gp.impl.individual.SingleTreeIndividual;
import utils.Pair;


public class Parameters implements FunctionApproximationParameters<
        Pair<Double, Double>, Double,
        SingleTreeIndividual<Pair<Double, Double>, Double>,
        SingleObjectiveEvaluator<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>>
> {

    @Override
    public Initialiser<SingleTreeIndividual<Pair<Double, Double>, Double>> initializer() {
        return null;
    }

    @Override
    public Breeder<EvaluatedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>, SingleTreeIndividual<Pair<Double, Double>, Double>> breeder() {
        return null;
    }

    @Override
    public SingleObjectiveEvaluator<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>> trainEvaluator() {
        return null;
    }

    @Override
    public SingleObjectiveEvaluator<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>> testEvaluator() {
        return null;
    }

    @Override
    public Statistic<EvaluatedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>, ?> scoreLogger() {
        return null;
    }

    @Override
    public int numGenerations() {
        return 0;
    }
}

