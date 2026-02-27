package gp.statistics;

import gp.Population;

/**
 * Interface for collecting statistics from populations.
 * @param <I> The individual type in the population
 */
public interface Statistic<I> extends SideEffect<Population<I>> {
    /**
     * Performs the side effect and returns the population unchanged.
     * @param population The population to process
     * @return The same population
     */
    @Override
    default Population<I> sideEffect(Population<I> population) {
        log(population);
        return population;
    }


    /**
     * Logs statistics about the population.
     * @param population The population to analyze
     */
    void log(Population<I> population);
}
