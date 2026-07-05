package gp.impl.initializers;

import gp.core.initializers.IndividualInitializer;
import gp.core.initializers.Initializer;
import gp.core.initializers.TypedNonTerminal;
import gp.core.initializers.TypedTerminal;
import gp.impl.individual.SingleTreeIndividual;
import utils.random.RandomSource;

import java.util.List;

public interface Initializers {
    /**
     * Creates an initializer with grow method (probabilistic termination).
     * @param <T> The terminal type
     * @param <R> The return type
     * @param random The random generator
     * @param terminals Map of terminals by return type
     * @param nonTerminals Map of non-terminals by return type
     * @param populationSize The population size
     * @param maxTries Maximum creation attempts
     * @param maxDepth Maximum tree depth
     * @param returnType The return type class
     * @return A new initializer using the grow method
     */
    static <T, R> IndividualInitializer<SingleTreeIndividual<T, R>> grow(
        RandomSource random,
        List<TypedTerminal<T, ?>> terminals,
        List<TypedNonTerminal<?, ?>> nonTerminals,
        int populationSize,
        int maxTries,
        int maxDepth,
        Class<R> returnType
    ) {
        return NodeInitializer.grow(
            random, terminals, nonTerminals,
            populationSize, maxTries, maxDepth,
            returnType
        ).wrap(SingleTreeIndividual::of);
    }

    /**
     * Creates an initializer with full method (terminates at max depth).
     * @param <T> The terminal type
     * @param <R> The return type
     * @param random The random generator
     * @param terminals Map of terminals by return type
     * @param nonTerminals Map of non-terminals by return type
     * @param populationSize The population size
     * @param maxTries Maximum creation attempts
     * @param maxDepth Maximum tree depth
     * @param returnType The return type class
     * @return A new initializer using the full method
     */
    static <T, R> IndividualInitializer<SingleTreeIndividual<T, R>> full(
        final RandomSource random,
        final List<TypedTerminal<T, ?>> terminals,
        final List<TypedNonTerminal<?, ?>> nonTerminals,
        final int populationSize,
        final int maxTries,
        final int maxDepth,
        final Class<R> returnType
    ) {
        return NodeInitializer.full(
            random, terminals, nonTerminals,
            populationSize, maxTries, maxDepth,
            returnType
        ).wrap(SingleTreeIndividual::of);
    }

    /**
     * Creates an initializer with using ramped half-and-half.
     * @param <T> The terminal type
     * @param <R> The return type
     * @param random The random generator
     * @param terminals Map of terminals by return type
     * @param nonTerminals Map of non-terminals by return type
     * @param populationSize The population size
     * @param maxTries Maximum creation attempts
     * @param maxDepth Maximum tree depth
     * @param returnType The return type class
     * @return A new initializer using the full method
     */
    static <T, R> Initializer<SingleTreeIndividual<T, R>> rampedHalfAndHalf(
        final int maxDepth,
        final RandomSource random,
        final List<TypedTerminal<T, ?>> terminals,
        final List<TypedNonTerminal<?, ?>> nonTerminals,
        final int populationSize,
        final int maxTries,
        final Class<R> returnType
    ) {
        return new RampedHalfAndHalf<>(
            maxDepth, random, terminals, nonTerminals,
            populationSize, maxTries, returnType
        );
    }
}
