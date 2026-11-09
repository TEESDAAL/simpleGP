package gp.impl.individual;

import gp.core.individual.Individual;
import gp.impl.individual.lineage.Lineage;
import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.Node;
import util.operator.Operator;
import util.sequence.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A record representing an individual as a single tree structure.
 */
final class SingleTreeIndividualImpl<T, Out> implements SingleTreeIndividual<T, Out> {
    private final ImmutableNode<T, Out, ?, ?> tree;
    private final Lineage lineage;

    /**
     * @param tree  The immutable tree representing this individual
     */
    SingleTreeIndividualImpl(
            ImmutableNode<T, Out, ?, ?> tree,
            Lineage lineage
    ) {
        this.tree = tree;
        this.lineage = lineage;
    }

    <P extends Individual<?, ?>> SingleTreeIndividualImpl(
            ImmutableNode<T, Out, ?, ?> tree,
            List<P> parents,
            Operator<P, Sequence<SingleTreeIndividual<T, Out>>> operator
    ) {
        this.tree = tree;
        this.lineage = Lineage.descendant(
                operator,
                parents
        );
    }

    @Override
    public String toString() {
        return "SingleTreeIndividual[" + tree.toLispExpression() + "]";
    }


    @Override
    public Out evaluate(final T terminals) {
        return this.tree.evaluate(terminals);
    }

    @Override
    public ImmutableNode<T, Out, ?, ?> tree() {
        return tree;
    }

    public Lineage lineage() {
        return lineage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final var that = (SingleTreeIndividualImpl<?, ?>) obj;
        return Objects.equals(this.tree, that.tree) &&
                Objects.equals(this.lineage, that.lineage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tree, lineage);
    }
}


/**
 * An interface representing an individual as a single tree structure.
 *
 * @param <T>   The terminal type
 * @param <Out> The output type
 */
public interface SingleTreeIndividual<T, Out> extends Individual<T, Out> {
    /**
     * Creates a single tree individual from a tree node.
     *
     * @param <Out>      The output type
     * @param <T>        The terminal type
     * @param individual The tree node
     * @return A new single tree individual
     */
    static <Out, T> SingleTreeIndividual<T, Out> of(
            ImmutableNode<T, Out, ?, ?> individual,
            Lineage lineage
    ) {
        return new SingleTreeIndividualImpl<>(individual, lineage);
    }

    static <T, Out, P extends Individual<?,?>> SingleTreeIndividual<T, Out> of(
            ImmutableNode<T, Out, ?, ?> individual,
            List<P> parents,
            Operator<P, Sequence<SingleTreeIndividual<T, Out>>> operator
    ) {
        return new SingleTreeIndividualImpl<>(
                individual,
                parents,
                operator
        );
    }

    /**
     * Get the Node this individual wraps.
     *
     * @return The inner node.
     */
    ImmutableNode<T, Out, ?, ?> tree();

    @Override
    default Out evaluate(final T terminals) {
        return this.tree().evaluate(terminals);
    }

    /**
     * Creates an operator that works on single tree individuals by wrapping
     * a node operator.
     *
     * @param <T>          The terminal type
     * @param <Out>        The output type
     * @param nodeOperator The operator that works on nodes
     * @return An operator that works on single tree individuals
     */
    static <T, Out> Operator<
            SingleTreeIndividual<T, Out>,
            Sequence<SingleTreeIndividual<T, Out>>
    > operator(
            Operator<
                    Node<T, Out, ?, ?, ?>,
                    Sequence<ImmutableNode<T, Out, ?, ?>>
            > nodeOperator
    ) {
        return new Operator<>() {
            @Override
            public Sequence<SingleTreeIndividual<T, Out>> produce(
                    final List<SingleTreeIndividual<T, Out>> parents
            ) {
                final List<Node<T, Out, ?, ?, ?>> trees = new ArrayList<>(
                        parents.size()
                );

                for (final SingleTreeIndividual<T, Out> parent : parents) {
                    trees.add(parent.tree());
                }

                return nodeOperator.produce(trees)
                        .map(n -> SingleTreeIndividual.of(
                                n, parents, this
                        ));
            }

            @Override
            public int arity() {
                return nodeOperator.arity();
            }
        };
    }
}

