package gp.impl.selectors;

import gp.core.breeder.SelectionMechanism;
import gp.core.fitness.Fitness;
import gp.core.individual.EvaluatedIndividual;
import gp.core.individual.Individual;
import gp.core.selectors.Sampler;
import utils.Preconditions;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Elitism selector that always selects the best individual.
 *
 * @param <ReturnType> The return type
 * @param <TerminalHolder> The terminal type
 * @param <Ind> The individual type
 * @param <Fit> The fitness type
 * @param elitismCount The number of elite individuals to select
 * @param fitnessComparatorFunction A function that produces a fitness comparator
 */
public record Elitism<
        TerminalHolder, ReturnType,
        Ind extends Individual<TerminalHolder, ReturnType>,
        Fit extends Fitness<Fit>
>(
        int elitismCount,
        Function<
                List<EvaluatedIndividual<
                        TerminalHolder, ReturnType, Ind, Fit
                >>,
                Comparator<Fit>
        > fitnessComparatorFunction
) implements SelectionMechanism<
        EvaluatedIndividual<TerminalHolder, ReturnType, Ind, Fit>,
        List<EvaluatedIndividual<TerminalHolder, ReturnType, Ind, Fit>>
> {
    /**
     * Compact constructor to validate parameters.
     *
     * @param elitismCount The number of elite individuals to select
     * @param fitnessComparatorFunction A function that produces a fitness comparator
     */
    public Elitism {
        Preconditions.assertTrue(
                elitismCount > 0,
                "elitismCount must be > 0"
        );
    }

    /**
     * Creates an elitism selector with the given elitism count and comparator.
     *
     * @param elitismCount the number of elite individuals to select
     * @param fitnessComparatorFunction a function that produces a fitness comparator
     *     from a collection of evaluated individuals
     * @param <T> the terminal type
     * @param <R> the return type
     * @param <I> the individual type
     * @param <F> the fitness type
     * @return a new elitism selector
     */
    public static <T, R, I extends Individual<T, R>, F extends Fitness<F>>
    Elitism<T, R, I, F> of(
            final int elitismCount,
            final Function<
                    List<EvaluatedIndividual<T, R, I, F>>,
                    Comparator<F>
            > fitnessComparatorFunction
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
    public Sampler<List<EvaluatedIndividual<TerminalHolder, ReturnType, Ind, Fit>>> selectorFrom(
        final List<EvaluatedIndividual<
            TerminalHolder,
            ReturnType,
            Ind,
            Fit
        >> items
    ) {
        final Comparator<Fit> fitnessComparator = fitnessComparatorFunction.apply(
            items
        );

        return () -> items.stream()
                .sorted((e1, e2) -> fitnessComparator.compare(
                    e2.fitness(), e1.fitness()
                )).limit(elitismCount)
                .toList();
    }
}
