package example;

import gp.GPPipeLine;
import gp.Population;
import gp.TerminationCriterion;
import gp.breeder.Breeder;
import gp.breeder.Initializer;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.evaluators.Evaluator;
import gp.individual.Individual;
import gp.statistics.SideEffect;
import gp.statistics.Statistic;

import java.lang.reflect.InvocationTargetException;


interface DoubleInd extends Individual<Double, Double> {}
interface DoubleFit extends Fitness<DoubleFit> {}
public class GPExample {
    static Initializer<DoubleInd> initializer;
    static Evaluator<Double, Double, DoubleInd, DoubleFit> trainEvaluator;
    static Breeder<EvaluatedIndividual<Double, Double, DoubleInd, DoubleFit>, DoubleInd> breeder;
    static Evaluator<Double, Double, DoubleInd, DoubleFit> testEvaluator;
    static Statistic<EvaluatedIndividual<Double, Double, DoubleInd, DoubleFit>> scoreLogger;

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        assert args[0].equals("--config");

        setupFromClassPath(args[1]);
        final Population<EvaluatedIndividual<Double, Double, DoubleInd, DoubleFit>> finalPopulation = GPPipeLine
                .start(initializer::initialize)
                .repeat(TerminationCriterion.nIters(50),
                        pop -> pop
                                .then(trainEvaluator::evaluate)
                                .then(scoreLogger::sideEffect)
                                .then(SideEffect.of(ignored -> System.out.println("Breeding population")))
                                .then(breeder::breed)
                )
                .then(testEvaluator::evaluate)
                .then(scoreLogger::sideEffect)
                .finish();
    }

    private static void setupFromClassPath(String classPath) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        GPExampleConfig config = (GPExampleConfig) Class.forName(classPath).getConstructor().newInstance();
        GPExample.initializer = config.initializer();
        GPExample.trainEvaluator = config.trainEvaluator();
        GPExample.breeder = config.breeder();
        GPExample.testEvaluator = config.testEvaluator();
        GPExample.scoreLogger = config.scoreLogger();
    }
}

interface GPExampleConfig {

    Initializer<DoubleInd> initializer();

    Evaluator<Double, Double, DoubleInd, DoubleFit> trainEvaluator();

    Breeder<EvaluatedIndividual<Double, Double, DoubleInd, DoubleFit>, DoubleInd> breeder();

    Evaluator<Double, Double, DoubleInd, DoubleFit> testEvaluator();

    Statistic<EvaluatedIndividual<Double, Double, DoubleInd, DoubleFit>> scoreLogger();
}