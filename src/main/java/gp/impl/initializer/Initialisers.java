package gp.impl.initializer;

import gp.core.initializer.IndividualInitialiser;
import gp.core.primitive_set.PrimitiveSet;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.individual.lineage.Lineage;
import util.random.RandomSource;

/**
 * A utility class which holds common initialisers.
 */
public enum Initialisers {;
    /**
     * Creates an initializer with grow method (probabilistic termination).
     * @param <T> The terminal type
     * @param <R> The return type
     * @param random The random generator
     * @param primitiveSet The set of terminals and non-terminals
     * @param populationSize The population size
     * @param maxTries Maximum creation attempts
     * @param maxDepth Maximum tree depth
     * @param returnType The return type class
     * @return A new initializer using the grow method
     */
    public static <T, R> IndividualInitialiser<SingleTreeIndividual<T, R>> grow(
            RandomSource random,
            PrimitiveSet<T> primitiveSet,
            int populationSize,
            int maxTries,
            int maxDepth,
            Class<R> returnType
    ) {
        final var grow = NodeInitialiser.grow(
                random, primitiveSet,
                populationSize, maxTries, maxDepth,
                returnType
        );
        return grow.wrap(i -> SingleTreeIndividual.of(i, Lineage.progenitor(grow)));
    }

    /**
     * Creates an initializer with full method (terminates at max depth).
     * @param <T> The terminal type
     * @param <R> The return type
     * @param random The random generator
     * @param primitiveSet The set of terminals and non-terminals
     * @param populationSize The population size
     * @param maxTries Maximum creation attempts
     * @param maxDepth Maximum tree depth
     * @param returnType The return type class
     * @return A new initializer using the full method
     */
    public static <T, R> IndividualInitialiser<SingleTreeIndividual<T, R>> full(
            RandomSource random,
            PrimitiveSet<T> primitiveSet,
            int populationSize,
            int maxTries,
            int maxDepth,
            Class<R> returnType
    ) {
        final var full = NodeInitialiser.full(
                random, primitiveSet,
                populationSize, maxTries, maxDepth,
                returnType
        );
        return full.wrap(i -> SingleTreeIndividual.of(i, Lineage.progenitor(full)));
    }

    /**
     * Creates an initializer with using ramped half-and-half.
     * @param <T> The terminal type
     * @param <R> The return type
     * @param random The random generator
     * @param primitiveSet The set of terminals and non-terminals
     * @param populationSize The population size
     * @param maxTries Maximum creation attempts
     * @param maxDepth Maximum tree depth
     * @param returnType The return type class
     * @return A new initializer using the full method
     */
    public static <T, R> RampedHalfAndHalf<T, R> rampedHalfAndHalf(
            int maxDepth,
            RandomSource random,
            PrimitiveSet<T> primitiveSet,
            int populationSize,
            int maxTries,
            Class<R> returnType
    ) {
        return new RampedHalfAndHalf<>(
            maxDepth, random, primitiveSet,
            populationSize, maxTries, returnType
        );
    }
}
