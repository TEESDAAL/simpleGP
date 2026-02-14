package example.function_approximation;

import example.function_approximation.parameters.FunctionApproximationParams;
import example.function_approximation.parameters.UnaryFunctionApproximator;
import gp.GPPipeLine;
import gp.Population;
import gp.TerminationCriterion;
import gp.breeder.Breeder;
import gp.breeder.Initializer;
import gp.single_tree.SingleObjectiveEvaluator;
import gp.single_tree.SingleObjectiveFitness;
import gp.individual.EvaluatedIndividual;
import gp.single_tree.SingleTreeIndividual;
import gp.statistics.SideEffect;
import gp.statistics.Statistic;

import java.lang.reflect.InvocationTargetException;




public class FunctionApproximator {
    final Initializer<SingleTreeIndividual<Double, Double>> initializer;
    final SingleObjectiveEvaluator<Double> trainEvaluator;
    final Breeder<
            EvaluatedIndividual<Double, Double, SingleTreeIndividual<Double, Double>, SingleObjectiveFitness>,
            SingleTreeIndividual<Double, Double>> breeder;
    final SingleObjectiveEvaluator<Double> testEvaluator;
    final Statistic<EvaluatedIndividual<Double, Double, SingleTreeIndividual<Double, Double>, SingleObjectiveFitness>> scoreLogger;

    public FunctionApproximator(Initializer<SingleTreeIndividual<Double, Double>> initializer, SingleObjectiveEvaluator<Double> trainEvaluator, Breeder<EvaluatedIndividual<Double, Double, SingleTreeIndividual<Double, Double>, SingleObjectiveFitness>, SingleTreeIndividual<Double, Double>> breeder, SingleObjectiveEvaluator<Double> testEvaluator, Statistic<EvaluatedIndividual<Double, Double, SingleTreeIndividual<Double, Double>, SingleObjectiveFitness>> scoreLogger) {
        this.initializer = initializer;
        this.trainEvaluator = trainEvaluator;
        this.breeder = breeder;
        this.testEvaluator = testEvaluator;
        this.scoreLogger = scoreLogger;
    }

    public static FunctionApproximator fromParams(FunctionApproximationParams<Double> params) {
        return new FunctionApproximator(
                params.initializer(),
                params.trainEvaluator(),
                params.breeder(),
                params.testEvaluator(),
                params.scoreLogger()
        );
    }


    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        assert args[0].equals("--config");

        FunctionApproximator.fromParams(
               UnaryFunctionApproximator.of(Math::sin, 0)
        ).train();
    }

    public Population<?> train() {
        return GPPipeLine
                .start(initializer::initialize)
                .then(SideEffect.of(ignored -> System.out.println("Population Initialized")))
                .repeat(TerminationCriterion.nIters(50),
                        pop -> pop
                                .then(SideEffect.of(ignored -> System.out.println("Evaluating population")))
                                .then(trainEvaluator::evaluate)
                                .then(scoreLogger)
                                .then(SideEffect.of(ignored -> System.out.println("Breeding population")))
                                .then(breeder::breed)
                )
                .then(testEvaluator::evaluate)
                .then(scoreLogger::sideEffect)
                .finish();
    }
}

