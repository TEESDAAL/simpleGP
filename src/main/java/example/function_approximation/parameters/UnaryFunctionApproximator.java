package example.function_approximation.parameters;

import com.google.common.collect.Streams;
import gp.fitness.Goal;
import gp.individual.EvaluatedIndividual;
import gp.initializers.TypedNonTerminal;
import gp.initializers.TypedTerminal;
import gp.single_tree.SingleObjectiveFitness;
import gp.single_tree.SingleTreeIndividual;
import gp.statistics.Statistic;
import gp.utils.Operator;
import gp.utils.Pair;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Second more low level parameters for the
 * FunctionApproximatorParameters.
 */
public interface UnaryFunctionApproximator
        extends FunctionApproximationParameters<Double> {

    /**
     * Creates a UnaryFunctionApproximator from a function and seed.
     * @param function The function to approximate
     * @param seed The random seed
     * @return A UnaryFunctionApproximator instance
     */
    static UnaryFunctionApproximator of(
            Function<Double, Double> function, long seed) {
        return new UnaryFunctionApproximator() {
            /**
             * Gets the function to approximate.
             * @return The function to approximate.
             */
            @Override
            public Function<Double, Double> function() {
                return function;
            }

            /**
             * Gets the random seed for this run.
             * @return The random seed for this run.
             */
            @Override
            public long seed() {
                return seed;
            }
        };
    }

    /**
     * Gets the function to approximate.
     * @return The function to approximate
     */
    Function<Double, Double> function();

    /**
     * Gets the random seed.
     * @return The random seed
     */
    long seed();

    /**
     * Gets the default population size.
     * @return The default population size.
     */
    @Override
    default int populationSize() {
        return 1000;
    }

    /**
     * Gets the maximum number of tree creation attempts.
     * @return The maximum number of tree creation attempts.
     */
    @Override
    default int maxTries() {
        return 100;
    }

    /**
     * Gets the maximum tree depth.
     * @return The maximum tree depth.
     */
    @Override
    default int maxDepth() {
        return 7;
    }


    /**
     * Gets the stream of terminals.
     * @return The stream of terminals
     */
    default Stream<TypedTerminal<Double, ?>> terminals() {
        return Stream.of(
                TypedTerminal.of(x -> x, Double.class)
        );
    }

    /**
     * Gets the double non-terminals.
     * @return The stream of double operators
     */
    default Stream<Operator<Double, Double>> doubleNonTerminals() {
        return Stream.of(
                Operator.bin(Math::max),
                Operator.bin(Math::min),
                Operator.unary(x -> -x),
                Operator.unary(x -> x * x),
                Operator.unary(Math::abs)
        );
    }

    /**
     * Converts unary operators into typed non-terminals.
     * @return The stream of non-terminal operators.
     */
    @Override
    default Stream<TypedNonTerminal<?, ?>> nonTerminals() {
        return doubleNonTerminals()
                .map(op -> TypedNonTerminal.of(
                                op, Double.class, Double.class
                ));
    }

    /**
     * Generates points between min and max.
     * @param min The minimum value
     * @param max The maximum value
     * @param numPoints The number of points
     * @param function The function to apply
     * @return A stream of point pairs
     */
    static Stream<Pair<Double, Double>> pointsBetween(
            Double min, Double max, int numPoints,
            Function<Double, Double> function) {
        double scaleToMaxMinusMin = (max - min) / (numPoints - 1);
        return IntStream.range(0, numPoints)
                .mapToDouble(n -> n * scaleToMaxMinusMin)
                .map(d -> d + min)
                .mapToObj(d -> new Pair<>(d, function.apply(d)));
    }

    /**
     * Gets the training data.
     * @return The training data stream
     */
    default Stream<Pair<Double, Double>> trainingData() {
        return pointsBetween(
                minRange(), maxRange(), numTrainingPoints(), function());
    }

    /**
     * Gets the testing data.
     * @return The testing data stream
     */
    default Stream<Pair<Double, Double>> testingData() {
        return pointsBetween(
                minRange(), maxRange(), numTestingPoints(), function());
    }

    /**
     * Gets the number of training points.
     * @return The number of training points
     */
    default int numTrainingPoints() {
        return 1000;
    }

    /**
     * Gets the number of testing points.
     * @return The number of testing points
     */
    default int numTestingPoints() {
        return 10000;
    }

    /**
     * Gets the maximum range.
     * @return The maximum range
     */
    default double maxRange() {
        return 100;
    }

    /**
     * Gets the minimum range.
     * @return The minimum range
     */
    default double minRange() {
        return -100;
    }

    /**
     * Computes mean squared error between outputs and true outputs.
     * @param outputs The predicted outputs.
     * @param trueOutputs The expected outputs.
     * @return The mean squared error.
     */
    @Override
    default double error(
            Stream<@NonNull Double> outputs,
            Stream<@NonNull Double> trueOutputs) {
        return Streams.zip(
                        outputs, trueOutputs,
                        (a, b) -> Math.pow(a - b, 2)
                ).mapToDouble(error -> error)
                .average()
                .orElseThrow();
    }

    /**
     * Gets the optimization goal.
     * @return The optimization goal
     */
    default Goal goal() {
        return Goal.MINIMIZE;
    }

    /**
     * Logs the average population score.
     * @return The score logging statistic.
     */
    @Override
    default Statistic<EvaluatedIndividual<Double, Double,
            SingleTreeIndividual<Double, Double>,
            SingleObjectiveFitness>> scoreLogger() {
        return (pop) -> System.out.println(
                "Average population score: "
                        + pop.individuals().stream()
                        .mapToDouble(ind -> ind.fitness().score())
                        .average()
        );
    }
}
