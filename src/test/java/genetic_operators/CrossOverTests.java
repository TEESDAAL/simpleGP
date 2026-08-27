package genetic_operators;

import gp.impl.genetic_operators.CrossOver;
import gp.impl.individual.typed_tree.Node;
import org.junit.jupiter.api.Test;
import utils.random.RandomSource;
import utils.typed_functions.TypedBiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CrossOverTests {
    private final TypedBiFunction<Double, Double, Double> min = TypedBiFunction.of(
        Math::min, Double.class, Double.class, Double.class
    );
    private final TypedBiFunction<Double, Double, Double> add = TypedBiFunction.of(
        Double::sum, Double.class, Double.class, Double.class
    );

    @Test
    public void testCrossOver() {
        final Node<Double, Double, ?, ?, ?> parent1 = Node.nonTerm(
            "add", add,
            Node.term("one", _ -> 1.0, Double.class),
            Node.term("two", _ -> 2.0, Double.class)
        );
        final Node<Double, Double, ?, ?, ?> parent2 = Node.nonTerm(
            "min", min,
            Node.term("ten", _ -> 10.0, Double.class),
            Node.term("twenty", _ -> 20.0, Double.class)
        );

        final var children = new CrossOver<Double, Double>(new LeftChildRandom(), 20, 1)
            .produce(parent1, parent2);

        assertNotEquals(parent1.getExpression(), children.getFirst().getExpression());
        assertNotEquals(parent2.getExpression(), children.getLast().getExpression());
        assertEquals(12.0, children.getFirst().evaluate(0.0));
        assertEquals(1.0, children.getLast().evaluate(0.0));
    }

    @Test
    public void testCrossOverSubtrees() {
        final Node<Double, Double, ?, ?, ?> parent1 = Node.nonTerm(
            "add", add,
            Node.term("one", _ -> 1.0, Double.class),
            Node.nonTerm(
                "min", min,
                Node.term("two", _ -> 2.0, Double.class),
                Node.term("three", _ -> 3.0, Double.class)
            )
        );
        final Node<Double, Double, ?, ?, ?> parent2 = Node.nonTerm(
            "min", min,
            Node.term("ten", _ -> 10.0, Double.class),
            Node.nonTerm(
                "add", add,
                Node.term("twenty", _ -> 20.0, Double.class),
                Node.term("thirty", _ -> 30.0, Double.class)
            )
        );

        final var children = new CrossOver<Double, Double>(new SubtreeRandom(), 17, 1)
            .produce(parent1, parent2);

        assertEquals("add(one, add(twenty, thirty))",
            children.getFirst().getExpression());
        assertEquals("min(ten, min(two, three))",
            children.getLast().getExpression());
        assertEquals(51.0, children.getFirst().evaluate(0.0));
        assertEquals(2.0, children.getLast().evaluate(0.0));
    }

    private static class LeftChildRandom implements RandomSource {
        @Override
        public void seed(int seed) {
        }

        @Override
        public int nextInt(int lowerBound, int upperBound) {
            return lowerBound + 1;
        }

        @Override
        public double nextDouble(double lowerBound, double upperBound) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long nextLong(long lowerBound, long upperBound) {
            throw new UnsupportedOperationException();
        }

        @Override
        public float nextFloat(float lowerBound, float upperBound) {
            throw new UnsupportedOperationException();
        }
    }

    private static final class SubtreeRandom extends LeftChildRandom {
        @Override
        public int nextInt(int lowerBound, int upperBound) {
            return lowerBound + 2;
        }
    }
}
