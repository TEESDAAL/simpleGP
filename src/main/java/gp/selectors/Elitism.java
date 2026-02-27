package gp.selectors;

import gp.breeder.SelectorBuilder;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;
import gp.statistics.Selector;

import java.util.Collection;

/**
 * Elitism selector that always selects the best individual.
 * @param <R> The return type
 * @param <T> The terminal type
 * @param <I> The individual type
 * @param <F> The fitness type
 */
public record Elitism<R, T, I extends Individual<T, R>,
        F extends Fitness<F>>()
        implements SelectorBuilder<EvaluatedIndividual<T, R, I, F>> {
    /**
     * Creates a selector that always returns the best individual.
     * @param items The population to select from
     * @return A selector that returns the best individual
     */
    @Override
    public Selector<EvaluatedIndividual<T, R, I, F>> prime(
            final Collection<EvaluatedIndividual<T, R, I, F>> items) {
        return () -> items.stream().reduce(
                (bestIndividual, currentIndividual) ->
                        switch (currentIndividual.fitness()
                                .compareWith(bestIndividual.fitness())) {
            case BETTER -> currentIndividual;
            case WORSE, EQUAL -> bestIndividual;
        }).orElseThrow();
    }
}
