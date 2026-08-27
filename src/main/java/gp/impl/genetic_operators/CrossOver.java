package gp.impl.genetic_operators;

import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.MutableNode;
import gp.impl.individual.typed_tree.MutableNonTerminal;
import gp.impl.individual.typed_tree.Node;
import gp.impl.selectors.random.RandomSampler;
import utils.Preconditions;
import utils.operators.BinaryOperator;
import utils.random.RandomSource;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A crossover operator that takes in 2 trees and performs random subtree crossover.
 * Includes crossover of the root of the tree too.
 * @param random The source of randomness, used to randomly select the root node.
 * @param maxDepth The maximum allowed depth after crossover.
 * @param maxTries The maximum number of times to re-try crossover if the produced
 *                 trees are larger than the max-depth. If reached the original trees
 *                 are passed through.
 * @param <T> The type of the root node
 * @param <Out> The return type of in input and output trees
 */
public record CrossOver<T, Out>(
    RandomSource random,
    int maxDepth,
    int maxTries
) implements BinaryOperator<
        Node<T, Out, ?, ?, ?>,
        List<ImmutableNode<T, Out, ?, ?>>
> {
    public CrossOver {
        Preconditions.assertTrue(
            maxDepth >= 1,
            "The max Depth has to include at least 1 node."
        );
        Preconditions.assertTrue(
            maxTries >= 1,
            "We must be able to try to produce an individual at least 1 time."
        );
        Preconditions.allNonNull(random);
    }

    @Override
    public List<ImmutableNode<T, Out, ?, ?>> produce(
        final Node<T, Out, ?, ?, ?> parent1,
        final Node<T, Out, ?, ?, ?> parent2
    ) {
        for (int i=0; i<maxDepth; i++) {
            final List<ImmutableNode<T, Out, ?, ?>> children = helper(
                parent1.mutableCopy(),
                parent2.mutableCopy()
            );
            assert children.size() == 2;
            if (children.getFirst().depth() <= maxDepth
                && children.getLast().depth() <= maxDepth
            ) {
                return children;
            }
        }

        return List.of(
            parent1.immutableCopy(),
            parent2.immutableCopy()
        );
    }

    private List<ImmutableNode<T, Out, ?, ?>> helper(
        final MutableNode<T, Out, ?, ?> parent1,
        final MutableNode<T, Out, ?, ?> parent2
    ) {
        final NodeWithParentOrRoot<T> mutationPoint1 = getMutationPoint(parent1);

        final NodeWithParentOrRoot<T> mutationPoint2 = getMutationPointOfType(
            parent2,
                mutationPoint1.node().returnType()
        );

        if (mutationPoint1.isRoot()) {
            return handleCrossOverAtRoot(
                    parent1, mutationPoint1, parent2
            );
        }
        if (mutationPoint2.isRoot()) {
            return handleCrossOverAtRoot(
                    parent2, mutationPoint2, parent1
            );
        }

        return regularCrossOver(
                parent1, parent2,
                mutationPoint1.notRootOrThrow(),
                mutationPoint2.notRootOrThrow()
        );
    }

    private List<ImmutableNode<T, Out, ?, ?>> regularCrossOver(
        final MutableNode<T, Out, ?, ?> parent1,
        final MutableNode<T, Out, ?, ?> parent2,
        final NodeWithParent<T> mutationPoint1,
        final NodeWithParent<T> mutationPoint2
    ) {
        swapChildren(
            mutationPoint1.parent(), mutationPoint1.indexInParent(),
            mutationPoint2.parent(), mutationPoint2.indexInParent()
        );
        return List.of(
                parent1.immutableCopy(),
                parent2.immutableCopy()
        );
    }

    private static <T> void swapChildren(
        MutableNonTerminal<T, ?, ?, ?> firstParent,
        int child1Index,
        MutableNonTerminal<T, ?, ?, ?> secondParent,
        int child2Index
    ) {
        final MutableNode<T, ?, ?, ?> firstChild = firstParent.getChild(child1Index);
        final MutableNode<T, ?, ?, ?> secondChild = secondParent.getChild(child2Index);
        firstParent.replaceChild(child1Index, _ -> secondChild);
        secondParent.replaceChild(child2Index, _ -> firstChild);
    }

    private List<ImmutableNode<T, Out, ?, ?>> handleCrossOverAtRoot(
        final MutableNode<T, Out, ?, ?> rootParent,
        final NodeWithParentOrRoot<T> nonRootParentMutationPoint,
        final MutableNode<T, Out, ?, ?> nonRootParent
    ) {
        // If both mutation points are the root swapping the trees does nothing :)
        if (nonRootParentMutationPoint.isRoot()) {
            return List.of(
                    rootParent.immutableCopy(),
                    nonRootParent.immutableCopy()
            );
        }

        nonRootParentMutationPoint.parent().orElseThrow().replaceChild(
                nonRootParentMutationPoint.indexInParent().orElseThrow(),
                _ -> rootParent

        );

        //noinspection unchecked
        return List.of(
                nonRootParent.immutableCopy(),
            (ImmutableNode<T, Out, ?, ?>) nonRootParentMutationPoint.node()
                .immutableCopy()
        );
    }


    private NodeWithParentOrRoot<T> getMutationPoint(
        final MutableNode<T, Out, ?, ?> parent
    ) {
        return RandomSampler
                .sampleOrThrow(NodeWithParentOrRoot.collect(parent), random);
    }


    private <R> NodeWithParentOrRoot<T> getMutationPointOfType(
        final MutableNode<T, Out, ?, ?> parent,
        final Class<R> returnType
    ) throws NoSuchElementException {
        assert returnType != null : "Return type cannot be null";
        final List<NodeWithParentOrRoot<T>> candidates = NodeWithParentOrRoot
            .collect(parent)
            .stream()
            .filter(n -> returnType.isAssignableFrom(n.node().returnType()))
            .toList();

        return RandomSampler
                .sampleOrThrow(candidates, random);
    }

}

