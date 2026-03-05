package gp.initializers;

import gp.Population;
import gp.tree.ImmutableNode;
import gp.utils.operators.Operator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class InitializerTest {
    Random random = new Random();
    List<TypedTerminal<Double, ?>> terminals = List.of(
        TypedTerminal.of(x -> x, Double.class),
        TypedTerminal.of(x -> x * x, Double.class)
    );

    List<TypedNonTerminal<?, ?>> nonTerminals = List.of(
        TypedNonTerminal.of(Operator.bin(Math::max), Double.class, Double.class),
        TypedNonTerminal.of(Operator.bin(Math::min), Double.class, Double.class),
        TypedNonTerminal.of(Operator.unary(x -> -x), Double.class, Double.class),
        TypedNonTerminal.of(Operator.unary(Object::toString), Object.class, String.class)
    );

    @Test
    public void testPopulationSizeCorrect() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 2_000; i++) {
            assertEquals(i, BaseInitializer.full(
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
                () -> BaseInitializer.grow(
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
                () -> BaseInitializer.grow(
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
            Population<ImmutableNode<Double, ?, String, ?, ?>> population =
                    BaseInitializer.full(
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
                            .allMatch(ind -> ind.depth() == maxDepth)
            );
        });
    }
}
