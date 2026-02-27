package gp.genetic_operators;

import gp.initializers.BaseInitializer;
import gp.initializers.OperatorSelector;
import gp.initializers.TypedNonTerminal;
import gp.initializers.TypedTerminal;
import gp.random.RandomSampler;
import gp.tree.ImmutableNode;
import gp.tree.ImmutableNonTerminal;
import gp.tree.ImmutableTerminal;
import gp.tree.MutableNode;
import gp.tree.MutableNonTerminal;
import gp.tree.Node;
import gp.tree.NonTerminal;
import gp.tree.Terminal;
import gp.utils.UnaryOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Subtree mutation operator that replaces a subtree
 * with a new randomly generated one.
 * @param <T> The terminal input type
 * @param <Out> The output type
 * @param randomGen The random number generator
 * @param terminalMap Map of terminals by return type
 * @param nonTerminalMap Map of non-terminals by return type
 * @param depthLimit Maximum depth for generated subtrees
 * @param attemptLimit Maximum attempts to generate a valid subtree
 */
public record SubtreeMutation<T, Out>(
        Random randomGen,
        Map<Class<?>, List<TypedTerminal<T, ?>>> terminalMap,
        Map<Class<?>, List<TypedNonTerminal<?, ?>>> nonTerminalMap,
        int depthLimit,
        int attemptLimit
) implements UnaryOperator<
        Node<T, ?, Out, ?, ?>,
        ImmutableNode<T, ?, Out, ?, ?>
> {

    /**
     * Creates a SubtreeMutation operator.
     * @param <T> The terminal type
     * @param <Out> The output type
     * @param random The random generator
     * @param terminals Map of terminals by return type
     * @param nonTerminals Map of non-terminals by return type
     * @param maxDepth Maximum depth
     * @param maxTries Maximum attempts
     * @return A new subtree mutation operator
     */
    public static <T, Out> SubtreeMutation<T, Out> of(
            final Random random,
            final Map<Class<?>, List<TypedTerminal<T, ?>>> terminals,
            final Map<Class<?>, List<TypedNonTerminal<?, ?>>>
                    nonTerminals,
            final int maxDepth,
            final int maxTries
    ) {
        return new SubtreeMutation<>(
                random, terminals, nonTerminals, maxDepth, maxTries);
    }

    /**
     * Applies the mutation operator to a node.
     * @param root The root node to mutate
     * @return A mutated immutable copy of the tree
     */
    @Override
    @SuppressWarnings("unchecked") // Type erasure :(
    public ImmutableNode<T, ?, Out, ?, ?> produce(
            final Node<T, ?, Out, ?, ?> root
    ) {
        return switch (root) {
            case Terminal<?, ?> term -> {
                Terminal<T, Out> actualTermTypes = (Terminal<T, Out>) term;
                yield randomTerminal(actualTermTypes.returnType());
            }
            case NonTerminal<?, ?, ?, ?> nonTerminal -> {
                NonTerminal<T, ?, Out, ?> actualNonTerminalTypes =
                        (NonTerminal<T, ?, Out, ?>) nonTerminal;
                yield replaceChild(actualNonTerminalTypes.mutableCopy());
            }
        };
    }

    @SuppressWarnings("unchecked")
    private <MutationPointInputType> ImmutableNonTerminal<T, ?, Out>
            replaceChild(
                    final MutableNonTerminal<T, ?, Out> root
            ) {
        List<MutableNonTerminal<T, ?, ?>> nonTerminals = new ArrayList<>();
        for (Node<T, ?, ?, ?, ?> node : root.stream().toList()) {
            if (node instanceof MutableNonTerminal<?, ?, ?> nonTerm) {
                nonTerminals.add((MutableNonTerminal<T, ?, ?>) nonTerm);
            }
        }

        MutableNonTerminal<T, MutationPointInputType, ?> mutationPoint
                = (MutableNonTerminal<T, MutationPointInputType, ?>)
                RandomSampler.sample(nonTerminals, randomGen)
                .orElseThrow(() -> new IllegalStateException(
                        "Tree somehow has no nodes?"));
        int depthOfMutationPoint = root.depth()
                - mutationPoint.depth();
        MutableNode<T, ?, MutationPointInputType, ?, ?> subTree
                = this.createSubTree(
                depthLimit - depthOfMutationPoint,
                mutationPoint.inputType()
        );

        mutationPoint.replaceChild(
                RandomSampler.sampleIndex(
                        mutationPoint.children(), randomGen)
                .orElseThrow(),
                subTree
        );
        return root.immutableCopy();
    }


    private <OutputType> MutableNode<T, ?, OutputType, ?, ?>
            createSubTree(
                    final int maxDepthParam,
                    final Class<OutputType> returnType
            ) {
        return BaseInitializer.grow(
                randomGen, terminalMap, nonTerminalMap, 1,
                attemptLimit, maxDepthParam, returnType
        ).createIndividual().mutableCopy();
    }


    private <R> ImmutableTerminal<T, R> randomTerminal(
            final Class<R> returnType
    ) {
        return RandomSampler.sample(
                OperatorSelector.validTerminals(
                        this.terminalMap, returnType),
                this.randomGen
        ).map(term -> Node.term(term.terminal(), term.returnType()))
                .orElseThrow(() -> new IllegalStateException(
                        "Should be impossible as you can always"
                        + " reselect the same terminal"
                ));
    }
}
