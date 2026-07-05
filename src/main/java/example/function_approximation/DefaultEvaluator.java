package example.function_approximation;

import gp.Population;
import gp.core.evaluators.Evaluator;
import gp.core.evaluators.IndividualEvaluator;
import gp.core.fitness.Goal;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.EvaluatedIndividual;
import gp.impl.fitness.SingleObjectiveFit;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.statistics.EvaluationFunctions;
import utils.DataSet;
import utils.Pair;
import utils.random.RandomSource;

import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;

public class DefaultEvaluator implements Evaluator<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness> {

    final RandomSource random;
    BiFunction<Double, Double, Double> targetFunction; 
    final int numSamples;
    public DefaultEvaluator(RandomSource random, int numSamples) {
        this(random, numSamples, (x, y) -> Math.cos(y)*Math.exp(Math.sin(x)));
    }

    public DefaultEvaluator(RandomSource random, int numSamples, BiFunction<Double, Double, Double> targetFunction) {
        this.random = random;
        this.numSamples = numSamples;
        this.targetFunction = targetFunction;
    }


    DataSet<Pair<Double, Double>, Double> generateRandomDataSet() {
        double xOffset = random.nextDouble(-0.1, 0.1);
        double yOffset = random.nextDouble(-0.1, 0.1);
        int end = (int) sqrt(numSamples);
        return new DataSet<>(
            IntStream.range(0, end).boxed()
                .flatMap(x -> IntStream.range(0, end).mapToObj(y -> Pair.of(
                    x / (double) end + xOffset,
                    y / (double) end + yOffset
                )))
                .map(p -> Pair.of(p, p.reduce(targetFunction)))
                .toList()
        );
    }

    @Override
    public Population<EvaluatedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>> evaluate(Population<SingleTreeIndividual<Pair<Double, Double>, Double>> population) {
        var dataset = generateRandomDataSet();

        IndividualEvaluator<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness> evaluator = i -> SingleObjectiveFit.of(EvaluationFunctions.MSE(
            dataset.zip((p, y) -> Pair.of(i.evaluate(p), y)).toList()
        ), Goal.MINIMIZE);
        return evaluator.evaluate(population);
    }
}
