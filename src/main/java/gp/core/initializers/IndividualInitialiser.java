package gp.core.initializers;

import gp.Population;
import utils.Parallelizeable;

import java.util.function.Function;

/**
 * Initializer for creating populations of individuals.
 * @param <I> The individual type
 */
public interface IndividualInitialiser<I>
    extends Initialiser<I>, Parallelizeable {
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
     *
     * @return the population size
     */
    int populationSize();

    /**
     * Wraps initialized individuals with a mapping function.
     *
     * @param function the mapping function
     * @param <U> the wrapped type
     * @return a wrapped initializer
     */
    default <U> IndividualInitialiser<U> wrap(Function<I, U> function) {
        var self = this;
        return new IndividualInitialiser<>() {

            @Override
            public boolean shouldParallelize() {
                return self.shouldParallelize();
            }

            @Override
            public U createIndividual() {
                return function.apply(self.createIndividual());
            }

            @Override
            public int populationSize() {
                return self.populationSize();
            }
        };
    }
}


