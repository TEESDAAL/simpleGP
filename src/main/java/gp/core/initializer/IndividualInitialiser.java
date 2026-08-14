package gp.core.initializer;

import gp.Population;
import utils.Cache;
import utils.Parallelizeable;

import java.util.function.Function;

/**
 * Initializer for creating populations of individuals.
 * @param <I> The individual type
 */
public interface IndividualInitialiser<I> extends Initialiser<I>, Parallelizeable {
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
        final var self = this;
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

    /**
     * Create an initializer that tries to obtain a unique population.
     * It does this by repolling this until a unique individual is generated,
     *  or until numTries is exceeded.
     * Note: This method determines uniqueness based on .hashCode and .equals
     *  therefore these must be implemented correctly on I.
     * @param numTries The number of times to recreate an individual
     *   before accepting a duplicate
     * @return The new initializer that tries to produce a unique population
     */
    default IndividualInitialiser<I> attemptToEnforceUniqueness(int numTries) {
        final IndividualInitialiser<I> inner = this;
        return new IndividualInitialiser<>() {
            private final Cache<I> createdIndividuals = Cache.empty();
            @Override
            public I createIndividual() {
               return createdIndividuals.repeatUntilAbsent(inner::createIndividual, numTries);
            }

            @Override
            public int populationSize() {
                return inner.populationSize();
            }

            @Override
            public boolean shouldParallelize() {
                return inner.shouldParallelize();
            }
        };
    }
}


