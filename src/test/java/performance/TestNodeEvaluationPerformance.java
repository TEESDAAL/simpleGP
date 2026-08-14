package performance;

import example.function_approximation.DefaultInitialiser;
import gp.Population;
import gp.core.initializer.PrimitiveSet;
import gp.core.initializer.PrimitiveSetBuilder;
import gp.impl.initializers.Initializers;
import org.junit.jupiter.api.Test;
import utils.Pair;
import utils.Repeat;
import utils.RepeatTest;
import utils.operators.Operator;
import utils.random.RandomSource;

import java.util.Random;

public class TestNodeEvaluationPerformance {
    PrimitiveSet<Double> primitiveSet = PrimitiveSetBuilder.<Double>empty()
        .addUncachedTerminal("x", x -> x, Double.class)
        .addUncachedTerminal("square", x -> x*x, Double.class)
        .addNonTerminal("max", Operator.bin(Math::max), Double.class, Double.class)
        .addNonTerminal("min", Operator.bin(Math::min), Double.class, Double.class)
        .addNonTerminal("neg", Operator.unary(x -> -x), Double.class, Double.class)
        .addNonTerminal("toStr", Operator.unary(Object::toString), Object.class, String.class)
        .build();
    @Test
    public void checkNodeEvaluationTimes() {
        final RandomSource rand = RandomSource.of(12);
        final var pop = new DefaultInitialiser(rand, primitiveSet).initialize();
        System.out.println("WARMUP");
        Repeat.of(
            100,
            () -> pop.individuals().forEach(p -> p.evaluate(Pair.of(
                rand.nextDouble(-10, 10),
                rand.nextDouble(-10, 10))
            ))
        );

        final long start = System.currentTimeMillis();
        final int testEvals = 1000;
        Repeat.of(
            1000,
            () -> pop.individuals().forEach(p -> p.evaluate(Pair.of(
                rand.nextDouble(-10, 10),
                rand.nextDouble(-10, 10))
            ))
        );

        System.out.println(pop.size()*testEvals + " evals in" + (System.currentTimeMillis() - start) + "ms");
        System.out.println("Average individual size: "+ pop.individuals().stream().mapToInt(p -> p.tree().depth()).average().orElse(0.0));
    }
}
