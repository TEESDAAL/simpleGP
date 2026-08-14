package gp.impl.initializers;

import gp.core.initializer.*;
import gp.impl.individual.SingleTreeIndividual;
import utils.random.RandomSource;

public enum Initializers {
    ;

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
        return NodeInitialiser.grow(
            random, primitiveSet,
            populationSize, maxTries, maxDepth,
            returnType
        ).wrap(SingleTreeIndividual::of);
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
            final RandomSource random,
            final PrimitiveSet<T> primitiveSet,
            final int populationSize,
            final int maxTries,
            final int maxDepth,
            final Class<R> returnType
    ) {
        return NodeInitialiser.full(
            random, primitiveSet,
            populationSize, maxTries, maxDepth,
            returnType
        ).wrap(SingleTreeIndividual::of);
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
    public static <T, R> Initialiser<SingleTreeIndividual<T, R>> rampedHalfAndHalf(
            final int maxDepth,
            final RandomSource random,
            final PrimitiveSet<T> primitiveSet,
            final int populationSize,
            final int maxTries,
            final Class<R> returnType
    ) {
        return new RampedHalfAndHalf<>(
            maxDepth, random, primitiveSet,
            populationSize, maxTries, returnType
        );
    }
}
