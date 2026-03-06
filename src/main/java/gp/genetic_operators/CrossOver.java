package gp.genetic_operators;

import gp.random.RandomSampler;
import gp.tree.ImmutableNode;
import gp.tree.MutableNode;
import gp.tree.Node;
import gp.utils.operators.BinaryOperator;

import java.util.List;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * A crossover operator that takes in 2 trees and performs random subtree crossover.
 * Includes crossover of the root of the tree too.
 * @param random The source of randomness, used to randomly select the root node.
 * @param <T> The type of the root node
 * @param <Out> The return type of in input and output trees
 */
public record CrossOver<T, Out>(Random random) implements BinaryOperator<
        Node<T, ?, Out, ?, ?>,
        List<ImmutableNode<T, ?, Out, ?, ?>>
> {

    @Override
    public List<ImmutableNode<T, ?, Out, ?, ?>> produce(
            Node<T, ?, Out, ?, ?> parent1,
            Node<T, ?, Out, ?, ?> parent2
    ) {
        return helper(
                parent1.mutableCopy(),
                parent2.mutableCopy()
        );
    }

    @SuppressWarnings("unchecked")
    private <R> List<ImmutableNode<T, ?, Out, ?, ?>> helper(
            MutableNode<T, ?, Out, ?, ?> parent1,
            MutableNode<T, ?, Out, ?, ?> parent2
    ) {
        // Handle special case of tree
        // Give this type R so we can talk it
        NodeWithParent<T, R> mutationPoint1 = (NodeWithParent<T, R>)
                getMutationPoint(parent1.mutableCopy());

        NodeWithParent<T, R> mutationPoint2 = getMutationPoint(
                parent2.mutableCopy(),
                mutationPoint1.node().returnType()
        );

        if (mutationPoint1.isRoot()) {
            return handleCrossOverAtRoot(
                    parent1, (NodeWithParent<T, Out>) mutationPoint1, parent2
            );
        }
        if (mutationPoint2.isRoot()) {
            return handleCrossOverAtRoot(
                    parent2, (NodeWithParent<T, Out>) mutationPoint2, parent1
            );
        }

        return regularCrossOver(
                parent1, parent2,
                mutationPoint1,
                mutationPoint2
        );
    }

    private <R> List<ImmutableNode<T, ?, Out, ?, ?>> regularCrossOver(
            MutableNode<T, ?, Out, ?, ?> parent1,
            MutableNode<T, ?, Out, ?, ?> parent2,
            NodeWithParent<T, R> mutationPoint1,
            NodeWithParent<T, R> mutationPoint2
    ) {
        mutationPoint1.parent().orElseThrow().replaceChild(
                mutationPoint1.childIndex().orElseThrow(),
                mutationPoint2.node()
        );

        mutationPoint2.parent().orElseThrow().replaceChild(
                mutationPoint2.childIndex().orElseThrow(),
                mutationPoint1.node()
        );

        return List.of(
                parent1.immutableCopy(),
                parent2.immutableCopy()
        );
    }

    private List<ImmutableNode<T, ?, Out, ?, ?>> handleCrossOverAtRoot(
            MutableNode<T, ?, Out, ?, ?> rootParent,
            NodeWithParent<T, Out> nonRootParentMutationPoint,
            MutableNode<T, ?, Out, ?, ?> nonRootParent
    ) {

        if (nonRootParentMutationPoint.isRoot()) {
            return List.of(
                    rootParent.immutableCopy(),
                    nonRootParent.immutableCopy()
            );
        }

        nonRootParentMutationPoint.parent().orElseThrow().replaceChild(
                nonRootParentMutationPoint.childIndex().orElseThrow(),
                rootParent

        );
        return List.of(
                nonRootParent.immutableCopy(),
                nonRootParentMutationPoint.node().immutableCopy()
        );
    }


    private NodeWithParent<T, ?> getMutationPoint(
            MutableNode<T, ?, Out, ?, ?> parent
    ) {
        return RandomSampler
                .sample(NodeWithParent.collect(parent), random)
                .orElseThrow();
    }


    private <R> NodeWithParent<T, R> getMutationPoint(
            MutableNode<T, ?, Out, ?, ?> parent,
                Class<R> returnType
    ) throws NoSuchElementException {
        @SuppressWarnings("unchecked")
        List<NodeWithParent<T, R>> candidates = NodeWithParent.collect(parent)
                .stream()
                .filter(n -> returnType
                        .isAssignableFrom(n.node().returnType())
                ).map(n -> (NodeWithParent<T, R>) n)
                .toList();

        return RandomSampler
                .sample(candidates, random)
                .orElseThrow();
    }
}

