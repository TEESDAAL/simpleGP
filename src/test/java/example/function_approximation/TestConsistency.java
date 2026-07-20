package example.function_approximation;

import example.function_approximation.parameters.initial.ParameterBuilder;
import gp.Population;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.EvaluatedIndividual;
import gp.impl.individual.SingleTreeIndividual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Pair;
import utils.random.SourceOfRandom;

public class TestConsistency {
    @Test
    public void testConsistency() {
        Population<EvaluatedIndividual<
            Pair<Double, Double>, Double,
            SingleTreeIndividual<Pair<Double, Double>, Double>,
            SingleObjectiveFitness
        >>  prevpop = null;

        for (int i = 0; i < 10; i++) {
            System.out.println("Run "+i);
            SourceOfRandom rand = new SourceOfRandom(42);
            System.out.println(rand.get().nextInt());
            var params = ParameterBuilder.<Pair<Double, Double>, Double>of()
                .initializer(new DefaultInitialiser(rand.get()))
                .breeder(new DefaultBreeder(rand.get()))
                .testEvaluator(new DefaultEvaluator(rand.get(), 10))
                .trainEvaluator(new DefaultEvaluator(rand.get(), 600))
                .build();

            var currentPop = new FunctionApproximator<>(
                params.initializer(),
                params.trainEvaluator(),
                params.breeder(),
                params.testEvaluator(),
                params.scoreLogger()
            ).train(5);
            if (prevpop != null) {
                Assertions.assertEquals(prevpop.toString(), currentPop.toString());
            }
            prevpop = currentPop;
        }
    }


    @Test
    public void testInconsistency() {
        Population<EvaluatedIndividual<
            Pair<Double, Double>, Double,
            SingleTreeIndividual<Pair<Double, Double>, Double>,
            SingleObjectiveFitness
        >> prevpop = null;

        for (int i = 0; i < 10; i++) {
            System.out.println("Run " + i);
            SourceOfRandom rand = new SourceOfRandom(i);
            System.out.println(rand.get().nextInt());
            var params = ParameterBuilder.<Pair<Double, Double>, Double>of()
                .initializer(new DefaultInitialiser(rand.get()))
                .breeder(new DefaultBreeder(rand.get()))
                .testEvaluator(new DefaultEvaluator(rand.get(), 10))
                .trainEvaluator(new DefaultEvaluator(rand.get(), 600))
                .build();

            var currentPop = new FunctionApproximator<>(
                params.initializer(),
                params.trainEvaluator(),
                params.breeder(),
                params.testEvaluator(),
                params.scoreLogger()
            ).train(5);
            if (prevpop != null) {
                Assertions.assertNotEquals(prevpop.toString(), currentPop.toString());
            }
            prevpop = currentPop;
        }
    }
}
