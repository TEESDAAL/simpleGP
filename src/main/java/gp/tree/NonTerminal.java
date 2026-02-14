package gp.tree;

import gp.utils.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public sealed interface NonTerminal<
        Terminals, Input, Output,
        Child extends Node<Terminals, ?, Input, ?, ?>
> extends Node<
        Terminals, Input, Output,
        MutableNonTerminal<Terminals, Input, Output>,
        ImmutableNonTerminal<Terminals, Input, Output>
        > permits MutableNonTerminal, ImmutableNonTerminal
{
    List<Child> children();
    Class<Input> inputType();

    Operator<Input, Output> function();

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
                this.children().stream().map(child -> child.mutableCopy())
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

