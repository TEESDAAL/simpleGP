package example.function_approximation.parameters;

import gp.breeder.Breeder;
import gp.breeder.Initializer;
import gp.breeder.NaiveBreeder;
import gp.breeder.SelectorBuilder;
import gp.fitness.Goal;
import gp.genetic_operators.Identity;
import gp.genetic_operators.SubtreeMutation;
import gp.individual.EvaluatedIndividual;
import gp.initializers.BaseInitializer;
import gp.initializers.TypedNonTerminal;
import gp.initializers.TypedTerminal;
import gp.random.ProbabilisticElement;
import gp.random.WeightedRandomSampler;
import gp.selectors.TournamentSelection;
import gp.single_tree.SingleObjectiveEvaluator;
import gp.single_tree.SingleObjectiveFitness;
import gp.single_tree.SingleTreeIndividual;
import gp.single_tree.SingleTreeInitializer;
import gp.utils.Operator;
import gp.utils.Pair;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Further specification of parameters.
 * @param <T> The terminal Type.
 */
public interface FunctionApproximationParameters<T>
        extends FunctionApproximationParams<T> {
    /**
     * Gets a Stream of typed terminals: T -&gt; U.
     * @return A Stream of typed terminals: T -&gt; U.
     */
    Stream<TypedTerminal<T, ?>> terminals();

    /**
     * Gets a stream of typed nonTerminals: list[U] -&gt; V.
     * @return A stream of typed nonTerminals: list[U] -&gt; V.
     */
    Stream<TypedNonTerminal<?, ?>> nonTerminals();

    /**
     * Gets the training data.
     * @return The training data
     */
    Stream<Pair<T, Double>> trainingData();

    /**
     * Gets the testing data.
     * @return The testing data
     */
    Stream<Pair<T, Double>> testingData();

    /**
     * Computes error.
     * @param outputs The outputs
     * @param trueOutputs The true outputs
     * @return The error
     */
    double error(Stream<Double> outputs, Stream<Double> trueOutputs);

    /**
     * Gets the optimization goal.
     * @return The goal
     */
    Goal goal();

    /**
     * Gets the random seed.
     * @return The seed
     */
    long seed();

    /**
     * Gets the population size.
     * @return The population size
     */
    int populationSize();

    /**
     * Gets the max tries.
     * @return The max tries
     */
    int maxTries();

    /**
     * Gets the max depth.
     * @return The max depth
     */
    int maxDepth();

    /**
     * Gets the random number generator.
     * @return The random number generator
     */
    default Random random() {
        return new Random(seed());
    }

    /**
     * Builds a tree initializer using the configured terminals,
     * non-terminals, and limits.
     * @return The initializer for single-tree individuals.
     */
    @Override
    default Initializer<SingleTreeIndividual<T, Double>>
            initializer() {
        return new SingleTreeInitializer<>(BaseInitializer.grow(
                this.random(),
                this.terminalMap(),
                this.nonTerminalMap(),
                this.populationSize(),
                this.maxTries(),
                this.maxDepth(),
                Double.class
        ));
    }

    /**
     * Gets the terminal map.
     * @return The terminal map
     */
    default Map<Class<?>, List<TypedTerminal<T, ?>>>
            terminalMap() {
        return this.terminals().collect(
                Collectors.groupingBy(TypedTerminal::returnType));
    }

    /**
     * Gets the non-terminal map.
     * @return The non-terminal map
     */
    default Map<Class<?>, List<TypedNonTerminal<?, ?>>>
            nonTerminalMap() {
        return this.nonTerminals().collect(
                Collectors.groupingBy(TypedNonTerminal::returnType));
    }


    /**
     * Creates the evaluator for training data.
     * @return The training evaluator.
     */
    @Override
    default SingleObjectiveEvaluator<T> trainEvaluator() {
        return SingleObjectiveEvaluator.of(
                (ind) -> this.error(
                        trainingData().map(Pair::first).map(ind::evaluate),
                        trainingData().map(Pair::second)
                ), this.goal()
        );
    }

    /**
     * Creates the evaluator for testing data.
     * @return The testing evaluator.
     */
    @Override
    default SingleObjectiveEvaluator<T> testEvaluator() {
        return SingleObjectiveEvaluator.of(
                (ind) -> this.error(
                        testingData().map(Pair::first).map(ind::evaluate),
                        testingData().map(Pair::second)
                ), this.goal()
        );
    }

    /**
     * Gets operator probabilities.
     * @return The list of operator probabilities
     */
    default List<ProbabilisticElement<Operator<
            SingleTreeIndividual<T, Double>,
            SingleTreeIndividual<T, Double>>>>
            operatorProbabilities() {
        return List.of(
                ProbabilisticElement.of(
                        0.3,
                        SingleTreeIndividual.operator(
                                new SubtreeMutation<>(
                                this.random(), this.terminalMap(),
                                this.nonTerminalMap(),
                                this.maxDepth(), this.maxTries()
                                )
                        )),
                ProbabilisticElement.of(0.7, new Identity<>())
        );
    }

    /**
     * Creates the breeder with operator probabilities and tournament
     * selection.
     * @return The configured breeder.
     */
    @Override
    default Breeder<EvaluatedIndividual<T, Double,
            SingleTreeIndividual<T, Double>, SingleObjectiveFitness>,
            SingleTreeIndividual<T, Double>> breeder() {
        WeightedRandomSampler<Operator<
                SingleTreeIndividual<T, Double>,
                SingleTreeIndividual<T, Double>
        >> operatorSelector = new WeightedRandomSampler<>(
                this.random(),
                this.operatorProbabilities()
        );

        SelectorBuilder<EvaluatedIndividual<
                T, Double,
                SingleTreeIndividual<T, Double>,
                SingleObjectiveFitness
        >> selectorBuilder = new TournamentSelection<>(
                this.random(), tournamentSize()
        );

        return new NaiveBreeder<>(
                operatorSelector,
                this.populationSize(),
                selectorBuilder
        );
    }

    /**
     * Gets the tournament size.
     * @return The tournament size
     */
    default int tournamentSize() {
        return 7;
    }
}
