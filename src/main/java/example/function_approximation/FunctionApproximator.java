package example.function_approximation;

import example.function_approximation.parameters.FunctionApproximationParams;
import example.function_approximation.parameters.UnaryFunctionApproximator;
import gp.GPPipeLine;
import gp.Population;
import gp.TerminationCriterion;
import gp.breeder.Breeder;
import gp.initializers.Initializer;
import gp.individual.EvaluatedIndividual;
import gp.single_tree.SingleObjectiveEvaluator;
import gp.fitness.single_objective.SingleObjectiveFitness;
import gp.single_tree.SingleTreeIndividual;
import gp.statistics.SideEffect;
import gp.statistics.Statistic;

import java.util.Comparator;

/**
 * An example GP run that aims to learn an approximation.
 * to a given function
 * @param initializer The population initializer
 * @param trainEvaluator The training evaluator
 * @param breeder The breeder
 * @param testEvaluator The testing evaluator
 * @param postEvaluationStatistics A score logger
 */
public record FunctionApproximator(
        Initializer<SingleTreeIndividual<Double, Double>> initializer,
        SingleObjectiveEvaluator<Double> trainEvaluator,
        Breeder<EvaluatedIndividual<
                Double, Double,
                SingleTreeIndividual<Double, Double>, SingleObjectiveFitness>,
                SingleTreeIndividual<Double, Double>
        > breeder,
        SingleObjectiveEvaluator<Double> testEvaluator,
        Statistic<EvaluatedIndividual<
                Double, Double,
                SingleTreeIndividual<Double, Double>,
                SingleObjectiveFitness
        >> postEvaluationStatistics
) {

    /**
     * Load a Function Approximator from a given params object.
     * @param params The parameters to load from
     * @return A valid function approximator
     */
    public static FunctionApproximator fromParams(
            final FunctionApproximationParams<Double> params
    ) {
        return new FunctionApproximator(
                params.initializer(),
                params.trainEvaluator(),
                params.breeder(),
                params.testEvaluator(),
                params.scoreLogger()
        );
    }

    /**
     * Initialize a run.
     * @param args Run arguments
     */
    public static void main(final String[] args) {
        assert args[0].equals("--config");

        var finalGen = FunctionApproximator.fromParams(
                UnaryFunctionApproximator.of(Math::sin, 0)
        ).train(50);

        System.out.println(
                "Best Individual: "
                        + finalGen.stream()
                        .min(Comparator.comparing(EvaluatedIndividual::fitness))
                        .map(ind -> ind.individual().tree().getExpression())
                        .orElseThrow()
        );
    }

    /**
     * Train a GP population for n generations.
     * @param numGenerations The number of generations to train for
     * @return The final Population
     */
    public Population<EvaluatedIndividual<
            Double, Double,
            SingleTreeIndividual<Double, Double>,
            SingleObjectiveFitness
    >> train(
            final int numGenerations
    ) {
        return GPPipeLine
                .start(initializer::initialize)
                .repeat(TerminationCriterion.nIters(numGenerations),
                        (i, pop) -> pop
                                .then(SideEffect.of((ignored) -> System.out.println(
                                        "Evaluating population for gen " + i)
                                )).then(trainEvaluator::evaluate)
                                .then(postEvaluationStatistics)
                                .then(breeder::breed)
                )
                .then(testEvaluator::evaluate)
                .then(postEvaluationStatistics)
                .finish();
    }
}

