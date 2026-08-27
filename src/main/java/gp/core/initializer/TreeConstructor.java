package gp.core.initializer;

import gp.core.breeder.SimpleSelectionMechanism;
import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.Node;
import gp.impl.individual.typed_tree.NodeCreator;
import gp.impl.selectors.random.RandomSampler;
import utils.random.RandomSource;

import java.util.Objects;
import java.util.Optional;

/**
 * Interface for constructing genetic programming trees recursively.
 * @param <T> The terminal input type
 * @param <R> The return/output type
 */
public interface TreeConstructor<T, R>
    extends IndividualInitialiser<ImmutableNode<T, R, ?, ?>> {
    /**
     * Gets the random number generator.
     *
     * @return The random generator
     */
    RandomSource random();

    /**
     * @return The selection mechanism for terminals.
     *  By default, picks a terminal uniformly.
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
        ImmutableNode<T, ReturnType, ?, ?>
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

        final TreeConstructor<T, R> constructor = this;
        return RandomSampler.sample(
            this.primitiveSet().validNonTerminals(returnType),
            this.random()
        ).flatMap(n -> n.function().toNode(
            n.name(),
            new NodeCreator<>() {
                @Override
                public <NewR> Optional<ImmutableNode<T, NewR, ?, ?>> get(
                    Class<NewR> returnType
                ) {
                    return constructor.recursivelyConstructIndividual(
                        currentDepth + 1,
                        Objects.requireNonNull(returnType)
                    );
                }
            }
        ));
    }
}
