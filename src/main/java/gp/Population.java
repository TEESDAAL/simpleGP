package gp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * Converts the population to a stream of individuals.
     * @return A stream of individuals in the population
     */
    public Stream<I> stream() {
        return individuals.stream();
    }

    /**
     * A collector that collects a stream of individuals into a population.
     * @return A collector that collects a stream of individuals into a population
     * @param <I> The individual type.
     */
    public static <I> Collector<I, ?, Population<I>> toPopulation() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                Population::of
        );
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
     * Gets the index of the specified individual in the population.
     * @param individual The individual to find
     * @return The index of the individual, or -1 if not found
     */
    public int indexOf(I individual) {
        return this.individuals.indexOf(individual);
    }

    /**
     * Gets a subset of the population from the specified indices.
     *      Provides a view of the population between the specified indices,
     *      this means that changes to the subpopulation will affect the
     *      original population and vice versa.
     * @param fromIndex The starting index (inclusive)
     * @param toIndex The ending index (exclusive)
     * @return A new population containing the individuals in the specified range
     */
    public Population<I> subPopulation(int fromIndex, int toIndex) {
        return new Population<>(individuals.subList(fromIndex, toIndex));
    }

    /**
     * Gets the size of the population.
     * @return The number of individuals in the population
     */
    public int size() {
        return individuals.size();
    }

    /**
     * @return if the population is empty (contains no individuals)
     */
    public boolean isEmpty() {
        return this.individuals.isEmpty();
    }
}
