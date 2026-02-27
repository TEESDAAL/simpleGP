package example;

import gp.GPPipeLine;
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


interface DoubleInd extends Individual<Double, Double> { }
interface DoubleFit extends Fitness<DoubleFit> { }

/**
 * Example GP run showing configuration from interface.
 */
public final class GPExample {
    /** Initializer for population. */
    private static Initializer<DoubleInd> initializer;
    /** Training evaluator. */
    private static Evaluator<Double, Double, DoubleInd, DoubleFit>
            trainEvaluator;
    /** Breeder for new generations. */
    private static Breeder<EvaluatedIndividual<Double,
            Double, DoubleInd, DoubleFit>, DoubleInd> breeder;
    /** Testing evaluator. */
    private static Evaluator<Double, Double, DoubleInd, DoubleFit>
            testEvaluator;
    /** Score logger. */
    private static Statistic<EvaluatedIndividual<Double,
            Double, DoubleInd, DoubleFit>> scoreLogger;

    private GPExample() {
        // Utility class
    }

    /**
     * Entry point. Expects `--config` and a fully qualified config
     * class name.
     * @param args CLI arguments.
     * @throws ClassNotFoundException When the config class is missing.
     * @throws InstantiationException When the config class cannot be
     *     instantiated.
     * @throws IllegalAccessException When the config constructor is not
     *     accessible.
     * @throws NoSuchMethodException When the config no-arg constructor
     *     is missing.
     * @throws InvocationTargetException When the config constructor
     *     throws.
     */
    public static void main(final String[] args)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        assert args[0].equals("--config");

        setupFromClassPath(args[1]);
        GPPipeLine
                .start(initializer::initialize)
                .repeat(TerminationCriterion.nIters(50),
                        (i, pop) -> pop
                                .then(trainEvaluator::evaluate)
                                .then(scoreLogger::sideEffect)
                                .then(SideEffect.of(ignored ->
                                        System.out.println(
                                                "Breeding population for gen "
                                                + i)))
                                .then(breeder::breed)
                )
                .then(testEvaluator::evaluate)
                .then(scoreLogger::sideEffect)
                .finish();
    }

    /**
     * Loads the config class and stores its GP components for the
     * run.
     * @param classPath Fully qualified class name on the classpath.
     * @throws ClassNotFoundException When the config class is missing.
     * @throws InstantiationException When the config class cannot be
     *     instantiated.
     * @throws IllegalAccessException When the config constructor is not
     *     accessible.
     * @throws NoSuchMethodException When the config no-arg constructor
     *     is missing.
     * @throws InvocationTargetException When the config constructor
     *     throws.
     */
    private static void setupFromClassPath(final String classPath)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, NoSuchMethodException,
            InvocationTargetException {
        GPExampleConfig config = (GPExampleConfig) Class.forName(classPath)
                .getConstructor()
                .newInstance();

        GPExample.initializer = config.initializer();
        GPExample.trainEvaluator = config.trainEvaluator();
        GPExample.breeder = config.breeder();
        GPExample.testEvaluator = config.testEvaluator();
        GPExample.scoreLogger = config.scoreLogger();
    }
}

/**
 * Config interface for GP examples.
 */
interface GPExampleConfig {

    /**
     * Gets the initializer.
     * @return The initializer
     */
    Initializer<DoubleInd> initializer();

    /**
     * Gets the training evaluator.
     * @return The training evaluator
     */
    Evaluator<Double, Double, DoubleInd, DoubleFit> trainEvaluator();

    /**
     * Gets the breeder.
     * @return The breeder
     */
    Breeder<EvaluatedIndividual<Double, Double, DoubleInd, DoubleFit>,
            DoubleInd> breeder();

    /**
     * Gets the testing evaluator.
     * @return The testing evaluator
     */
    Evaluator<Double, Double, DoubleInd, DoubleFit> testEvaluator();

    /**
     * Gets the score logger.
     * @return The score logger
     */
    Statistic<EvaluatedIndividual<Double, Double, DoubleInd,
            DoubleFit>> scoreLogger();
}
