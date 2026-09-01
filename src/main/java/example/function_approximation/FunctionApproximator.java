package example.function_approximation;

import example.function_approximation.parameters.DoubleNonTerminals;
import example.function_approximation.parameters.initial.ParameterBuilder;
import gp.GPPipeLine;
import gp.Population;
import gp.TerminationCriterion;
import gp.core.assessor.Assessor;
import gp.core.breeder.Breeder;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.AssessedIndividual;
import gp.core.individual.Individual;
import gp.core.initializer.Initialiser;
import gp.core.initializer.PrimitiveSet;
import gp.core.initializer.PrimitiveSetBuilder;
import gp.core.statistic.SideEffect;
import gp.core.statistic.Statistic;
import util.Pair;
import util.random.SourceOfRandom;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An example GP run that aims to learn an approximation.
 * to a given function of f(x, y)
 *
 * @param initialiser              The population initializer
 * @param trainEvaluator           The training evaluator
 * @param breeder                  The breeder
 * @param testEvaluator            The testing evaluator
 * @param postEvaluationStatistics A score logger
 */
public record FunctionApproximator<
        Ind extends Individual<Pair<Double, Double>, Double>,
        E extends Assessor<Pair<Double, Double>, Double, Ind, SingleObjectiveFitness>
>(
        Initialiser<Ind> initialiser,
        E trainEvaluator,
        Breeder<AssessedIndividual<Pair<Double, Double>, Double, Ind, SingleObjectiveFitness>, Ind> breeder,
        E testEvaluator,
        Statistic<AssessedIndividual<Pair<Double, Double>, Double, Ind, SingleObjectiveFitness>, ?> postEvaluationStatistics
) {

    /**
     * Initialize a run.
     */
    static void main() {
        final SourceOfRandom rand = new SourceOfRandom(42);

        final PrimitiveSet<Pair<Double, Double>> primitiveSet = PrimitiveSetBuilder.<Pair<Double, Double>>empty()
                .addUncachedTerminal("x", Pair::first, Double.class)
                .addUncachedTerminal("y", Pair::second, Double.class)
                .addAllNonTerminals(Collections.unmodifiableList(DoubleNonTerminals.all()))
                .build();

        final var params = ParameterBuilder.<Pair<Double, Double>, Double>of()
                .initializer(new DefaultInitialiser<>(rand.get(), primitiveSet, Double.class))
                .breeder(new DefaultBreeder<>(rand.get(), primitiveSet))
                .trainEvaluator(new DefaultAssessor(rand.get(), 100))
                .testEvaluator(new DefaultAssessor(rand.get(), 1))
                .addStatistic(
                        population -> {
                            final List<Double> fitness = population.stream().map(e -> e.fitness().score()).toList();
                            return "BEST: " + fitness.stream().mapToDouble(d -> d).min().orElse(0) + " SIZE: " + population.stream().mapToInt(i -> i.individual().tree().depth()).average().orElse(0.0);
//                    return "MEAN: "+ NumericUtils.round(CommonFunctions.MEAN(fitness), 3)+", MEDIAN: "+NumericUtils.round(CommonFunctions.MEDIAN(fitness), 3);
                        })
                .build();
        final long start = System.currentTimeMillis();
        final var finalGen = new FunctionApproximator<>(
                params.initializer(),
                params.trainEvaluator(),
                params.breeder(),
                params.testEvaluator(),
                params.scoreLogger()
        ).train(50);

        System.out.println("Training time took " + (System.currentTimeMillis() - start) + "ms");

        System.out.println(
                "Best Individual: "
                        + finalGen.stream()
                        .min(Comparator.comparing(AssessedIndividual::fitness))
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
     *
     * @param numGenerations The number of generations to train for
     * @return The final Population
     */
    public Population<AssessedIndividual<Pair<Double, Double>, Double, Ind, SingleObjectiveFitness>> train(
            final int numGenerations
    ) {

        return GPPipeLine
                .start(initialiser::initialize)
                .repeat(TerminationCriterion.nIters(numGenerations),
                        (i, pop) -> pop
                                .then(SideEffect.of((ignored) -> System.out.println(
                                        "Evaluating population for gen " + i)
                                ))
                                .then(trainEvaluator::assess)
                                .then(postEvaluationStatistics)
                                .then(breeder::breed)
                )
                .then(testEvaluator::assess)
                .then(postEvaluationStatistics)
                .finish();
    }
}


