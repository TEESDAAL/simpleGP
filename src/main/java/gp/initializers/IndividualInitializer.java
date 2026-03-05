package gp.initializers;

import gp.Population;
import gp.utils.Parallelizeable;

/**
 * Initializer for creating populations of individuals.
 * @param <I> The individual type
 */
public interface IndividualInitializer<I>
    extends Initializer<I>, Parallelizeable {
    /**
     * Initializes a population of individuals.
     * @return A population containing the created individuals
     */
    @Override
    default Population<I> initialize() {
        return Population.of(
                this.generateN(this::createIndividual, this.populationSize())
                        .toList()
        );
    }

    /**
     * Creates a single individual.
     * @return The created individual
     */
    I createIndividual();

    /**
     * Returns the size of the population to initialize.
     * @return The population size
     */
    int populationSize();
}


