package gp.impl.individual.typed_tree;

import util.typed_function.TypedBiFunction;

import java.util.List;

public record ImmutableBinaryNode<A, B, Terminals, R>(
        String name,
        TypedBiFunction<A, B, R> combiner,
        ImmutableNode<Terminals, A, ?, ?> left,
        ImmutableNode<Terminals, B, ?, ?> right
) implements BinaryNode<
        A, B, Terminals, R,
        ImmutableBinaryNode<A, B, Terminals, R>,
        ImmutableNode<Terminals, ?, ?, ?>
        >, ImmutableNonTerminal<
        Terminals, R,
        ImmutableBinaryNode<A, B, Terminals, R>,
        MutableBinaryNode<A, B, Terminals, R>
> {
    @Override
    public ImmutableBinaryNode<A, B, Terminals, R> self() {
        return this;
    }

    @Override
    public ImmutableBinaryNode<A, B, Terminals, R> immutableCopy() {
        return this;
    }

    @Override
    public List<ImmutableNode<Terminals, ?, ?, ?>> children() {
        return List.of(this.left, this.right);
    }
}
