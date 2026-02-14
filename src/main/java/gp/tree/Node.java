package gp.tree;

import gp.utils.Operator;
import gp.utils.UnaryOperator;

import java.util.List;
import java.util.stream.Stream;

public sealed interface Node<
        Terminals, Input, Output,
        Mutable extends MutableNode<Terminals, Input, Output, Mutable, Immutable>,
        Immutable extends ImmutableNode<Terminals, Input, Output, Immutable, Mutable>
        > permits Terminal, NonTerminal, MutableNode, ImmutableNode {
    Stream<Node<Terminals, ?, ?, ?, ?>> stream();
    Class<Output> returnType();
    Output evaluate(Terminals terminals);
    Mutable mutableCopy();
    Immutable immutableCopy();
    int depth();
    static <Term, Out> ImmutableTerminal<Term, Out> term(
            UnaryOperator<Term, Out> extractor,
            Class<Out> returnType
    ) {
        return ImmutableTerminal.of(extractor, returnType);
    }

    static <Term, In, Out> ImmutableNonTerminal<Term, In, Out> nonTerm(
            Operator<In, Out> function,
            List<ImmutableNode<Term, ?, In, ?, ?>> children,
            Class<In> inputType, Class<Out> outputType
    ) {
        return new ImmutableNonTerminal<>(function, children, inputType, outputType);
    }
}

