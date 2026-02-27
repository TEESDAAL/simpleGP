package gp.single_tree;

import gp.individual.Individual;
import gp.tree.ImmutableNode;
import gp.tree.Node;
import gp.utils.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * A record representing an individual as a single tree structure.
 * @param <T> The terminal type
 * @param <Out> The output type
 * @param tree The immutable tree representing this individual
 */
public record SingleTreeIndividual<T, Out>(
        ImmutableNode<T, ?, Out, ?, ?> tree
) implements Individual<T, Out> {
    /**
     * Creates a single tree individual from a tree node.
     * @param <Out> The output type
     * @param <T> The terminal type
     * @param individual The tree node
     * @return A new single tree individual
     */
    public static <Out, T> SingleTreeIndividual<T, Out> of(
            final ImmutableNode<T, ?, Out, ?, ?> individual
    ) {
        return new SingleTreeIndividual<>(individual);
    }

    @Override
    public Out evaluate(final T terminals) {
        return this.tree.evaluate(terminals);
    }

    /**
     * Creates an operator that works on single tree individuals by wrapping
     * a node operator.
     * @param <T> The terminal type
     * @param <Out> The output type
     * @param nodeOperator The operator that works on nodes
     * @return An operator that works on single tree individuals
     */
    public static <T, Out>
            Operator<
                SingleTreeIndividual<T, Out>,
                SingleTreeIndividual<T, Out>
            > operator(
            final Operator<
                    Node<T, ?, Out, ?, ?>,
                    ImmutableNode<T, ?, Out, ?, ?>
                    > nodeOperator
    ) {
        return new Operator<>() {
            @Override
            public SingleTreeIndividual<T, Out> produce(
                    final List<SingleTreeIndividual<T, Out>> parents
            ) {
                List<Node<T, ?, Out, ?, ?>> trees
                        = new ArrayList<>(parents.size());
                for (SingleTreeIndividual<T, Out> parent : parents) {
                    trees.add(parent.tree);
                }
                return SingleTreeIndividual.of(
                        nodeOperator.produce(trees)
                );
            }

            @Override
            public Integer size() {
                return nodeOperator.size();
            }
        };
    }
}
