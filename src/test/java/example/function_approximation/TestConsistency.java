package example.function_approximation;

import example.function_approximation.parameters.DoubleNonTerminals;
import example.function_approximation.parameters.initial.ParameterBuilder;
import gp.Population;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.AssessedIndividual;
import gp.core.initializer.PrimitiveSet;
import gp.core.initializer.PrimitiveSetBuilder;
import gp.impl.individual.SingleTreeIndividual;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Pair;
import utils.random.SourceOfRandom;

import java.util.Collections;

public class TestConsistency {
    PrimitiveSet<Pair<Double, Double>> primitiveSet = PrimitiveSetBuilder.<Pair<Double, Double>>empty()
            .addUncachedTerminal("x", Pair::first, Double.class)
            .addUncachedTerminal("y", Pair::second, Double.class)
            .addAllNonTerminals(Collections.unmodifiableList(DoubleNonTerminals.all()))
            .build();

    Class<Double> returnType = Double.class;

    @Test
    public void testConsistency() {
        Population<AssessedIndividual<
                    Pair<Double, Double>, Double,
                    SingleTreeIndividual<Pair<Double, Double>, Double>,
                    SingleObjectiveFitness
        >>  prevpop = null;

        for (int i = 0; i < 10; i++) {
            System.out.println("Run "+i);
            final SourceOfRandom rand = new SourceOfRandom(42);
            System.out.println(rand.get().nextInt());
            final var params = ParameterBuilder.<Pair<Double, Double>, Double>of()
                .initializer(new DefaultInitialiser<>(rand.get(), primitiveSet, returnType))
                .breeder(new DefaultBreeder<>(rand.get(), primitiveSet))
                .testEvaluator(new DefaultAssessor(rand.get(), 10))
                .trainEvaluator(new DefaultAssessor(rand.get(), 600))
                .build();

            final var currentPop = new FunctionApproximator<>(
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
        Population<AssessedIndividual<
                    Pair<Double, Double>, Double,
                    SingleTreeIndividual<Pair<Double, Double>, Double>,
                    SingleObjectiveFitness
                >> prevpop = null;

        for (int i = 0; i < 10; i++) {
            System.out.println("Run " + i);
            final SourceOfRandom rand = new SourceOfRandom(i);
            System.out.println(rand.get().nextInt());
            final var params = ParameterBuilder.<Pair<Double, Double>, Double>of()
                .initializer(new DefaultInitialiser<>(rand.get(), primitiveSet, returnType))
                .breeder(new DefaultBreeder<>(rand.get(), primitiveSet))
                .testEvaluator(new DefaultAssessor(rand.get(), 10))
                .trainEvaluator(new DefaultAssessor(rand.get(), 600))
                .build();

            final var currentPop = new FunctionApproximator<>(
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
