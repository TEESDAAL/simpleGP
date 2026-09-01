package gp.impl.individual.typed_tree;

import util.typed_function.TypedQuadFunction;

import java.util.List;

public sealed interface QuaternaryNode<
        Terminals,
        A, B, C, D, R,
        Self extends QuaternaryNode<Terminals, A, B, C, D, R, Self, Child>,
        Child extends Node<Terminals, ?, ?, ?, ?>
> extends NonTerminal<
        Terminals, R,
        Self, Child,
        ImmutableQuaternaryNode<Terminals, A, B, C, D, R>,
        MutableQuaternaryNode<Terminals, A, B, C, D, R>
> permits ImmutableQuaternaryNode, MutableQuaternaryNode {
    static <
            Terminals,
            A, B, C, D, R
    > ImmutableQuaternaryNode<Terminals, A, B, C, D, R> of(
            String name,
            TypedQuadFunction<A, B, C, D, R> typedFunction,
            ImmutableNode<Terminals, A, ?, ?> left,
            ImmutableNode<Terminals, B, ?, ?> middleLeft,
            ImmutableNode<Terminals, C, ?, ?> middleRight,
            ImmutableNode<Terminals, D, ?, ?> right
    ) {
        return new ImmutableQuaternaryNode<>(
                name,
                typedFunction,
                left,
                middleLeft,
                middleRight,
                right
        );
    }

    /**
     * @return The node representing the first tree.
     */
    Node<Terminals, A, ?, ?, ?> left();

    /**
     * @return The node representing the second tree.
     */
    Node<Terminals, B, ?, ?, ?> middleLeft();

    /**
     * @return The node representing the third tree.
     */
    Node<Terminals, C, ?, ?, ?> middleRight();

    /**
     * @return The node representing the third tree.
     */
    Node<Terminals, D, ?, ?, ?> right();

    /**
     * @return The bifunction which combines the left and right subtrees results.
     */
    TypedQuadFunction<A, B, C, D, R> combiner();

    default int arity() {
        return 4;
    }

    default R evaluate(Terminals terminals) {
        return this.combiner().apply(
                left().evaluate(terminals),
                middleLeft().evaluate(terminals),
                middleRight().evaluate(terminals),
                right().evaluate(terminals)
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    default R fromList(List<?> inputs) {
        assert inputs.size() == 4;
        return this.combiner().apply(
                (A) inputs.getFirst(),
                (B) inputs.get(1),
                (C) inputs.get(2),
                (D) inputs.get(3)
        );
    }

    default MutableQuaternaryNode<Terminals, A, B, C, D, R> mutableCopy() {
        return new MutableQuaternaryNode<>(
                this.name(),
                this.combiner(),
                this.left().mutableCopy(),
                this.middleLeft().mutableCopy(),
                this.middleRight().mutableCopy(),
                this.right().mutableCopy()
        );
    }

    default ImmutableQuaternaryNode<Terminals, A, B, C, D, R> immutableCopy() {
        return new ImmutableQuaternaryNode<>(
                this.name(),
                this.combiner(),
                this.left().immutableCopy(),
                this.middleLeft().immutableCopy(),
                this.middleRight().immutableCopy(),
                this.right().immutableCopy()
        );
    }
}
