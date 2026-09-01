package gp.impl.individual.typed_tree;

import util.typed_function.TypedTriFunction;

import java.util.List;

public record ImmutableTrinaryNode<Terminals, A, B, C, R>(
        String name,
        TypedTriFunction<A, B, C, R> combiner,
        ImmutableNode<Terminals, A, ?, ?> left,
        ImmutableNode<Terminals, B, ?, ?> middle,
        ImmutableNode<Terminals, C, ?, ?> right
) implements TrinaryNode<
        Terminals, A, B, C, R,
        ImmutableTrinaryNode<Terminals, A, B, C, R>,
        ImmutableNode<Terminals, ?, ?, ?>
>, ImmutableNonTerminal<
        Terminals, R,
        ImmutableTrinaryNode<Terminals, A, B, C, R>,
        MutableTrinaryNode<Terminals, A, B, C, R>
> {

    @Override
    public ImmutableTrinaryNode<Terminals, A, B, C, R> self() {
        return this;
    }

    @Override
    public ImmutableTrinaryNode<Terminals, A, B, C, R> immutableCopy() {
        return this;
    }

    @Override
    public List<ImmutableNode<Terminals, ?, ?, ?>> children() {
        return List.of(this.left, this.middle, this.right);
    }
}
