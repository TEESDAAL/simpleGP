package gp.impl.initializers;

import gp.core.initializers.IndividualCreationException;
import gp.core.initializers.TreeConstructor;
import gp.core.initializers.TypedNonTerminal;
import gp.core.initializers.TypedTerminal;
import gp.impl.individual.tree.ImmutableNode;
import utils.Preconditions;
import utils.random.RandomSource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.IntPredicate;

/**
 * Base implementation of tree constructor using configurable
 * termination criteria.
 * @param <T> The terminal input type
 * @param <R> The return type
 * @param random The random number generator
 * @param terminals Map of terminals by return type
 * @param nonTerminals Map of non-terminals by return type
 * @param shouldTerminate Predicate to determine termination
 * @param populationSize The population size to create
 * @param maxTries Maximum attempts to create an individual
 * @param maxDepth Maximum tree depth
 * @param returnType The return type class
 */
public record NodeInitialiser<T, R>(
        RandomSource random,
        List<TypedTerminal<T, ?>> terminals,
        List<TypedNonTerminal<?, ?>> nonTerminals,
        IntPredicate shouldTerminate,
        int populationSize,
        int maxTries,
        int maxDepth,
        Class<R> returnType
) implements TreeConstructor<T, R> {

    /**
     * Creates a BaseInitializer with the given parameters.
     * @throws IllegalArgumentException if the populationSize, or maxTries
     * is negative, or if maxDepth is negative, or if any parameter is null
     */
    public NodeInitialiser {
        List.of(random, terminals, nonTerminals, shouldTerminate, returnType)
                .forEach(Objects::requireNonNull);
        Preconditions.assertTrue(
                maxDepth >= 0, "Max depth must be non-negative"
        );
        Preconditions.assertTrue(
                populationSize >= 0,
                "Population size must be non-negative"
        );
        Preconditions.assertTrue(
                maxTries > 0, "Max tries must be positive"
        );
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
    static <T, R> NodeInitialiser<T, R> full(
        final RandomSource random,
        final List<TypedTerminal<T, ?>> terminals,
        final List<TypedNonTerminal<?, ?>> nonTerminals,
        final int populationSize,
        final int maxTries,
        final int maxDepth,
        final Class<R> returnType
    ) {
        return new NodeInitialiser<>(
            random, terminals, nonTerminals,
            depth -> depth >= maxDepth,
            populationSize, maxTries, maxDepth,
            returnType
        );
    }

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
    public static <T, R> NodeInitialiser<T, R> grow(
        final RandomSource random,
        final List<TypedTerminal<T, ?>> terminals,
        final List<TypedNonTerminal<?, ?>> nonTerminals,
        final int populationSize,
        final int maxTries,
        final int maxDepth,
        final Class<R> returnType
    ) {
        final double probabilityOfSamplingTerminal = terminals.size()
            / ((double) terminals.size() + nonTerminals.size());
        return new NodeInitialiser<>(
            random, terminals, nonTerminals,
            depth -> depth >= maxDepth
                || random.nextDouble()
                < probabilityOfSamplingTerminal,
            populationSize, maxTries, maxDepth,
            returnType
        );
    }



    @Override
    public boolean shouldTerminate(final int depth) {
        return shouldTerminate.test(depth);
    }

    /**
     * Creates an individual by recursively constructing a tree.
     * @return The created individual
     * @throws IndividualCreationException if creation fails after
     *     maxTries attempts
     */
    @Override
    public ImmutableNode<T, ?, R, ?, ?> createIndividual()
            throws IndividualCreationException {
        for (int i = 0; i < maxTries; i++) {
            final Optional<ImmutableNode<T, ?, R, ?, ?>> possibleTree
                    = this.recursivelyConstructIndividual(0, returnType
            );
            if (possibleTree.isPresent()) {
                return possibleTree.get();
            }
        }
        throw new IndividualCreationException(
                "Failed to create an individual after " + maxTries + " attempts."
                        + "\n Check that this is possible with the given"
                        + " terminals & non-terminals"
                        + "\n NON-TERMINALS: " + nonTerminals
                        + "\n TERMINALS: " + terminals
        );
    }

    @Override
    public boolean shouldParallelize() {
        return true;
    }

    @Override
    public int batchSize() {
        return 5;
    }
}

