package gp.tree;

import gp.utils.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a non-terminal node in a genetic programming tree.
 * A non-terminal is an internal node that applies a function to its children.
 * @param <Terminals> The type of terminal inputs
 * @param <Input> The input type for the operator function
 * @param <Output> The output type
 * @param <Child> The type of child nodes
 */
public sealed interface NonTerminal<
        Terminals, Input, Output,
        Child extends Node<Terminals, ?, Input, ?, ?>
> extends Node<
        Terminals, Input, Output,
        MutableNonTerminal<Terminals, Input, Output>,
        ImmutableNonTerminal<Terminals, Input, Output>
        > permits MutableNonTerminal, ImmutableNonTerminal {
    /**
     * Gets the child nodes of this non-terminal.
     * @return The list of children
     */
    List<Child> children();

    /**
     * Gets the input type for the operator function.
     * @return The input type class
     */
    Class<Input> inputType();

    /**
     * Gets the operator function applied by this non-terminal.
     * @return The operator
     */
    Operator<Input, Output> function();

    /**
     * Applies the operator to the given inputs.
     * @param inputs The input list for the operator
     * @return The output value
     */
    default Output manualOutput(List<Input> inputs) {
        return function().produce(inputs);
    }

    @Override
    default Output evaluate(Terminals terminals) {
        return this.manualOutput(this.children().stream()
                .map(child -> child.evaluate(terminals))
                .toList());
    }

    @Override
    default MutableNonTerminal<Terminals, Input, Output> mutableCopy() {
        return new MutableNonTerminal<>(
                this.function(),
                this.children().stream()
                        .map(child -> child.mutableCopy())
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.inputType(),
                this.returnType()
        );
    }

    @Override
    default ImmutableNonTerminal<Terminals, Input, Output> immutableCopy() {
        return new ImmutableNonTerminal<>(
                this.function(),
                this.children().stream()
                        .map(child -> child.immutableCopy())
                        .collect(Collectors.toCollection(ArrayList::new)),
                this.inputType(),
                this.returnType()
        );
    }

    @Override
    default Stream<Node<Terminals, ?, ?, ?, ?>> stream() {
        return Stream.concat(
                Stream.of(this),
                this.children().stream()
                        .flatMap(Node::stream)
        );
    }

    @Override
    default int depth() {
        return 1 + children().stream().mapToInt(Node::depth).max()
                .orElseThrow();
    }
}
