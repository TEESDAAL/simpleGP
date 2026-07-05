package example.function_approximation;

import example.function_approximation.parameters.*;
import example.function_approximation.parameters.initial.ParameterBuilder;
import gp.GPPipeLine;
import gp.Population;
import gp.TerminationCriterion;
import gp.core.breeder.Breeder;
import gp.core.evaluators.Evaluator;
import gp.core.individual.Individual;
import gp.core.initializers.Initializer;
import gp.core.individual.EvaluatedIndividual;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.statistics.SideEffect;
import gp.core.statistics.Statistic;
import gp.impl.statistics.CommonFunctions;
import gp.impl.statistics.NumericUtils;
import utils.Pair;
import utils.random.SourceOfRandom;

import java.util.Comparator;
import java.util.List;

/**
 * An example GP run that aims to learn an approximation.
 * to a given function of f(x, y)
 * @param initializer The population initializer
 * @param trainEvaluator The training evaluator
 * @param breeder The breeder
 * @param testEvaluator The testing evaluator
 * @param postEvaluationStatistics A score logger
 */
public record FunctionApproximator<
    X extends Pair<Double, Double>, Y extends Double,
    Ind extends Individual<X, Y>,
    E extends Evaluator<X, Y, Ind, SingleObjectiveFitness>
    >(
        Initializer<Ind> initializer,
        E trainEvaluator,
        Breeder<EvaluatedIndividual<X, Y, Ind, SingleObjectiveFitness>, Ind> breeder,
        E testEvaluator,
        Statistic<EvaluatedIndividual<X, Y, Ind, SingleObjectiveFitness>, ?> postEvaluationStatistics
) {

    /**
     * Initialize a run.
     * @param args Run arguments
     */
    public static void main(final String[] args) {
        SourceOfRandom rand = new SourceOfRandom(42);
        System.out.println(rand.get().nextInt());
        var params = ParameterBuilder.<Pair<Double, Double>, Double>of()
            .initializer(new DefaultInitializer(rand.get()))
            .breeder(new DefaultBreeder(rand.get()))
            .testEvaluator(new DefaultEvaluator(rand.get(), 10))
            .trainEvaluator(new DefaultEvaluator(rand.get(), 600))
            .addStatistic(
                population -> {
                    List<Double> fitness = population.stream().map(e -> e.fitness().score()).toList();
                    return "MEAN: "+ NumericUtils.round(CommonFunctions.MEAN(fitness), 3)+", MEDIAN: "+NumericUtils.round(CommonFunctions.MEDIAN(fitness), 3);
                })
            .build();

        var finalGen = new FunctionApproximator<>(
                params.initializer(),
                params.trainEvaluator(),
                params.breeder(),
                params.testEvaluator(),
                params.scoreLogger()
        ).train(5);
        System.out.println(rand.get().nextInt());

        System.out.println(
                "Best Individual: "
                        + finalGen.stream()
                        .min(Comparator.comparing(EvaluatedIndividual::fitness))
                        .map(ind -> ind.individual().tree().getExpression())
                    .orElseThrow()
                    .replace("%", "d")
                    .replace("+", "p")
                    .replace("-", "m")
                    .replace("*", "t")
                    .replace("neg", "n")
        );
    }

    /**
     * Train a GP population for n generations.
     * @param numGenerations The number of generations to train for
     * @return The final Population
     */
    public Population<EvaluatedIndividual<X, Y, Ind, SingleObjectiveFitness>> train(
            final int numGenerations
    ) {
        return GPPipeLine
            .start(initializer::initialize)
            .repeat(TerminationCriterion.nIters(numGenerations),
                    (i, pop) -> pop
                            .then(SideEffect.of((ignored) -> System.out.println(
                                    "Evaluating population for gen " + i)
                            ))
                            .then(trainEvaluator::evaluate)
                            .then(postEvaluationStatistics)
                            .then(breeder::breed)
            )
            .then(testEvaluator::evaluate)
            .then(postEvaluationStatistics)
            .finish();
    }
}


