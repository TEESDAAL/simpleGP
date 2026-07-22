package gp.impl.individual.tree;

import utils.ArrayUtils;
import utils.operators.Operator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO, make evaluate hold an array that it reuses for its outputs

/**
 * Represents a non-terminal node in a genetic programming tree.
 * A non-terminal is an internal node that applies a function to its children.
 * @param <Terminals> The type of terminal inputs
 * @param <Input> The input type for the operator function
 * @param <Output> The output type
 * @param <Child> The type of child nodes
 */
public sealed abstract class NonTerminal<
        Terminals, Input, Output,
        Child extends Node<Terminals, ?, Input, ?, ?>
> implements Node<
        Terminals, Input, Output,
        MutableNonTerminal<Terminals, Input, Output>,
        ImmutableNonTerminal<Terminals, Input, Output>
> permits MutableNonTerminal, ImmutableNonTerminal {
    /** The name of this non-terminal. */
    protected String name;

    /** The operator function. */
    protected Operator<Input, Output> function;

    /** The child nodes. */
    protected Child[] children;

    /** The input type of this non-terminal. */
    protected Class<Input> inputType;

    /** The return type of this non-terminal. */
    protected Class<Output> returnType;

    /**
     * @param name        The name of this non-terminal node
     * @param function    The operator function
     * @param children    The immutable child nodes
     * @param inputType   The input type class
     * @param returnType  The output type class
     */
    public NonTerminal(
        String name,
        Operator<Input, Output> function,
        Child[] children,
        Class<Input> inputType,
        Class<Output> returnType
    ) {
        this.name = Objects.requireNonNull(name);
        this.function = Objects.requireNonNull(function);
        this.children = Objects.requireNonNull(children);
        this.inputType = Objects.requireNonNull(inputType);
        this.returnType = Objects.requireNonNull(returnType);
    }

    /**
     * Find the maximum number of arguments of any node in the tree.
     * @return The maximum number of arguments needed for any child in this tree.
     */
    protected int maximumArity() {
        int maximumArity = this.function.arity();
        for (Child child : children) {
            final int childArity = switch (child) {
                case NonTerminal<?, ?, ?, ?> nonTerminal -> nonTerminal
                    .maximumArity();
                case Terminal<?, ?> _ -> 0;
            };
            maximumArity = Math.max(maximumArity, childArity);
        }
        return maximumArity;
    }

    @Override
    public String name() {
        return name;
    }

    /**
     * Get the return type of this node.
     * @return the return type of this node.
     */
    public Class<Output> returnType() {
        return this.returnType;
    }

    /**
     * Gets the child nodes of this non-terminal.
     * @return The list of children
     */
    public Child[] children() {
        return this.children;
    }

    /**
     * Gets the input type for the operator function.
     * @return The input type class
     */
    public Class<Input> inputType() {
        return this.inputType;
    }

    /**
     * Gets the operator function applied by this non-terminal.
     * @return The operator
     */
    public Operator<Input, Output> function() {
        return this.function;
    }

    /**
     * Applies the operator to the given inputs.
     * @param inputs The input list for the operator
     * @return The output value
     */
    public Output manualOutput(List<Input> inputs) {
        //noinspection unchecked
        return this.manualOutput(
            (Input[]) inputs.toArray()
        );
    }

    /**
     * Applies the operator to the given inputs.
     * @param inputs The input list for the operator
     * @return The output value
     */
    public Output manualOutput(Input[] inputs) {
        return function().produce(inputs);
    }

    @Override
    public Output evaluate(Terminals terminals) {
        @SuppressWarnings("unchecked")
        final Input[] inputs = (Input[]) new Object[children.length];
        for (int i=0; i < children.length; i++) {
            inputs[i] = children[i].evaluate(terminals);
        }

        return this.manualOutput(inputs);
    }

    /**
     * To prevent extra object creation an array is passed down.
     *  This array has to be at least as long as the maximum arity
     *    of all nodes in this subtree.
     * @param terminals The terminal inputs for evaluation
     * @param inputs The array to hold the children
     * @return The output of this node when given the terminals.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Output performantEvaluate(Terminals terminals, Object[] inputs) {
       throw new UnsupportedOperationException();
    }

    @Override
    public MutableNonTerminal<Terminals, Input, Output> mutableCopy() {
        return new MutableNonTerminal<>(
                this.name,
                this.function,
                ArrayUtils.mapInPlaceInvalidatingOldReferences(
                    Arrays.copyOf(this.children, this.children.length),
                    child -> child.mutableCopy()
                ),
                this.inputType,
                this.returnType
        );
    }

    @Override
    public ImmutableNonTerminal<Terminals, Input, Output> immutableCopy() {
        return new ImmutableNonTerminal<>(
                this.name,
                this.function,
                ArrayUtils.mapInPlaceInvalidatingOldReferences(
                    Arrays.copyOf(this.children, this.children.length),
                    child -> child.immutableCopy()
                ),
                this.inputType,
                this.returnType
        );
    }

    @Override
    public Stream<Node<Terminals, ?, ?, ?, ?>> stream() {
        return Stream.concat(
                Stream.of(this),
                this.children.stream()
                        .flatMap(Node::stream)
        );
    }

    @Override
    public int depth() {
        return 1 + children().stream().mapToInt(Node::depth).max()
                .orElseThrow();
    }
}
