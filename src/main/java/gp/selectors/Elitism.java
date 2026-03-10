package gp.selectors;

import gp.breeder.SelectorFrom;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;
import gp.statistics.Selector;
import gp.utils.Preconditions;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Elitism selector that always selects the best individual.
 * @param elitismCount The number of elite individuals to select
 * @param fitnessComparatorFunction A function that produces a fitness comparator
 *     from a collection of evaluated individuals.
 * @param <R> The return type
 * @param <T> The terminal type
 * @param <I> The individual type
 * @param <F> The fitness type
 */
public record Elitism<T, R, I extends Individual<T, R>, F extends Fitness<F>>(
                int elitismCount,
                Function<
                        Collection<EvaluatedIndividual<T, R, I, F>>,
                        Comparator<F>
                > fitnessComparatorFunction
) implements SelectorFrom<
        EvaluatedIndividual<T, R, I, F>,
        List<EvaluatedIndividual<T, R, I, F>>
> {

    public Elitism {
        Preconditions.assertTrue(elitismCount > 0, "elitismCount must be > 0");
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
    public Selector<List<EvaluatedIndividual<T, R, I, F>>> prime(
            final Collection<EvaluatedIndividual<T, R, I, F>> items
    ) {
        Comparator<F> fitnessComparator = fitnessComparatorFunction.apply(items);
        return () -> items.stream()
                .sorted((e1, e2) -> fitnessComparator.compare(
                        e2.fitness(), e1.fitness()
                )).limit(elitismCount)
                .toList();
    }
}
