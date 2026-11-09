package gp.impl.individual.testTree;

import util.typed_function.TypedQuadFunction;

import java.util.List;

public record ImmutableQuaternaryNode<T,A,B,C,D,R>(
        String name,
        TypedQuadFunction<A, B, C, D, R> combiner,
        ImmutableNode<T, A, ?> left,
        ImmutableNode<T, B, ?> middleLeft,
        ImmutableNode<T, C, ?> middleRight,
        ImmutableNode<T, D, ?> right
) implements QuaternaryNode<T,A,B,C,D,R,Immutable>, ImmutableNonTerminal<T, R> {
    @Override
    public List<ImmutableNode<T, ?, ?>> children() {
        return List.of(left, middleLeft, middleRight, right);
    }
}
