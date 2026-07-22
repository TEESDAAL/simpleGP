package gp.impl.individual.tree;

import utils.operators.Operator;

import java.util.Arrays;
import java.util.List;

/**
 * A mutable non-terminal node implementation.
 * @param <Terminals> The type of terminal inputs
 * @param <Input> The input type for the operator function
 * @param <Output> The output type
 */
public final class MutableNonTerminal<Terminals, Input, Output>
    extends NonTerminal<
        Terminals, Input, Output,
        MutableNode<Terminals, ?, Input, ?, ?>
    > implements MutableNode<
        Terminals, Input, Output,
        MutableNonTerminal<Terminals, Input, Output>,
        ImmutableNonTerminal<Terminals, Input, Output>
    > {

    /**
     * Constructs a mutable non-terminal with the given function and
     * children.
     * @param name The name of this non-terminal
     * @param operatorFunction The operator function
     * @param childList The child nodes
     * @param inType The input type
     * @param outType The output type
     */
    public MutableNonTerminal(
        String name, Operator<Input, Output> operatorFunction,
        MutableNode<
                Terminals, ?, Input,
                ? extends MutableNode<Terminals, ?, Input, ?, ?>,
                ? extends ImmutableNode<Terminals, ?, Input, ?, ?>
        >[] childList,
        Class<Input> inType, Class<Output> outType
    ) {
        super(name, operatorFunction, childList, inType, outType);
    }

    /**
     * Sets the operator function.
     * @param newFunction The new function
     * @return This mutable non-terminal for method chaining
     */
    public MutableNonTerminal<Terminals, Input, Output> setFunction(
        Operator<Input, Output> newFunction
    ) {
        this.function = newFunction;
        return this;
    }

    /**
     * Replaces a child at the given index.
     * @param index The index of the child to replace
     * @param child The new child node
     */
    public void replaceChild(
        int index,
        MutableNode<Terminals, ?, Input, ?, ?> child
    ) {
        this.children[index] = child;
    }

    /**
     * Gets the children list.
     * @return The children list
     */
    public List<MutableNode<
        Terminals, ?, Input,
        ? extends MutableNode<Terminals, ?, Input, ?, ?>,
        ? extends ImmutableNode<Terminals, ?, Input, ?, ?>
    >> getChildren() {
        return Arrays.asList(children);
    }

    /**
     * Sets the children list.
     * @param newChildren The new children list
     * @return This mutable non-terminal for method chaining
     */
    public MutableNonTerminal<Terminals, Input, Output> setChildren(
        MutableNode<
            Terminals, ?, Input,
            ? extends MutableNode<Terminals, ?, Input, ?, ?>,
            ? extends ImmutableNode<Terminals, ?, Input, ?, ?>
        >[] newChildren
    ) {
        this.children = newChildren;
        return this;
    }

    /**
     * Sets the name of this non-terminal.
     * @param name The new name
     * @return This mutable non-terminal with the updated name for method chaining
     */
    public MutableNonTerminal<Terminals, Input, Output> setName(final String name) {
        this.name = name;
        return this;
    }
}
