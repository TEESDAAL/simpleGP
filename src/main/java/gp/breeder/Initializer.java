package gp.breeder;

import gp.Population;

/**
 * Interface for initializing populations.
 * @param <I> The individual type
 */
public interface Initializer<I> {
    /**
     * Initializes a population.
     * @return The initialized population
     */
    Population<I> initialize();
}

