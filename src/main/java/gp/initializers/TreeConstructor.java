package gp.initializers;

import gp.random.RandomSampler;
import gp.tree.ImmutableNode;
import gp.tree.Node;
import gp.utils.IndividualInitializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Interface for constructing genetic programming trees recursively.
 * @param <T> The terminal input type
 * @param <R> The return/output type
 */
public interface TreeConstructor<T, R>
    extends IndividualInitializer<ImmutableNode<T, ?, R, ?, ?>> {
    /**
     * Gets the random number generator.
     * @return The random generator
     */
    Random random();

    /**
     * Gets the map of terminals by return type.
     * @return Map from return types to terminal lists
     */
    Map<Class<?>, List<TypedTerminal<T, ?>>> terminals();

    /**
     * Gets the map of non-terminals by return type.
     * @return Map from return types to non-terminal lists
     */
    Map<Class<?>, List<TypedNonTerminal<?, ?>>> nonTerminals();

    /**
     * Checks if tree construction should terminate at this depth.
     * @param depth The current depth
     * @return True if construction should terminate
     */
    boolean shouldTerminate(int depth);

    /**
     * Recursively constructs a tree node of the given return type.
     * @param <I> The input type for non-terminals
     * @param <ReturnType> The return type
     * @param currentDepth The current depth in the tree
     * @param returnType The desired return type
     * @return An optional containing the constructed node if
     *     successful
     */
    default <I, ReturnType>
            Optional<ImmutableNode<T, ?, ReturnType, ?, ?>>
            recursivelyConstructIndividual(
            final int currentDepth,
            final Class<ReturnType> returnType
    ) {
        if (shouldTerminate(currentDepth)) {
            return RandomSampler.sample(
                    OperatorSelector.validTerminals(
                            this.terminals(), returnType),
                    this.random()
            ).map(term -> Node.term(
                    term.terminal(), term.returnType()));
        }


        Optional<TypedNonTerminal<?, ReturnType>>
                potentialNonTerminal = RandomSampler.sample(
                OperatorSelector.validNonTerminals(
                        this.nonTerminals(), returnType),
                this.random()
        );

        if (potentialNonTerminal.isEmpty()) {
            return Optional.empty();
        }

        @SuppressWarnings("unchecked")
        // Add type I so we can talk about it later...
        TypedNonTerminal<I, ReturnType> nonTerminal =
                (TypedNonTerminal<I, ReturnType>)
                        potentialNonTerminal.get();
        List<ImmutableNode<T, ?, I, ?, ?>> children =
                new ArrayList<>();

        for (int i = 0; i < nonTerminal.nonTerminal().size(); i++) {
            Optional<ImmutableNode<T, ?, I, ?, ?>> child =
                    recursivelyConstructIndividual(
                            currentDepth + 1,
                            nonTerminal.inputType()
                    );
            if (child.isEmpty()) {
                return Optional.empty();
            }
            children.add(child.get());
        }

        return Optional.of(Node.nonTerm(
                nonTerminal.nonTerminal(),
                Collections.unmodifiableList(children),
                nonTerminal.inputType(),
                nonTerminal.returnType()
        ));
    }
}
