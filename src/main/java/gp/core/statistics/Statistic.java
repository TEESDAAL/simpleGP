package gp.core.statistics;

import gp.Population;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface for collecting statistics from populations.
 * @param <I> The individual type in the population
 */
public interface Statistic<I, R> extends SideEffect<Population<I>> {
    /**
     * Performs the side effect and returns the population unchanged.
     * @param population The population to process
     * @return The same population
     */
    @Override
    default Population<I> sideEffect(Population<I> population) {
        log().accept(statistic(population));
        return population;
    }

    /// How to handle the output of the log, the default, just outputs it to the console,
    /// However this could be very easily updated to write to a file e.t.c
    /// @return The consumer of the log output.
    default Consumer<R> log() {
        return System.out::println;
    }


    /**
     * Logs statistics about the population.
     * @param population The population to analyze
     */
    R statistic(Population<I> population);

    static <I, T, R> Statistic<I, R> of(Function<I, T> converter, Function<List<T>, R> combiner) {
        return population -> combiner.apply(population.stream().map(converter).toList());
    }
}
