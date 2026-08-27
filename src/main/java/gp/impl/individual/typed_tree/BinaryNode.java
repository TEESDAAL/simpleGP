package gp.impl.individual.typed_tree;

import utils.typed_functions.TypedBiFunction;

import java.util.List;

public sealed interface BinaryNode<
    A, B,
    Terminals, R,
    Self extends BinaryNode<A, B, Terminals, R, Self, Child>,
    Child extends Node<Terminals, ?, ?, ?, ?>
> extends NonTerminal<
    Terminals, R,
    Self, Child,
    ImmutableBinaryNode<A, B, Terminals, R>,
    MutableBinaryNode<A, B, Terminals, R>
> permits MutableBinaryNode, ImmutableBinaryNode {
    static <Terminals, R, B, A> ImmutableBinaryNode<A, B, Terminals, R> of(
        String name,
        TypedBiFunction<A,B,R> typedBiFunction,
        ImmutableNode<Terminals,A,?,?> left,
        ImmutableNode<Terminals,B,?,?> right
    ) {
        return new ImmutableBinaryNode<>(
            name,
            typedBiFunction,
            left,
            right
        );
    }

    /**
     * @return The node representing the left-hand tree. The first argument to combiner.
     */
    Node<Terminals, A, ?, ?, ?> left();
    /**
     * @return The node representing the right-hand tree.
     *  The second/last argument to combiner.
     */
    Node<Terminals, B, ?, ?, ?> right();

    /**
     * @return The bifunction which combines the left and right subtrees results.
     */
    TypedBiFunction<A, B, R> combiner();

    default R evaluate(Terminals terminals) {
        return this.combiner().apply(
            left().evaluate(terminals),
            right().evaluate(terminals)
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    default R fromList(List<?> inputs) {
        assert inputs.size() == 2;
        return this.combiner().apply(
            (A) inputs.getFirst(),
            (B) inputs.get(1)
        );
    }

    default MutableBinaryNode<A, B, Terminals, R> mutableCopy() {
        return new MutableBinaryNode<>(
            this.name(),
            this.left().mutableCopy(),
            this.right().mutableCopy(),
            this.combiner()
        );
    }

    default ImmutableBinaryNode<A, B, Terminals, R> immutableCopy() {
        return new ImmutableBinaryNode<>(
            this.name(),
            this.combiner(),
            this.left().immutableCopy(),
            this.right().immutableCopy()
        );
    }
}
