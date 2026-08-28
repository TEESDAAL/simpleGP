package gp.impl.individual.typed_tree;

import utils.typed_functions.TypedTriFunction;

import java.util.List;

public sealed interface TrinaryNode<
    Terminals,
    A, B, C, R,
    Self extends TrinaryNode<Terminals, A, B, C, R, Self, Child>,
    Child extends Node<Terminals, ?, ?, ?, ?>
> extends NonTerminal<
    Terminals, R,
    Self, Child,
    ImmutableTrinaryNode<Terminals, A, B, C, R>,
    MutableTrinaryNode<Terminals, A, B, C, R>
> permits ImmutableTrinaryNode, MutableTrinaryNode {
    static <Terminals,A,B,C,R> ImmutableTrinaryNode<Terminals,A,B,C,R> of(
        String name,
        TypedTriFunction<A,B,C,R> typedTriFunction,
        ImmutableNode<Terminals,A,?,?> left,
        ImmutableNode<Terminals,B,?,?> middle,
        ImmutableNode<Terminals,C,?,?> right
    ) {
        return new ImmutableTrinaryNode<>(name, typedTriFunction, left, middle, right);
    }

    /**
     * @return The node representing the left-hand tree. The first argument to combiner.
     */
    Node<Terminals, A, ?, ?, ?> left();
    /**
     * @return The node representing the middle child. The second argument to combiner.
     */
    Node<Terminals, B, ?, ?, ?> middle();
    /**
     * @return The node representing the right-hand tree. The last argument to combiner.
     */
    Node<Terminals, C, ?, ?, ?> right();

    /**
     * @return The bifunction which combines the left and right subtrees results.
     */
    TypedTriFunction<A, B, C, R> combiner();

    default int arity() {
        return 3;
    }

    default R evaluate(Terminals terminals) {
        return this.combiner().apply(
            left().evaluate(terminals),
            middle().evaluate(terminals),
            right().evaluate(terminals)
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    default R fromList(List<?> inputs) {
        assert inputs.size() == 3;
        return this.combiner().apply(
            (A) inputs.getFirst(),
            (B) inputs.get(1),
            (C) inputs.get(2)
        );
    }

    default MutableTrinaryNode<Terminals, A, B, C, R> mutableCopy() {
        return new MutableTrinaryNode<>(
            this.name(),
            this.combiner(),
            this.left().mutableCopy(),
            this.middle().mutableCopy(),
            this.right().mutableCopy()
        );
    }

    default ImmutableTrinaryNode<Terminals, A, B, C, R> immutableCopy() {
        return new ImmutableTrinaryNode<>(
            this.name(),
            this.combiner(),
            this.left().immutableCopy(),
            this.middle().immutableCopy(),
            this.right().immutableCopy()
        );
    }
}
