package gp.core.initializer;

import gp.core.breeder.SimpleSelectionMechanism;
import gp.impl.selectors.random.RandomSampler;
import gp.impl.individual.tree.ImmutableNode;
import gp.impl.individual.tree.Node;
import utils.random.RandomSource;

import java.util.Optional;

/**
 * Interface for constructing genetic programming trees recursively.
 * @param <T> The terminal input type
 * @param <R> The return/output type
 */
public interface TreeConstructor<T, R>
    extends IndividualInitialiser<ImmutableNode<T, ?, R, ?, ?>> {
    /**
     * Gets the random number generator.
     *
     * @return The random generator
     */
    RandomSource random();

    /**
     * @return The selection mechanism for terminals.
     *  By default picks a terminal uniformly.
     * @param <A> The return type of the terminals.
     */
    default <A> SimpleSelectionMechanism<TypedTerminal<T, A>> terminalSelector() {
        return terminals -> RandomSampler.ofNonEmpty(terminals, this.random());
    }

    /**
     * Gets the primitive set, i.e. the map of terminals and non-terminals.
     * @return Map from return types to terminal lists.
     */
    PrimitiveSet<T> primitiveSet();

    /**
     * Checks if tree construction should terminate at this depth.
     *
     * @param depth The current depth
     * @return True if construction should terminate
     */
    boolean shouldTerminate(int depth);

    /**
     * Recursively constructs a tree node of the given return type.
     *
     * @param <ReturnType> The return type
     * @param currentDepth The current depth in the tree
     * @param returnType   The desired return type
     * @return An optional containing the constructed node if
     * successful
     */
    default <ReturnType> Optional<
        ImmutableNode<T, ?, ReturnType, ?, ?>
    > recursivelyConstructIndividual(
        final int currentDepth,
        final Class<ReturnType> returnType
    ) {
        if (shouldTerminate(currentDepth)) {
            return RandomSampler.sample(
                    this.primitiveSet().terminalsOfType(returnType),
                    this.random()
            ).map(term -> Node.term(
                term.name(), term.terminal(), term.returnType()
            ));
        }

        return RandomSampler.sample(
                this.primitiveSet().validNonTerminals(returnType),
                this.random()
        ).flatMap(nonTerm -> buildNonTerminal(
                nonTerm, currentDepth
        ));
    }

    /**
     * Create a non-terminal node.
     * @param typedNonTerminal The typed non-terminal to create a node for.
     * @param currentDepth The current depth in the tree.
     * @return An optional containing the constructed node if successful.
     * @param <I> The input type for the non-terminal.
     * @param <ReturnType> The return type of the non-terminal.
     */
    default <I, ReturnType> Optional<
        ImmutableNode<T, ?, ReturnType, ?, ?>
    > buildNonTerminal(
        TypedNonTerminal<I, ReturnType> typedNonTerminal,
        int currentDepth
    ) {
        @SuppressWarnings("unchecked")
        final ImmutableNode<T, ?, I, ?, ?>[] children = (ImmutableNode<T, ?, I, ?, ?>[])
            new ImmutableNode[typedNonTerminal.nonTerminal().arity()];
        for (int i = 0; i < typedNonTerminal.nonTerminal().arity(); i++) {
            final var child = recursivelyConstructIndividual(
                    currentDepth + 1,
                    typedNonTerminal.inputType()
            );
            if (child.isEmpty()) {
                return Optional.empty();
            }
            children[i] = child.get();
        }

        return Optional.of(Node.nonTerm(
                typedNonTerminal.name(),
                typedNonTerminal.nonTerminal(),
                children,
                typedNonTerminal.inputType(),
                typedNonTerminal.returnType()
        ));

    }
}
