package gp.core.initializers;

import gp.Population;

import java.util.function.Function;

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

    /// Create a new initializer by simply wrapping the produced individuals in a new type.
    /// @param wrapper The wrapper to convert the produced individuals
    /// @return the wrapped population
    /// @param <U> The produced type of the new initializer.
    default <U> Initializer<U> wrap(Function<I, U> wrapper) {
        return () -> this.initialize().stream()
            .map(wrapper)
            .collect(Population.toPopulation());
    }
}

