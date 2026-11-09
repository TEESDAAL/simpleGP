package gp.impl.individual.typed_tree;

import java.util.function.Function;
import java.util.stream.Stream;

public sealed interface Terminal<
        Terminals, R,
        Mutability,
        Self extends Terminal<Terminals, R, Mutability, Self>
> extends Node<
        Terminals, R,
        Mutability,
        Self,
        ImmutableTerminal<Terminals, R>,
        MutableTerminal<Terminals, R>
> permits ImmutableTerminal, MutableTerminal {
    Function<Terminals, R> extractor();

    @Override
    default R evaluate(Terminals terminals) {
        return this.extractor().apply(terminals);
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
    default Stream<Node<Terminals, ?, Mutability, ?, ?, ?>> stream() {
        return Stream.of(this);
    }
}
