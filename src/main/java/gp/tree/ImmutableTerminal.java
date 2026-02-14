package gp.tree;

import gp.utils.UnaryOperator;

import java.util.HashMap;
import java.util.Map;

public record ImmutableTerminal<Terminals, Output>(
        UnaryOperator<Terminals, Output> extractor,
        Class<Output> returnType
) implements
        Terminal<Terminals, Output>,
        ImmutableNode<
                Terminals, Terminals, Output,
                ImmutableTerminal<Terminals, Output>,
                MutableTerminal<Terminals, Output>
        > {
    private static final Map<ImmutableTerminal<?,?>, ImmutableTerminal<?,?>> cache = new HashMap<>();

    public static <Terminals, Output> ImmutableTerminal<Terminals, Output> of(
            UnaryOperator<Terminals, Output> extractor, Class<Output> returnType
    ) {
        ImmutableTerminal<Terminals, Output> term = new ImmutableTerminal<Terminals, Output>(extractor, returnType);
        //noinspection unchecked
        return (ImmutableTerminal<Terminals, Output>) cache.computeIfAbsent(term, k -> term);
    }
}
