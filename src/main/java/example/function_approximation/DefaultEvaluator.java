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
        return new DataSet<>(
            IntStream.range(0, numSamples).mapToObj(
                    i -> Pair.of(
                            random.nextDouble(-Math.PI, Math.PI),
                            random.nextDouble(-Math.PI, Math.PI)
                    )
                )
                .map(p -> Pair.of(p, p.reduce(targetFunction)))
                .toList()
        );
    }

    @Override
    public Population<EvaluatedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>> evaluate(Population<SingleTreeIndividual<Pair<Double, Double>, Double>> population) {
        var dataset = generateRandomDataSet();

        IndividualEvaluator<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness> evaluator = new IndividualEvaluator<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>() {
            @Override
            public SingleObjectiveFitness evaluate(SingleTreeIndividual<Pair<Double, Double>, Double> individual) {
                double sum = 0.0;
                double expectedResult;
                double result;
                double currentX;
                double currentY;
                for (int y=0;y<100;y++) {
                    currentX = random.nextDouble(-Math.PI, Math.PI);
                    currentY = random.nextDouble(-Math.PI, Math.PI);
                    expectedResult = Math.cos(currentY)*Math.exp(Math.sin(currentX));

                    result = Math.abs(expectedResult - individual.evaluate(Pair.of(currentX, currentY)));
                    sum += result;
                }
                return new SingleObjectiveFit(sum, Goal.MINIMIZE);
            }

            @Override
            public boolean shouldParallelize() {
                return false;
            }
        };
        return evaluator.evaluate(population);
    }
}
