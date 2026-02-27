package gp.tree;

import gp.utils.Operator;

import java.util.List;

/**
 * A mutable non-terminal node implementation.
 * @param <Terminals> The type of terminal inputs
 * @param <Input> The input type for the operator function
 * @param <Output> The output type
 */
public final class MutableNonTerminal<Terminals, Input, Output>
        implements NonTerminal<
        Terminals, Input, Output,
        MutableNode<Terminals, ?, Input, ?, ?>
        >, MutableNode<
        Terminals, Input, Output,
        MutableNonTerminal<Terminals, Input, Output>,
        ImmutableNonTerminal<Terminals, Input, Output>
        > {
    /** The operator function. */
    private Operator<Input, Output> function;
    /** The return type of this non-terminal. */
    private final Class<Output> returnType;
    /** The input type of this non-terminal. */
    private final Class<Input> inputType;

    /** The child nodes. */
    private List<MutableNode<
                Terminals, ?, Input,
                ? extends MutableNode<Terminals, ?, Input, ?, ?>,
                ? extends ImmutableNode<Terminals, ?, Input, ?, ?>
            >> children;


    /**
     * Constructs a mutable non-terminal with the given function and
     * children.
     * @param operatorFunction The operator function
     * @param childList The child nodes
     * @param inType The input type
     * @param outType The output type
     */
    public MutableNonTerminal(
            final Operator<Input, Output> operatorFunction,
            final List<MutableNode<
                Terminals, ?, Input,
                ? extends MutableNode<Terminals, ?, Input, ?, ?>,
                ? extends ImmutableNode<Terminals, ?, Input, ?, ?>
            >> childList,
            final Class<Input> inType,
            final Class<Output> outType) {
        this.function = operatorFunction;
        this.children = childList;
        this.inputType = inType;
        this.returnType = outType;
    }

    @Override
    public Class<Output> returnType() {
        return this.returnType;
    }


    @Override
    public Output evaluate(final Terminals terminals) {
        return this.function.produce(
                children.stream()
                        .map(child -> child.evaluate(terminals))
                        .toList()
        );
    }

    /**
     * Gets the operator function.
     * @return The function
     */
    public Operator<Input, Output> function() {
        return function;
    }

    /**
     * Sets the operator function.
     * @param newFunction The new function
     * @return This mutable non-terminal for method chaining
     */
    public MutableNonTerminal<Terminals, Input, Output> setFunction(
            final Operator<Input, Output> newFunction) {
        this.function = newFunction;
        return this;
    }

    /**
     * Gets the child nodes.
     * @return The list of children
     */
    public List<MutableNode<
            Terminals, ?, Input,
            ? extends MutableNode<Terminals, ?, Input, ?, ?>,
            ? extends ImmutableNode<Terminals, ?, Input, ?, ?>
            >> children() {
        return children;
    }

    @Override
    public Class<Input> inputType() {
        return inputType;
    }

    /**
     * Replaces a child at the given index.
     * @param index The index of the child to replace
     * @param child The new child node
     */
    public void replaceChild(
            final int index,
            final MutableNode<Terminals, ?, Input, ?, ?> child) {
        this.children.set(index, child);
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
        return children;
    }

    /**
     * Sets the children list.
     * @param newChildren The new children list
     */
    public MutableNonTerminal<Terminals, Input, Output> setChildren(
            final List<MutableNode<
                    Terminals, ?, Input,
                    ? extends MutableNode<Terminals, ?, Input, ?, ?>,
                    ? extends ImmutableNode<Terminals, ?, Input, ?, ?>
            >> newChildren) {
        this.children = newChildren;
        return this;
    }
}
