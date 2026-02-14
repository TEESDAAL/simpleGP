package gp.tree;

import gp.utils.UnaryOperator;

import java.util.stream.Stream;

public sealed interface Terminal<Terminals, Output> extends Node<
        Terminals, Terminals, Output, MutableTerminal<Terminals, Output>, ImmutableTerminal<Terminals, Output>
        > permits MutableTerminal, ImmutableTerminal {


    UnaryOperator<Terminals, Output> extractor();

    default MutableTerminal<Terminals, Output> mutableCopy() {
        return new MutableTerminal<Terminals, Output>(
                extractor(),
                returnType()
        );
    }


    default  ImmutableTerminal<Terminals, Output> immutableCopy() {
        return new ImmutableTerminal<>(
                extractor(),
                returnType()
        );
    }


    default Output evaluate(Terminals input) {
        return this.extractor().produce(input);
    }

    @Override
    default Stream<Node<Terminals, ?, ?, ?, ?>> stream() {
        return Stream.of(this);
    }

    @Override
    default int depth() {
        return 0;
    }
}
