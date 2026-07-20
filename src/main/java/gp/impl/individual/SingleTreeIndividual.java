package gp.impl.individual;

import gp.core.individual.Individual;
import gp.impl.individual.tree.ImmutableNode;
import gp.impl.individual.tree.Node;
import utils.operators.Operator;

import java.util.ArrayList;
import java.util.List;


/**
 * A record representing an individual as a single tree structure.
 * @param <T> The terminal type
 * @param <Out> The output type
 * @param tree The immutable tree representing this individual
 */
record SingleTreeIndividualImpl<T, Out>(
    ImmutableNode<T, ?, Out, ?, ?> tree
) implements SingleTreeIndividual<T, Out> {
    @Override
    public String toString() {
        return "SingleTreeIndividual[" + tree.getExpression()+"]";
    }
}


/**
 * An interface representing an individual as a single tree structure.
 * @param <T> The terminal type
 * @param <Out> The output type
 */
public interface SingleTreeIndividual<T, Out> extends Individual<T, Out> {
    /**
     * Creates a single tree individual from a tree node.
     * @param <Out> The output type
     * @param <T> The terminal type
     * @param individual The tree node
     * @return A new single tree individual
     */
    static <Out, T> SingleTreeIndividual<T, Out> of(
            final ImmutableNode<T, ?, Out, ?, ?> individual
    ) {
        return new SingleTreeIndividualImpl<>(individual);
    }

    /**
     * Get the Node this individual wraps.
     * @return The inner node.
     */
    ImmutableNode<T, ?, Out, ?, ?> tree();

    @Override
    default Out evaluate(final T terminals) {
        return this.tree().evaluate(terminals);
    }

    /**
     * Creates an operator that works on single tree individuals by wrapping
     * a node operator.
     * @param <T> The terminal type
     * @param <Out> The output type
     * @param nodeOperator The operator that works on nodes
     * @return An operator that works on single tree individuals
     */
    static <T, Out>
    Operator<
        SingleTreeIndividual<T, Out>,
        List<SingleTreeIndividual<T, Out>>
    > operator(
        Operator<
            Node<T, ?, Out, ?, ?>,
            List<ImmutableNode<T, ?, Out, ?, ?>>
        > nodeOperator
    ) {
        return new Operator<>() {
            @Override
            public List<SingleTreeIndividual<T, Out>> produce(
                    final List<SingleTreeIndividual<T, Out>> parents
            ) {
                final List<Node<T, ?, Out, ?, ?>> trees
                        = new ArrayList<>(parents.size());
                for (final SingleTreeIndividual<T, Out> parent : parents) {
                    trees.add(parent.tree());
                }
                return nodeOperator.produce(trees)
                        .stream()
                        .map(SingleTreeIndividual::of)
                        .toList();
            }

            @Override
            public Integer arity() {
                return nodeOperator.arity();
            }
        };
    }
}
