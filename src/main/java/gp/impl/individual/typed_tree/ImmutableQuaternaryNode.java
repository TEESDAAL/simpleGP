package gp.impl.individual.typed_tree;

import util.typed_function.TypedQuadFunction;

import java.util.List;

public record ImmutableQuaternaryNode<Terminals, A, B, C, D, R>(
        String name,
        TypedQuadFunction<A, B, C, D, R> combiner,
        ImmutableNode<Terminals, A, ?, ?> left,
        ImmutableNode<Terminals, B, ?, ?> middleLeft,
        ImmutableNode<Terminals, C, ?, ?> middleRight,
        ImmutableNode<Terminals, D, ?, ?> right
) implements QuaternaryNode<
        Terminals, A, B, C, D, R,
        ImmutableQuaternaryNode<Terminals, A, B, C, D, R>,
        ImmutableNode<Terminals, ?, ?, ?>
>, ImmutableNonTerminal<
        Terminals, R,
        ImmutableQuaternaryNode<Terminals, A, B, C, D, R>,
        MutableQuaternaryNode<Terminals, A, B, C, D, R>
> {

    @Override
    public ImmutableQuaternaryNode<Terminals, A, B, C, D, R> self() {
        return this;
    }

    @Override
    public ImmutableQuaternaryNode<Terminals, A, B, C, D, R> immutableCopy() {
        return this;
    }

    @Override
    public List<ImmutableNode<Terminals, ?, ?, ?>> children() {
        return List.of(this.left, this.middleLeft, this, middleRight, this.right);
    }
}
