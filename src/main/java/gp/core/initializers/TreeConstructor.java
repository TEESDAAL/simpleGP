package gp.core.initializers;

import gp.impl.selectors.random.RandomSampler;
import gp.impl.individual.tree.ImmutableNode;
import gp.impl.individual.tree.Node;
import utils.random.RandomSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
     * Gets the map of terminals by return type.
     *
     * @return Map from return types to terminal lists
     */
    List<TypedTerminal<T, ?>> terminals();

    /**
     * Gets the map of non-terminals by return type.
     *
     * @return Map from return types to non-terminal lists
     */
    List<TypedNonTerminal<?, ?>> nonTerminals();

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
     * @param <I>          The input type for non-terminals
     * @param <ReturnType> The return type
     * @param currentDepth The current depth in the tree
     * @param returnType   The desired return type
     * @return An optional containing the constructed node if
     * successful
     */
    default <I, ReturnType> Optional<
        ImmutableNode<T, ?, ReturnType, ?, ?>
        > recursivelyConstructIndividual(
        final int currentDepth,
        final Class<ReturnType> returnType
    ) {
        if (shouldTerminate(currentDepth)) {
            return RandomSampler.sample(
                OperatorSelector.validTerminals(
                    this.terminals(), returnType
                ), this.random()
            ).map(term -> Node.term(
                term.name(), term.terminal(), term.returnType()
            ));
        }


        final Optional<TypedNonTerminal<?, ReturnType>> potentialNonTerminal =
            RandomSampler.sample(
                OperatorSelector.validNonTerminals(
                    this.nonTerminals(), returnType
                ),
                this.random()
            );

        if (potentialNonTerminal.isEmpty()) {
            return Optional.empty();
        }

        @SuppressWarnings("unchecked")
        final TypedNonTerminal<I, ReturnType> typedNonTerminal =
            (TypedNonTerminal<I, ReturnType>) potentialNonTerminal.get();

        final List<ImmutableNode<T, ?, I, ?, ?>> children = new ArrayList<>(
            typedNonTerminal.nonTerminal().arity()
        );

        for (int i = 0; i < typedNonTerminal.nonTerminal().arity(); i++) {
            final var child = recursivelyConstructIndividual(
                currentDepth + 1,
                typedNonTerminal.inputType()
            );
            if (child.isEmpty()) {
                return Optional.empty();
            }
            children.add(child.get());
        }

        return Optional.of(Node.nonTerm(
            typedNonTerminal.name(),
            typedNonTerminal.nonTerminal(),
            Collections.unmodifiableList(children),
            typedNonTerminal.inputType(),
            typedNonTerminal.returnType()
        ));
    }
}
