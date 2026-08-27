package gp.impl.individual.typed_tree;

import utils.typed_functions.TypedTriFunction;

import java.util.List;

public record ImmutableTrinaryNode<A, B, C, Terminals, R>(
    String name,
    TypedTriFunction<A,B,C,R> combiner,
    ImmutableNode<Terminals, A, ?, ?> left,
    ImmutableNode<Terminals, B, ?, ?> middle,
    ImmutableNode<Terminals, C, ?, ?> right
) implements TrinaryNode<
    A, B, C, Terminals, R,
    ImmutableTrinaryNode<A, B, C, Terminals, R>,
    ImmutableNode<Terminals, ?, ?, ?>
>, ImmutableNonTerminal<
    Terminals, R,
    ImmutableTrinaryNode<A, B, C, Terminals, R>,
    MutableTrinaryNode<A, B, C, Terminals, R>
> {

    @Override
    public ImmutableTrinaryNode<A, B, C, Terminals, R> self() {
        return this;
    }

    @Override
    public ImmutableTrinaryNode<A, B, C, Terminals, R> immutableCopy() {
        return this;
    }

    @Override
    public List<ImmutableNode<Terminals, ?, ?, ?>> children() {
        return List.of(this.left, this.middle, this.right);
    }
}
