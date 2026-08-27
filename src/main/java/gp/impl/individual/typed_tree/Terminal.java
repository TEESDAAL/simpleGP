package gp.impl.individual.typed_tree;

import java.util.function.Function;
import java.util.stream.Stream;

public non-sealed interface Terminal<
    Terminals, R,
    Self extends Terminal<Terminals, R, Self>
> extends Node<
    Terminals, R,
    Self,
    ImmutableTerminal<Terminals, R>,
    MutableTerminal<Terminals, R>
> {
    Function<Terminals, R> extractor();

    @Override
    default R evaluate(Terminals terminals) {
        return this.extractor().apply(terminals);
    }

    @Override
    default ImmutableTerminal<Terminals, R> immutableCopy() {
        return new ImmutableTerminal<>(
            this.name(),
            this.extractor(),
            this.returnType()
        );
    }

    @Override
    default MutableTerminal<Terminals, R> mutableCopy() {
        return new MutableTerminal<>(
            this.name(),
            this.extractor(),
            this.returnType()
        );
    }

    @Override
    default Stream<Node<Terminals, ?, ?, ?, ?>> stream() {
        return Stream.of(this);
    }
}
