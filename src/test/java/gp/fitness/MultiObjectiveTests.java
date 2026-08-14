package gp.fitness;

import gp.core.fitness.Comparison;
import gp.core.fitness.Goal;
import gp.impl.fitness.MultiObjectiveFit;
import gp.core.fitness.MultiObjectiveFitness;
import gp.impl.fitness.SingleObjectiveFit;
import gp.core.fitness.SingleObjectiveFitness;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MultiObjectiveTests {
    @Test
    public void testParetoRankings() {
        final List<MultiObjectiveFit> fitnesses = Stream.of(
                Stream.of(1.0, 2.0),
                Stream.of(2.0, 1.0),
                Stream.of(1.5, 1.5),
                Stream.of(3.0, 3.0)
        ).map(fs -> fs.map(
                f -> (SingleObjectiveFitness) SingleObjectiveFit.of(f, Goal.MINIMIZE)).toList()
                ).map(MultiObjectiveFit::of)
                .toList();
        final Map<Integer, List<MultiObjectiveFit>> ranks = MultiObjectiveFitness.paretoRanks(fitnesses, i -> i);

        assertEquals(
                List.of(fitnesses.getLast()),
                ranks.get(1)
        );
    }

    @Test
    public void testMOFFitnessSorting() {
        final List<MultiObjectiveFit> fitnesses = createRandomFitnesses();

        for (final MultiObjectiveFit fit1 : fitnesses) {
            assertEquals(Comparison.EQUAL, fit1.compareWith(fit1));
            for (final MultiObjectiveFit fit2 : fitnesses) {
                assertEquals(
                        fit1.compareWith(fit2),
                        fit2.compareWith(fit1).flip()
                );
                assertEquals(
                        Math.signum(fit1.compareWith(fit2).ord()),
                        Math.signum(fit2.compareWith(fit1).ord()) * -1,
                        .0001
                );

            }
        }
    }

    @Test
    public void fuzzTestParetoRankings() {
        final List<MultiObjectiveFit> fitnesses = createRandomFitnesses();
        final Map<Integer, List<MultiObjectiveFit>> ranks = MultiObjectiveFitness.paretoRanks(fitnesses, i -> i);

        for (final List<MultiObjectiveFit> rank : ranks.values()) {
            for (final MultiObjectiveFit multiObjectiveFit : rank) {
                for (final MultiObjectiveFit other : rank) {
                    assertEquals(Comparison.EQUAL, multiObjectiveFit.compareWith(other));
                }
            }
        }
    }

    static List<MultiObjectiveFit> createRandomFitnesses() {
        final Random random = new Random();
        return IntStream.range(0, 1000)
                .mapToObj(i -> IntStream
                        .range(0, 2)
                        .mapToObj(ignored -> random.nextInt(-100, 100))
                ).map(fs -> fs.map(f -> SingleObjectiveFit.of(Double.valueOf(f), Goal.MINIMIZE))
                        .map(SingleObjectiveFitness.class::cast)
                        .toList())
                .map(MultiObjectiveFit::new)
                .toList();
    }
}

// Source - https://stackoverflow.com/a/35000727
// Posted by Gili, modified by community. See post 'Timeline' for change history
// Retrieved 2026-03-09, License - CC BY-SA 3.0

