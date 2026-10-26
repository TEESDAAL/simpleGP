package gp.impl.individual;

import gp.core.individual.Individual;
import gp.core.individual.LineagedIndividual;
import gp.impl.individual.lineage.Lineage;
import gp.impl.individual.lineage.Tracked;
import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.Node;
import util.operator.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A record representing an individual as a single tree structure.
 *
 */
final class SingleTreeIndividualImpl<T, Out> implements SingleTreeIndividual<T, Out> {
    private final ImmutableNode<T, Out, ?, ?> tree;
    private final Tracked<SingleTreeIndividual<T, Out>> lineage;

    /**
     * @param tree  The immutable tree representing this individual
     */
    SingleTreeIndividualImpl(
            ImmutableNode<T, Out, ?, ?> tree,
            Tracked<SingleTreeIndividual<T,Out>> lineage
    ) {
        this.tree = tree;
        this.lineage = lineage;
    }

    <P extends Individual<?, ?>> SingleTreeIndividualImpl(
            ImmutableNode<T, Out, ?, ?> tree,
            List<P> parents,
            Operator<P, List<SingleTreeIndividual<T, Out>>> operator
    ) {
        this.tree = tree;
        this.lineage = Lineage.descendant(
                this,
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

    public Tracked<SingleTreeIndividual<T, Out>> lineage() {
        return lineage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SingleTreeIndividualImpl<?, ?>) obj;
        return Objects.equals(this.tree, that.tree) &&
                Objects.equals(this.lineage, that.lineage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tree, lineage);
    }

    @Override
    public Lineage linage() {
        return this.lineage;
    }
}


/**
 * An interface representing an individual as a single tree structure.
 *
 * @param <T>   The terminal type
 * @param <Out> The output type
 */
public interface SingleTreeIndividual<T, Out> extends LineagedIndividual<
        T, Out, SingleTreeIndividual<T, Out>, Lineage<SingleTreeIndividual<?, ?>>
> {
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
            Tracked<SingleTreeIndividual<T, Out>> lineage
    ) {
        return new SingleTreeIndividualImpl<>(individual, lineage);
    }

    static <T, Out, P extends Individual<?,?>> SingleTreeIndividual<T, Out> of(
            ImmutableNode<T, Out, ?, ?> individual,
            List<P> parents,
            Operator<P, List<SingleTreeIndividual<T, Out>>> operator
    ) {
        return new SingleTreeIndividualImpl<>(individual, parents, operator);
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
            List<SingleTreeIndividual<T, Out>>
    > operator(
            Operator<
                    Node<T, Out, ?, ?, ?>,
                    List<ImmutableNode<T, Out, ?, ?>>
            > nodeOperator
    ) {
        return new Operator<>() {
            @Override
            public List<SingleTreeIndividual<T, Out>> produce(
                    final List<SingleTreeIndividual<T, Out>> parents
            ) {
                final List<Node<T, Out, ?, ?, ?>> trees = new ArrayList<>(
                        parents.size()
                );

                for (final SingleTreeIndividual<T, Out> parent : parents) {
                    trees.add(parent.tree());
                }

                return nodeOperator.produce(trees)
                        .stream()
                        .map(n -> SingleTreeIndividual.<
                                T, Out,
                                SingleTreeIndividual<T, Out>
                        >of(n, parents, this))
                        .toList();
            }

            @Override
            public Integer arity() {
                return nodeOperator.arity();
            }
        };
    }
}

