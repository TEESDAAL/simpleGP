package gp.impl.individual.typed_tree;

import java.util.function.Function;

public record ImmutableTerminal<Terminals, R>(
    String name,
    Function<Terminals, R> extractor,
    Class<R> returnType
) implements Terminal<
    Terminals, R,
    ImmutableTerminal<Terminals, R>
>, ImmutableNode<
    Terminals,
    R,
    ImmutableTerminal<Terminals, R>,
    MutableTerminal<Terminals, R>
> {
    public static <T, R> ImmutableTerminal<T,R> of(
        String name,
        Function<T,R> terminal,
        Class<R> returnType
    ) {
        return new ImmutableTerminal<>(name, terminal, returnType);
    }

    @Override
    public ImmutableTerminal<Terminals, R> self() {
        return this;
    }
}
