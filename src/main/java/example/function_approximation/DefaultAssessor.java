package example.function_approximation;

import gp.core.assessor.IndividualAssessor;
import gp.core.fitness.Goal;
import gp.core.fitness.SingleObjectiveFitness;
import gp.impl.fitness.SingleObjectiveFit;
import gp.impl.individual.SingleTreeIndividual;
import utils.Pair;
import utils.random.RandomSource;

import java.util.function.BiFunction;

public class DefaultAssessor implements IndividualAssessor<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness> {
    final RandomSource random;
    BiFunction<Double, Double, Double> targetFunction; 
    final int numSamples;
    public DefaultAssessor(RandomSource random, int numSamples) {
        this(random, numSamples, (x, y) -> Math.cos(y)*Math.exp(Math.sin(x)));
    }

    public DefaultAssessor(RandomSource random, int numSamples, BiFunction<Double, Double, Double> targetFunction) {
        this.random = random;
        this.numSamples = numSamples;
        this.targetFunction = targetFunction;
    }


    @Override
    public SingleObjectiveFitness evaluate(SingleTreeIndividual<Pair<Double, Double>, Double> individual) {
        double sum = 0.0;
        double expectedResult;
        double result;
        double currentX;
        double currentY;
        for (int y = 0; y < 100; y++) {
            currentX = random.nextDouble(-Math.PI, Math.PI);
            currentY = random.nextDouble(-Math.PI, Math.PI);
            expectedResult = Math.cos(currentY) * Math.exp(Math.sin(currentX));

            result = Math.abs(expectedResult - individual.evaluate(Pair.of(currentX, currentY)));
            sum += result;
        }
        return new SingleObjectiveFit(sum, Goal.MINIMIZE);
    }

    @Override
    public boolean shouldParallelize() {
        return false;
    }
}
