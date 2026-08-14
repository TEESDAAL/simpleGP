package gp.fitness;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Utility class for testing comparator contracts.
 */
public enum Comparators {
    ;

    // The implementor must ensure signum(x.compareTo(y)) == -signum(y.compareTo(x)) for all x and y.
// The implementor must also ensure that the relation is transitive:
// (x.compareTo(y) > 0 && y.compareTo(z) > 0) implies x.compareTo(z) > 0.
// Finally, the implementor must ensure that x.compareTo(y)==0 implies that signum(x.compareTo(z)) == signum(y.compareTo(z)), for all z.
    static <T> void violatesContract(Comparator<T> comparator, List<T> elements) {
        for (final T x : elements) {
            for (final T y : elements) {
                final int result1 = comparator.compare(x, y);
                final int result2 = comparator.compare(y, x);
                assertEquals(Math.signum(result1), -Math.signum(result2), 0.0001);
                for (final T z : elements) {
                    if (comparator.compare(x, y) > 0 && comparator.compare(y, z) > 0) {
                        assertTrue(comparator.compare(x, z) > 0);
                    }
                    if (comparator.compare(x, y) == 0) {
                        assertEquals(
                                Math.signum(comparator.compare(x, z)),
                                Math.signum(comparator.compare(y, z)),
                                0.0001
                        );
                    }
                }
            }
        }

    }

}
