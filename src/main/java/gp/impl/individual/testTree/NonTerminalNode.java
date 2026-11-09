package gp.impl.individual.testTree;

import gp.impl.individual.testTree.visitor.TreeVisitor;

import java.util.List;

public sealed interface NonTerminalNode<T, R, M extends Mutability> extends Node<
        T, R, M, NonTerminal
> permits MutableNonTerminal, ImmutableNonTerminal,
        UnaryNode, BinaryNode, TrinaryNode, QuaternaryNode {
    /**
     * @return All the children of this non-terminal.
     */
    List<? extends Node<T, ?, M, ?>> children();

    /**
     * @return The input types for this node.
     */
    List<Class<?>> inputTypes();

    /**
     * Evaluate this individual for a set of given inputs
     * @param list The list of inputs, must be type compatible with {@link this.inputTypes()}.
     * @return The result of evaluating this node on the given inputs.
     */
    R fromList(List<?> list);

    @Override
    default <U> U visit(TreeVisitor<T, R, U, M> visitor) {
        return visitor.nonTerminal(this);
    }
}
