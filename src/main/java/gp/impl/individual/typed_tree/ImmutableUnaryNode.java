package gp.impl.individual.typed_tree;

import util.typed_function.TypedFunction;

import java.util.List;

public record ImmutableUnaryNode<Terminals, A, R>(
        String name,
        TypedFunction<A, R> combiner,
        ImmutableNode<Terminals, A, ?, ?> input
) implements UnaryNode<
        Terminals, A, R,
        ImmutableUnaryNode<Terminals,A, R>,
        ImmutableNode<Terminals, ?, ?, ?>
>, ImmutableNonTerminal<
        Terminals, R,
        ImmutableUnaryNode<Terminals, A, R>,
        MutableUnaryNode<Terminals, A, R>
> {
    @Override
    public List<ImmutableNode<Terminals, ?, ?, ?>> children() {
        return List.of(input);
    }


    @Override
    public ImmutableUnaryNode<Terminals, A, R> self() {
        return this;
    }
}
