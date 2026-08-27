package gp.impl.genetic_operators;

import gp.core.initializer.PrimitiveSet;
import gp.core.initializer.TypedTerminal;
import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.ImmutableNonTerminal;
import gp.impl.individual.typed_tree.ImmutableTerminal;
import gp.impl.individual.typed_tree.MutableNode;
import gp.impl.individual.typed_tree.MutableNonTerminal;
import gp.impl.individual.typed_tree.Node;
import gp.impl.initializers.NodeInitialiser;
import gp.impl.selectors.random.RandomSampler;
import utils.Updater;
import utils.operators.UnaryOperator;
import utils.random.RandomSource;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Subtree mutation operator that replaces a subtree
 * with a new randomly generated one. Guaranteed to
 *
 * @param <T> The terminal input type
 * @param <Out> The output type
 * @param random The random number generator
 * @param primitiveSet The set of terminals and non-terminals.
 * @param depthLimit Maximum depth for generated subtrees
 * @param attemptLimit Maximum attempts to generate a valid subtree
 */
public record SubtreeMutation<T, Out>(
    RandomSource random,
    PrimitiveSet<T> primitiveSet,
    int depthLimit,
    int attemptLimit
) implements UnaryOperator<
    Node<T, Out, ?, ?, ?>,
    List<ImmutableNode<T, Out, ?, ?>>
> {

    /**
     * Creates a SubtreeMutation operator.
     *
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
        return new SubtreeMutation<>(random, primitiveSet, maxDepth, maxTries);
    }

    /**
     * Applies the mutation operator to a node.
     *
     * @param root The root node to mutate
     * @return A mutated immutable copy of the tree
     */
    @Override
    @SuppressWarnings("unchecked") // Type erasure :(
    public List<ImmutableNode<T, Out, ?, ?>> produce(
        final Node<T, Out, ?, ?, ?> root
    ) {
        for (int i = 0; i<this.attemptLimit; i++) {
            final Optional<ImmutableNode<T, Out, ?, ?>> node = switch (
                root.immutableCopy()
            ) {
                case ImmutableTerminal<?, ?> term -> randomTerminal(
                    (Class<Out>) term.returnType()
                );
                case ImmutableNonTerminal<?, ?, ?, ?> nonTerminal ->
                    mutateSubTree((ImmutableNonTerminal<T, Out, ?, ?>) nonTerminal);
            };

            if (node.isPresent()) {
                return List.of(node.get());
            }
        }

        return List.of(root.immutableCopy());
    }

    // Another type erasure L
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Optional<ImmutableNode<T, Out, ?, ?>> mutateSubTree(
        final ImmutableNonTerminal<T, Out, ?, ?> root
    ) {
        final boolean shouldMutateRoot = random.nextInt(0, root.size()) == 0;
        if (shouldMutateRoot) {
            return this.createSubTree(depthLimit, root.returnType());
        }
        // Select a random non-terminal node to mutate
        final List<MutableNonTerminal> nonTerminals = root.mutableCopy().stream()
            .filter(n -> n instanceof MutableNonTerminal)
            .map(n -> (MutableNonTerminal) n)
            .toList();

        final MutableNonTerminal mutationPoint = RandomSampler.sampleOrThrow(
            nonTerminals, random
        );

        final int depthOfMutationPoint = root.depth() - mutationPoint.depth();
        // Unsure why this needs to be a variable and can't be inlined :(
        final Updater<MutableNode<T, ?, ?, ?>> updater = t -> createSubTree(
            depthLimit - depthOfMutationPoint,
            t.returnType()
        ).orElseThrow().mutableCopy();
        try {
            mutationPoint.replaceChild(
                random.nextInt(0, mutationPoint.combiner().arity()),
                updater
            );
            return Optional.of(root);
        } catch (NoSuchElementException _) {
            return Optional.empty();
        }
    }


    private <OutputType> Optional<ImmutableNode<T, OutputType, ?, ?>> createSubTree(
        final int maxDepthParam,
        final Class<OutputType> returnType
    ) {
        return NodeInitialiser.grow(
            random, primitiveSet, 1,
            attemptLimit, maxDepthParam, Objects.requireNonNull(returnType)
        ).recursivelyConstructIndividual(0, returnType);
    }


    private <R> Optional<ImmutableNode<T, R, ?, ?>> randomTerminal(Class<R> returnType) {
        final TypedTerminal<T, R> term = RandomSampler.sampleOrThrow(
            primitiveSet.terminalsOfType(returnType),
            this.random
        );

        return Optional.of(Node.term(term.name(), term.terminal(), term.returnType()));
    }
}
