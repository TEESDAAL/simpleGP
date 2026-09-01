package gp.fitness;

import gp.core.fitness.Comparison;
import gp.core.fitness.Goal;
import gp.impl.fitness.DefaultMultiObjectiveFitness;
import gp.core.fitness.MultiObjectiveFitness;
import gp.impl.fitness.DefaultSingleObjectiveFitness;
import gp.core.fitness.SingleObjectiveFitness;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MultiObjectiveTests {
    @Test
    public void testParetoRankings() {
        final List<DefaultMultiObjectiveFitness> fitnesses = Stream.of(
            Stream.of(1.0, 2.0), // rank 1
            Stream.of(3.0, 3.0), // rank 2
            Stream.of(2.0, 1.0), // rank 1
            Stream.of(1.5, 1.5)  // rank 1
        ).map(fs -> fs.map(
            f -> (SingleObjectiveFitness) DefaultSingleObjectiveFitness.of(f, Goal.MINIMIZE)).toList()
        ).map(DefaultMultiObjectiveFitness::of)
            .toList();
        final Map<Integer, List<DefaultMultiObjectiveFitness>> ranks = MultiObjectiveFitness.paretoRanks(fitnesses, i -> i);

        assertEquals(
            List.of(fitnesses.get(1)),
            ranks.get(1)
        );
    }

    @Test
    public void testMOFFitnessSorting() {
        final List<DefaultMultiObjectiveFitness> fitnesses = createRandomFitnesses();

        for (final DefaultMultiObjectiveFitness fit1 : fitnesses) {
            assertEquals(Comparison.EQUAL, fit1.paretoComparison(fit1));
            for (final DefaultMultiObjectiveFitness fit2 : fitnesses) {
                assertEquals(
                    fit1.paretoComparison(fit2),
                    fit2.paretoComparison(fit1).flip()
                );
                assertEquals(
                    Math.signum(fit1.paretoComparison(fit2).ord()),
                    Math.signum(fit2.paretoComparison(fit1).ord()) * -1,
                    .0001
                );

            }
        }
    }

    @Test
    public void fuzzTestParetoRankings() {
        final List<DefaultMultiObjectiveFitness> fitnesses = createRandomFitnesses();
        final Map<Integer, List<DefaultMultiObjectiveFitness>> ranks = MultiObjectiveFitness.paretoRanks(fitnesses, i -> i);

        for (final List<DefaultMultiObjectiveFitness> rank : ranks.values()) {
            for (final DefaultMultiObjectiveFitness multiObjectiveFit : rank) {
                for (final DefaultMultiObjectiveFitness other : rank) {
                    assertEquals(Comparison.EQUAL, multiObjectiveFit.paretoComparison(other));
                }
            }
        }
    }

    static List<DefaultMultiObjectiveFitness> createRandomFitnesses() {
        final Random random = new Random();
        return IntStream.range(0, 1000)
            .mapToObj(_ -> IntStream
                .range(0, 2)
                .mapToObj(ignored -> random.nextInt(-100, 100))
            ).map(fs -> fs.map(f -> DefaultSingleObjectiveFitness.of(Double.valueOf(f), Goal.MINIMIZE))
                .map(SingleObjectiveFitness.class::cast)
                .toList())
            .map(DefaultMultiObjectiveFitness::new)
            .toList();
    }
}

// Source - https://stackoverflow.com/a/35000727
// Posted by Gili, modified by community. See post 'Timeline' for change history
// Retrieved 2026-03-09, License - CC BY-SA 3.0

