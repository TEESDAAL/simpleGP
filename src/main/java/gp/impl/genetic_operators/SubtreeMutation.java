package gp.impl.genetic_operators;

import gp.core.initializer.PrimitiveSet;
import gp.core.initializer.TypedTerminal;
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
 * @param depthLimit Maximum depth for generated subtrees
 * @param attemptLimit Maximum attempts to generate a valid subtree
 */
public record SubtreeMutation<T, Out>(
        RandomSource random,
        PrimitiveSet<T> primitiveSet,
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
     * @param primitiveSet The set of terminals and non-terminals
     * @param maxDepth Maximum depth
     * @param maxTries Maximum attempts
     * @return A new subtree mutation operator
     */
    public static <T, Out> SubtreeMutation<T, Out> of(
            final RandomSource random,
            final PrimitiveSet<T> primitiveSet,
            final int maxDepth,
            final int maxTries
    ) {
        return new SubtreeMutation<>(
                random, primitiveSet, maxDepth, maxTries
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
        final ImmutableNode<T, ?, Out, ?, ?> node = switch (root) {
            case Terminal<?, ?> term -> {
                final Terminal<T, Out> actualTermTypes = (Terminal<T, Out>) term;
                yield randomTerminal(actualTermTypes.returnType());
            }
            case NonTerminal<?, ?, ?, ?> nonTerminal -> {
                final NonTerminal<T, ?, Out, ?> actualNonTerminalTypes =
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
        final List<MutableNonTerminal<T, ?, ?>> nonTerminals = new ArrayList<>();
        for (final Node<T, ?, ?, ?, ?> node : root.stream().toList()) {
            if (node instanceof MutableNonTerminal<?, ?, ?> nonTerm) {
                nonTerminals.add((MutableNonTerminal<T, ?, ?>) nonTerm);
            }
        }

        final MutableNonTerminal<T, MutationPointInputType, ?> mutationPoint
                = (MutableNonTerminal<T, MutationPointInputType, ?>) RandomSampler.sampleOrThrow(nonTerminals, random);

        final int depthOfMutationPoint = root.depth()
                - mutationPoint.depth();

        final MutableNode<T, ?, MutationPointInputType, ?, ?> subTree
                = this.createSubTree(
                depthLimit - depthOfMutationPoint,
                mutationPoint.inputType()
        );

        mutationPoint.replaceChild(
            RandomSampler.sampleIndex(
                mutationPoint.children(), random
            ),
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
                random, primitiveSet, 1,
                attemptLimit, maxDepthParam, returnType
        ).createIndividual().mutableCopy();
    }


    private <R> ImmutableTerminal<T, R> randomTerminal(final Class<R> returnType) {
        final TypedTerminal<T, R> term = RandomSampler.sampleOrThrow(
                primitiveSet.terminalsOfType(returnType),
                this.random
        );

        return Node.term(term.name(), term.terminal(), term.returnType());
    }
}
