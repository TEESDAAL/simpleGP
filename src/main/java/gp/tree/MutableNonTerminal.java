package gp.tree;

import gp.utils.Operator;

import java.util.List;

public final class MutableNonTerminal<Terminals, Input, Output> implements NonTerminal<
        Terminals, Input, Output,
        MutableNode<Terminals, ?, Input, ?, ?>
        >, MutableNode<
        Terminals, Input, Output,
        MutableNonTerminal<Terminals, Input, Output>,
        ImmutableNonTerminal<Terminals, Input, Output>
        > {
    private Operator<Input, Output> function;
    private Class<Output> returnType;
    private Class<Input> inputType;

    List<MutableNode<
                Terminals, ?, Input,
                ? extends MutableNode<Terminals, ?, Input, ?, ?>,
                ? extends ImmutableNode<Terminals, ?, Input, ?, ?>
            >> children;

    public MutableNonTerminal(
            Operator<Input, Output> function,
            List<MutableNode<
                Terminals, ?, Input,
                ? extends MutableNode<Terminals, ?, Input, ?, ?>,
                ? extends ImmutableNode<Terminals, ?, Input, ?, ?>
            >> children,
            Class<Input> inputType,
            Class<Output> returnType) {
        this.function = function;
        this.children = children;
        this.inputType = inputType;
        this.returnType = returnType;
    }

    @Override
    public Class<Output> returnType() {
        return this.returnType;
    }


    @Override
    public Output evaluate(Terminals terminals) {
        return this.function.produce(
                children.stream().map(child -> child.evaluate(terminals))
                        .toList()
        );
    }

    public Operator<Input, Output> function() {
        return function;
    }

    public MutableNonTerminal<Terminals, Input, Output> setFunction(Operator<Input, Output> function) {
        this.function = function;
        return this;
    }

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

    public void replaceChild(int index,  MutableNode<Terminals, ?, Input, ?, ?> child) {
        this.children.set(index, child);
    }
}
