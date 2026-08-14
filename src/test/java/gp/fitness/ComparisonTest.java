package gp.fitness;

import gp.core.fitness.Comparison;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public class ComparisonTest {
    @Test
    public void worseTest() {
        final List<Comparison> worse = List.of(
                Comparison.WORSE,
                Comparison.compareMax(1, 2),
                Comparison.compareMin(2, 1),
                Comparison.BETTER.flip(),
                Comparison.EQUAL.then(() -> Comparison.WORSE),
                Comparison.WORSE.then(() -> Comparison.BETTER)
        );
        for (final Comparison comparison : worse) {
            assertSame(Comparison.WORSE, comparison);
        }
    }

    @Test
    public void equalTest() {
        final List<Comparison> equal = List.of(
                Comparison.EQUAL,
                Comparison.compareMax(1, 1),
                Comparison.compareMin(1, 1),
                Comparison.EQUAL.flip(),
                Comparison.EQUAL.then(() -> Comparison.EQUAL)
        );
        for (final Comparison comparison : equal) {
            assertSame(Comparison.EQUAL, comparison);
        }
    }

    @Test
    public void betterTest() {
        final List<Comparison> better = List.of(
                Comparison.BETTER,
                Comparison.compareMax(2.0, 1.0),
                Comparison.compareMin(1.0, 2.0),
                Comparison.WORSE.flip(),
                Comparison.EQUAL.then(() -> Comparison.BETTER),
                Comparison.BETTER.then(() -> Comparison.WORSE)
        );
        for (final Comparison comparison : better) {
            assertSame(Comparison.BETTER, comparison);
        }
    }
}
