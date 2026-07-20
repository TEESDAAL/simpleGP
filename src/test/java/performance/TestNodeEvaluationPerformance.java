package performance;

import example.function_approximation.DefaultInitialiser;
import gp.Population;
import gp.impl.initializers.Initializers;
import org.junit.jupiter.api.Test;
import utils.Pair;
import utils.Repeat;
import utils.RepeatTest;
import utils.random.RandomSource;

import java.util.Random;

public class TestNodeEvaluationPerformance {
    @Test
    public void checkNodeEvaluationTimes() {
        final RandomSource rand = RandomSource.of(12);
        final var pop = new DefaultInitialiser(rand).initialize();
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
