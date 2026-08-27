package gp.initializers;

import gp.Population;
import gp.core.initializer.IndividualCreationException;
import gp.core.initializer.PrimitiveSet;
import gp.core.initializer.PrimitiveSetBuilder;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.initializers.Initializers;
import org.junit.jupiter.api.Test;
import utils.random.RandomSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class InitialiserTest {
    public static final int ITERATIONS = 2_000;
    int seed = 12;
    RandomSource random = RandomSource.of(seed);
    public static final PrimitiveSet<Double> primitiveSet = PrimitiveSetBuilder.<Double>empty()
        .addUncachedTerminal("x", x -> x, Double.class)
        .addUncachedTerminal("square", x -> x*x, Double.class)
        .addNonTerminal("max", Math::max, Double.class, Double.class, Double.class)
        .addNonTerminal("min", Math::min, Double.class, Double.class, Double.class)
        .addNonTerminal("neg", x -> -x, Double.class, Double.class)
        .addNonTerminal("toStr", Object::toString, Object.class, String.class)
        .build();

    @Test
    public void testPopulationSizeCorrect() {
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            assertEquals(i, Initializers.full(
                    random,
                    primitiveSet,
                    i,
                    100,
                    7,
                    String.class
            ).initialize().individuals().size());
        }
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Test
    public void testIllegalMaxDepth() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Initializers.grow(
                        random,
                        primitiveSet,
                        100,
                        100,
                        -1,
                        String.class
                ).initialize(),
                "-1 should be an invalid max depth"
        );
        assertThrows(
                IndividualCreationException.class,
                () -> Initializers.grow(
                        random,
                        primitiveSet,
                        100,
                        100,
                        0,
                        String.class
                ).initialize(),
                "Should be able to construct a single-node tree"
        );
    }

    @Test
    public void testMaxDepth() {
        IntStream.range(2, 10).forEach(maxDepth -> {
            final Population<SingleTreeIndividual<Double, String>> population = Initializers.full(
                    random,
                    primitiveSet,
                    100,
                    100,
                    maxDepth,
                    String.class
            ).initialize();

            assertTrue(
                    population.stream()
                            .allMatch(ind -> ind.tree().depth() == maxDepth)
            );
        });
    }
}
