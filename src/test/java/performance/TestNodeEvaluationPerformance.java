package performance;

import example.function_approximation.DefaultInitialiser;
import gp.core.initializer.PrimitiveSet;
import gp.core.initializer.PrimitiveSetBuilder;
import org.junit.jupiter.api.Test;
import utils.Pair;
import utils.Repeat;
import utils.random.RandomSource;

public class TestNodeEvaluationPerformance {
    PrimitiveSet<Pair<Double, Double>> primitiveSet = PrimitiveSetBuilder.<Pair<Double, Double>>empty()
        .addUncachedTerminal("x", Pair::first, Double.class)
        .addUncachedTerminal("y", Pair::second, Double.class)
        .addNonTerminal("max", Math::max, Double.class, Double.class, Double.class)
        .addNonTerminal("min", Math::min, Double.class, Double.class, Double.class)
        .addNonTerminal("neg", x -> -x, Double.class, Double.class)
        .addNonTerminal("toStr", Object::toString, Object.class, String.class)
        .build();
    @Test
    public void checkNodeEvaluationTimes() {
        final RandomSource rand = RandomSource.of(12);
        final var pop = new DefaultInitialiser<>(
            rand, primitiveSet, String.class
        ).initialize();
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
