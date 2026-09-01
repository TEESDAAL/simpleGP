package gp.fitness;

import gp.Population;
import gp.core.fitness.Comparison;
import gp.core.fitness.Goal;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.AssessedIndividual;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.initializers.Initializers;
import gp.impl.selectors.Elitism;
import org.junit.jupiter.api.Test;
import utils.random.RandomSource;
import utils.stream_utils.StreamZipper;

import java.util.Comparator;

import static gp.initializers.InitialiserTest.primitiveSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleObjectiveTests {
    final Population<SingleObjectiveFitness> pop = Population.of(
        SingleObjectiveFitness.of(10, Goal.MINIMIZE),
        SingleObjectiveFitness.of(2, Goal.MINIMIZE),
        SingleObjectiveFitness.of(2.0, Goal.MINIMIZE),
        SingleObjectiveFitness.of(4.3, Goal.MINIMIZE),
        SingleObjectiveFitness.of(1.27, Goal.MINIMIZE),
        SingleObjectiveFitness.of(12.20, Goal.MINIMIZE)
    );
    @Test
    public void testBehaviour() {
        assertEquals(
            Comparison.BETTER,
            pop.first().compare(pop.last())
        );
        assertEquals(
            Comparison.WORSE,
            SingleObjectiveFitness.of(
                3,
                Goal.MAXIMIZE
            ).compare(SingleObjectiveFitness.of(
                12,
                Goal.MAXIMIZE
            ))
        );
    }
    @Test
    public void testSorting() {
        assertEquals(
            pop.stream().sorted(Comparator.comparingDouble(SingleObjectiveFitness::score).reversed())
                .toList(),
            pop.stream().sorted(
                SingleObjectiveFitness.directComparison().fromPopulation(pop)
            ).toList()
        );

        final var localSorter = SingleObjectiveFitness.directComparison().fromPopulation(pop);

        assertEquals(
            pop.stream().sorted(Comparator.comparingDouble(SingleObjectiveFitness::score).reversed())
                .toList(),
            pop.stream()
                .sorted(localSorter).toList()
        );
    }

    @Test
    public void elitismTest() {
        final var elitism = Elitism.<
            Double, String,
            SingleTreeIndividual<Double, String>,
            SingleObjectiveFitness
            >of(
            3,
            SingleObjectiveFitness.directComparison()
        );
        final Population<SingleTreeIndividual<Double, String>> inds = Initializers.grow(
            RandomSource.of(12), primitiveSet,
            6, 100, 8, String.class
        ).initialize();
        final Population<
            AssessedIndividual<
                Double, String,
                SingleTreeIndividual<Double, String>,
                SingleObjectiveFitness
                >
            > evals = StreamZipper.zip(
            inds.stream(),
            pop.stream(),
            AssessedIndividual::of
        ).collect(Population.toPopulation());

        System.out.println(
            elitism.selectorFrom(
                evals.individuals()
            ).sample());

    }
}
