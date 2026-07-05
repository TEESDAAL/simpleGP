package gp.initializers;

import gp.Population;
import gp.core.initializers.IndividualCreationException;
import gp.core.initializers.TypedNonTerminal;
import gp.core.initializers.TypedTerminal;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.initializers.Initializers;
import utils.operators.Operator;
import org.junit.jupiter.api.Test;
import utils.random.RandomSource;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class InitializerTest {
    RandomSource random = RandomSource.of(12);
    List<TypedTerminal<Double, ?>> terminals = List.of(
        TypedTerminal.nonCached("x", x -> x, Double.class),
        TypedTerminal.nonCached("square", x -> x * x, Double.class)
    );

    List<TypedNonTerminal<?, ?>> nonTerminals = List.of(
        TypedNonTerminal.of("max", Operator.bin(Math::max), Double.class, Double.class),
        TypedNonTerminal.of("min", Operator.bin(Math::min), Double.class, Double.class),
        TypedNonTerminal.of("neg", Operator.unary(x -> -x), Double.class, Double.class),
        TypedNonTerminal.of("toStr", Operator.unary(Object::toString), Object.class, String.class)
    );

    @Test
    public void testPopulationSizeCorrect() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 2_000; i++) {
            assertEquals(i, Initializers.full(
                    random,
                    terminals,
                    nonTerminals,
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
                        terminals,
                        nonTerminals,
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
                        terminals,
                        nonTerminals,
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
            Population<SingleTreeIndividual<Double, String>> population =
                    Initializers.full(
                            random,
                            terminals,
                            nonTerminals,
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
