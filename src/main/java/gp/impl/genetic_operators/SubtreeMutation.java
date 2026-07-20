package gp.impl.genetic_operators;

import gp.core.initializers.OperatorSelector;
import gp.core.initializers.TypedNonTerminal;
import gp.core.initializers.TypedTerminal;
import gp.impl.selectors.random.RandomSampler;
import gp.impl.individual.tree.ImmutableNode;
import gp.impl.individual.tree.ImmutableNonTerminal;
import gp.impl.individual.tree.ImmutableTerminal;
import gp.impl.individual.tree.MutableNode;
import gp.impl.individual.tree.MutableNonTerminal;
import gp.impl.individual.tree.Node;
import gp.impl.individual.tree.NonTerminal;
import gp.impl.individual.tree.Terminal;
import gp.impl.initializers.NodeInitialiser;
import utils.operators.UnaryOperator;
import utils.random.RandomSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Subtree mutation operator that replaces a subtree
 * with a new randomly generated one.
 * @param <T> The terminal input type
 * @param <Out> The output type
 * @param random The random number generator
 * @param terminals Map of terminals by return type
 * @param nonTerminals Map of non-terminals by return type
 * @param depthLimit Maximum depth for generated subtrees
 * @param attemptLimit Maximum attempts to generate a valid subtree
 */
public record SubtreeMutation<T, Out>(
        RandomSource random,
        List<TypedTerminal<T, ?>> terminals,
        List<TypedNonTerminal<?, ?>> nonTerminals,
        int depthLimit,
        int attemptLimit
) implements UnaryOperator<
        Node<T, ?, Out, ?, ?>,
        List<ImmutableNode<T, ?, Out, ?, ?>>
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
            final RandomSource random,
            final List<TypedTerminal<T, ?>> terminals,
            final List<TypedNonTerminal<?, ?>> nonTerminals,
            final int maxDepth,
            final int maxTries
    ) {
        return new SubtreeMutation<>(
                random, terminals, nonTerminals, maxDepth, maxTries
        );
    }

    /**
     * Applies the mutation operator to a node.
     * @param root The root node to mutate
     * @return A mutated immutable copy of the tree
     */
    @Override
    @SuppressWarnings("unchecked") // Type erasure :(
    public List<ImmutableNode<T, ?, Out, ?, ?>> produce(
            final Node<T, ?, Out, ?, ?> root
    ) {
        ImmutableNode<T, ?, Out, ?, ?> node = switch (root) {
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

        return List.of(node);
    }

    @SuppressWarnings("unchecked")
    private <MutationPointInputType> ImmutableNonTerminal<T, ?, Out> replaceChild(
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
                RandomSampler.sample(nonTerminals, random)
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
                mutationPoint.children(), random
            ).orElseThrow(),
            subTree
        );
        return root.immutableCopy();
    }


    private <OutputType> MutableNode<T, ?, OutputType, ?, ?>
            createSubTree(
                    final int maxDepthParam,
                    final Class<OutputType> returnType
            ) {
        return NodeInitialiser.grow(
                random, terminals, nonTerminals, 1,
                attemptLimit, maxDepthParam, returnType
        ).createIndividual().mutableCopy();
    }


    private <R> ImmutableTerminal<T, R> randomTerminal(
            final Class<R> returnType
    ) {
        return RandomSampler.sample(
                OperatorSelector.validTerminals(
                        this.terminals, returnType
                ),
                this.random
        ).map(term -> Node.term(
                term.name(), term.terminal(), term.returnType()
        )).orElseThrow(() -> new IllegalStateException(
                "Should be impossible as you can always reselect the same terminal"
        ));
    }
}
