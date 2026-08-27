package gp.impl.individual.typed_tree;

import utils.typed_functions.TypedFunction;

import java.util.List;

public record ImmutableUnaryNode<A, Terminals, R>(
    String name,
    TypedFunction<A, R> combiner,
    ImmutableNode<Terminals, A, ?, ?> input
) implements UnaryNode<
    A, Terminals, R,
    ImmutableUnaryNode<A, Terminals, R>,
    ImmutableNode<Terminals, ?, ?, ?>
>, ImmutableNonTerminal<
    Terminals, R,
    ImmutableUnaryNode<A, Terminals, R>,
    MutableUnaryNode<A, Terminals, R>
> {
    @Override
    public List<ImmutableNode<Terminals, ?, ?, ?>> children() {
        return List.of(input);
    }

    @Override
    public ImmutableUnaryNode<A, Terminals, R> self() {
        return this;
    }
}
