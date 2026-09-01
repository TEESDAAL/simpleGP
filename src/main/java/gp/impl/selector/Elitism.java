package gp.impl.selector;

import gp.Population;
import gp.core.breeder.SelectionMechanism;
import gp.core.fitness.Fitness;
import gp.core.individual.AssessedIndividual;
import gp.core.individual.Individual;
import gp.core.selector.Sampler;
import util.Preconditions;

import java.util.Comparator;
import java.util.List;

/**
 * Elitism selector that always selects the best individual.
 *
 * @param <ReturnType>              The return type
 * @param <TerminalHolder>          The terminal type
 * @param <I>                       The individual type
 * @param <F>                       The fitness type
 * @param elitismCount              The number of elite individuals to select
 * @param fitnessComparatorFunction A function that produces a fitness comparator
 */
public record Elitism<
        TerminalHolder, ReturnType,
        I extends Individual<TerminalHolder, ReturnType>,
        F extends Fitness<F>
>(int elitismCount, Fitness<F> fitnessComparatorFunction) implements SelectionMechanism<
        AssessedIndividual<TerminalHolder, ReturnType, I, F>,
        List<AssessedIndividual<TerminalHolder, ReturnType, I, F>>
> {
    /**
     * Compact constructor to validate parameters.
     *
     * @param elitismCount              The number of elite individuals to select
     * @param fitnessComparatorFunction A function that produces a fitness comparator
     */
    public Elitism {
        Preconditions.assertTrue(
                elitismCount >= 0,
                "elitismCount must be >= 0"
        );
    }

    /**
     * Creates an elitism selector with the given elitism count and comparator.
     *
     * @param elitismCount              the number of elite individuals to select
     * @param fitnessComparatorFunction a function that produces a fitness comparator
     *                                  from a collection of evaluated individuals
     * @param <T>                       the terminal type
     * @param <R>                       the return type
     * @param <I>                       the individual type
     * @param <F>                       the fitness type
     * @return a new elitism selector
     */
    public static <T, R, I extends Individual<T, R>, F extends Fitness<F>>
    Elitism<T, R, I, F> of(
            final int elitismCount,
            final Fitness<F> fitnessComparatorFunction
    ) {
        return new Elitism<>(
                elitismCount,
                fitnessComparatorFunction
        );
    }

    /**
     * Creates a selector that always returns the best individual.
     *
     * @param items the population to select from
     * @return a selector that returns the best individual
     */
    @Override
    public Sampler<List<
            AssessedIndividual<TerminalHolder, ReturnType, I, F>
            >> selectorFrom(
            final List<AssessedIndividual<
                    TerminalHolder,
                    ReturnType,
                    I,
                    F
                    >> items
    ) {
        final Comparator<F> fitnessComparator = fitnessComparatorFunction
                .fromPopulation(Population.of(items).map(AssessedIndividual::fitness));

        return () -> items.stream()
                .sorted((e1, e2) -> fitnessComparator.compare(
                        e1.fitness(), e2.fitness()
                )).limit(elitismCount)
                .toList();
    }
}
