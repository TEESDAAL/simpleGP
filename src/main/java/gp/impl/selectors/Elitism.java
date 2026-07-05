package gp.impl.selectors;

import gp.core.breeder.SelectionMechanism;
import gp.core.fitness.Fitness;
import gp.core.individual.EvaluatedIndividual;
import gp.core.individual.Individual;
import gp.core.selectors.Sampler;
import utils.Preconditions;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;

/**
 * Elitism selector that always selects the best individual.
 * @param elitismCount The number of elite individuals to select
 * @param fitnessComparatorFunction A function that produces a fitness comparator
 *     from a collection of evaluated individuals.
 *     Note this has to produce a comparator from the list of individuals
 *      to allow for pareto dominance relations
 * @param <ReturnType> The return type
 * @param <TerminalHolder> The terminal type
 * @param <Ind> The individual type
 * @param <Fit> The fitness type
 */
public record Elitism<
        TerminalHolder, ReturnType,
        Ind extends Individual<TerminalHolder, ReturnType>,
        Fit extends Fitness<Fit>
>(
        int elitismCount,
        Function<
                Collection<EvaluatedIndividual<
                        TerminalHolder, ReturnType, Ind, Fit
                >>,
                Comparator<Fit>
        > fitnessComparatorFunction
) implements SelectionMechanism<
        EvaluatedIndividual<TerminalHolder, ReturnType, Ind, Fit>,
        Collection<EvaluatedIndividual<TerminalHolder, ReturnType, Ind, Fit>>
> {
    /**
     * Compact constructor to validate parameters.
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
     * Creates an elitism selector with the given elitism count
     * and fitness comparator function.
     * @param elitismCount The number of elite individuals to select
     * @param fitnessComparatorFunction A function that produces a fitness comparator
     *     from a collection of evaluated individuals.
     * @param <R> The return type
     * @param <T> The terminal type
     * @param <I> The individual type
     * @param <F> The fitness type

     */
    public static <
            T, R,
            I extends Individual<T, R>,
            F extends Fitness<F>
    > Elitism<T, R, I, F> of(
            int elitismCount,
            Function<
                    Collection<EvaluatedIndividual<T, R, I, F>>,
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
     * @param items The population to select from
     * @return A selector that returns the best individual
     */
    @Override
    public Sampler<
                    Collection<EvaluatedIndividual<TerminalHolder, ReturnType, Ind, Fit>>
            > selectorFrom(
            final Collection<
                    EvaluatedIndividual<TerminalHolder, ReturnType, Ind, Fit>
            > items
    ) {
        Comparator<Fit> fitnessComparator = fitnessComparatorFunction.apply(items);
        return () -> items.stream()
                .sorted((e1, e2) -> fitnessComparator.compare(
                        e2.fitness(), e1.fitness()
                )).limit(elitismCount)
                .toList();
    }
}
