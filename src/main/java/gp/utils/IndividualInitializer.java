package gp.utils;

import gp.Population;
import gp.breeder.Initializer;
import gp.breeder.Parallelizeable;

import java.util.stream.IntStream;

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
//        return Population.of(
//                this.generateN(this::createIndividual, this.populationSize())
//                        .toList()
//        );
        return Population.of(
                IntStream.range(0, this.populationSize())
                        .mapToObj(ignored -> this.createIndividual())
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


