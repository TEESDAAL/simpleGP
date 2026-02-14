package example.function_approximation.parameters;

import com.google.common.collect.Streams;
import gp.Population;
import gp.fitness.Goal;
import gp.individual.EvaluatedIndividual;
import gp.initializers.TypedNonTerminal;
import gp.initializers.TypedTerminal;
import gp.single_tree.SingleObjectiveFitness;
import gp.single_tree.SingleTreeIndividual;
import gp.statistics.Statistic;
import gp.utils.Operator;
import gp.utils.Pair;
import gp.utils.UnaryOperator;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface UnaryFunctionApproximator extends FunctionApproximationParameters<Double> {
    static UnaryFunctionApproximator of(Function<Double, Double> function, long seed) {
        return new UnaryFunctionApproximator() {
            @Override
            public Function<Double, Double> function() {
                return function;
            }

            @Override
            public long seed() {
                return seed;
            }
        };
    }

    Function<Double, Double> function();
    long seed();

    default List<TypedTerminal<Double, ?>> terminals() {
        return List.of(
                TypedTerminal.of(x -> x, Double.class)
        );
    }
    default List<Operator<Double, Double>> doubleNonTerminals() {
        return List.of(
                Operator.bin(Math::max),
                Operator.bin(Math::min),
                Operator.unary(x -> -x),
                Operator.unary(x -> x*x),
                Operator.unary(Math::abs)
        );
    }
    default List<TypedNonTerminal<?, ?>> nonTerminals() {
        return Collections.unmodifiableList(doubleNonTerminals()
                .stream()
                .map(op -> (TypedNonTerminal<?, ?>) TypedNonTerminal.of(op, Double.class, Double.class))
                .toList()
        );
    }

    static Stream<Pair<Double, Double>> pointsBetween(Double min, Double max, int numPoints, Function<Double, Double> function) {
        double scaleToMaxMinusMin = (max - min) / (numPoints - 1);
        return IntStream.range(0, numPoints)
                .mapToDouble(n -> n * scaleToMaxMinusMin)
                .map(d -> d + min)
                .mapToObj(d -> new Pair<>(d, function.apply(d)));
    }

    default Stream<Pair<Double, Double>> trainingData() {
        return pointsBetween(minRange(), maxRange(), numTrainingPoints(), function());
    }
    default Stream<Pair<Double, Double>> testingData() {
        return pointsBetween(minRange(), maxRange(), numTestingPoints(), function());
    }

    default int numTrainingPoints() {
        return 10;
    }

    default int numTestingPoints() {
        return 1000;
    }

    default double maxRange() {
        return 100;
    }

    default double minRange() {
        return -100;
    }
    @Override
    default double error(Stream<@NonNull Double> outputs, Stream<@NonNull Double> trueOutputs) {
        return Streams.zip(
                        outputs, trueOutputs,
                        (a, b) -> Math.pow(a - b, 2)
                ).mapToDouble(error -> error)
                .average()
                .orElseThrow();
    }

    default Goal goal() {
        return Goal.MINIMIZE;
    }

    @Override
    default Statistic<EvaluatedIndividual<Double, Double, SingleTreeIndividual<Double, Double>, SingleObjectiveFitness>> scoreLogger() {
        return (pop) -> System.out.println(
                "Average population score: "
                        + pop.individuals().stream()
                        .mapToDouble(ind -> ind.fitness().score())
                        .average()
        );
    }
}