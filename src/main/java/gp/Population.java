package gp;

import java.util.ArrayList;
import java.util.List;

/**
 * A record representing a population of individuals.
 * @param <I> The type of individuals in the population
 * @param individuals The list of individuals
 */
public record Population<I>(List<I> individuals) {
    /**
     * Creates a population with an immutable copy of the individuals list.
     */
    public Population {
        individuals = List.copyOf(individuals);
    }

    /**
     * Creates a new population from a list of individuals.
     * @param <I> The type of individuals
     * @param individuals The list of individuals
     * @return A new population containing the provided individuals
     */
    public static <I> Population<I> of(final List<I> individuals) {
        return new Population<>(individuals);
    }

    /**
     * Combines this population with another population.
     * @param other The other population to combine with
     * @return A new population containing individuals from both populations
     */
    public Population<I> combine(final Population<I> other) {
        List<I> newIndividuals = new ArrayList<>(individuals);
        newIndividuals.addAll(other.individuals);
        return new Population<>(newIndividuals);
    }

    /**
     * Gets the first individual in the population.
     * @return The first individual
     */
    public I getFirst() {
        return individuals.getFirst();
    }

    /**
     * Gets the individual at the specified index.
     * @param index The index of the individual to retrieve
     * @return The individual at the specified index
     */
    public I get(final int index) {
        assert index >= 0 && index < individuals.size();
        return individuals.get(index);
    }

    /**
     * Gets the size of the population.
     * @return The number of individuals in the population
     */
    public int size() {
        return individuals.size();
    }
}
