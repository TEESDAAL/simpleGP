package gp.impl.individual.typed_tree;

import utils.Updater;
import utils.typed_functions.TypedFunction;

import java.util.List;

public final class MutableUnaryNode<A, Terminals, R> implements
    UnaryNode<
        A, Terminals, R,
        MutableUnaryNode<A, Terminals, R>,
        MutableNode<Terminals, ?, ?, ?>
>, MutableNonTerminal<
    Terminals, R,
    MutableUnaryNode<A, Terminals, R>,
    ImmutableUnaryNode<A, Terminals, R>
> {
    String name;
    MutableNode<Terminals, A, ?, ?> inputNode;
    TypedFunction<A, R> function;

    public MutableUnaryNode(
        String name,
        MutableNode<Terminals, A, ?, ?> inputNode,
        TypedFunction<A, R> function
    ) {
        this.name = name;
        this.inputNode = inputNode;
        this.function = function;
    }

    @Override
    public MutableNode<Terminals, A, ?, ?> input() {
        return inputNode;
    }

    public MutableUnaryNode<A, Terminals, R> setInput(
        Updater<MutableNode<Terminals, A, ?, ?>> updater
    ) {
        this.inputNode = updater.newValue(this.inputNode);
        return this;
    }

    @Override
    public TypedFunction<A, R> combiner() {
        return this.function;
    }

    @Override
    public List<MutableNode<Terminals, ?, ?, ?>> children() {
        return List.of(inputNode);
    }

    @Override
    public MutableUnaryNode<A, Terminals, R> self() {
        return this;
    }

    @Override
    public String name() {
        return this.name;
    }

    public MutableUnaryNode<A, Terminals, R> setName(Updater<String> updater) {
        this.name = updater.newValue(name);
        return this;
    }

    @Override
    public MutableUnaryNode<A, Terminals, R> replaceChild(
        int index, Updater<MutableNode<Terminals, ?, ?, ?>> node
    ) {
        if (index != 0) {
            throw new IllegalArgumentException(
                "Unary node only supports setting children of index 0"
            );
        }
        return this.setInput(
            input -> MutableNode.<Terminals, A>returnTypeCompatible(
                node.newValue(input), function.inputType()
            )
        );
    }
}
