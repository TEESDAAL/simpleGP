package gp.core.statistics;

import gp.Population;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface for collecting statistics from populations.
 * @param <I> The individual type in the population
 * @param <R> The statistic result type
 */
public interface Statistic<I, R> extends SideEffect<Population<I>> {
    /**
     * Performs the side effect and returns the population unchanged.
     *
     * @param population the population to process
     * @return the same population
     */
    @Override
    default Population<I> sideEffect(Population<I> population) {
        log().accept(statistic(population));
        return population;
    }

    /**
     * Returns the consumer used to handle the statistic output.
     *
     * @return the consumer of the log output
     */
    default Consumer<R> log() {
        return System.out::println;
    }

    /**
     * Logs statistics about the population.
     *
     * @param population the population to analyze
     * @return the statistic result
     */
    R statistic(Population<I> population);

    /**
     * Creates a statistic from a converter and combiner.
     *
     * @param converter converts individuals to intermediate values
     * @param combiner combines the intermediate values into a result
     * @param <I> the individual type
     * @param <T> the intermediate type
     * @param <R> the statistic result type
     * @return the created statistic
     */
    static <I, T, R> Statistic<I, R> of(
            Function<I, T> converter,
            Function<List<T>, R> combiner
    ) {
        return population -> combiner.apply(
            population.stream().map(converter).toList()
        );
    }
}
