package gp.impl.individual.typed_tree;

import utils.typed_functions.TypedTriFunction;

import java.util.List;

public sealed interface TrinaryNode<
    A, B, C,
    Terminals, R,
    Self extends TrinaryNode<A, B, C, Terminals, R, Self, Child>,
    Child extends Node<Terminals, ?, ?, ?, ?>
> extends NonTerminal<
    Terminals, R,
    Self, Child,
    ImmutableTrinaryNode<A, B, C, Terminals, R>,
    MutableTrinaryNode<A, B, C, Terminals, R>
> permits ImmutableTrinaryNode, MutableTrinaryNode {
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

    default MutableTrinaryNode<A, B, C, Terminals, R> mutableCopy() {
        return new MutableTrinaryNode<>(
            this.name(),
            this.combiner(),
            this.left().mutableCopy(),
            this.middle().mutableCopy(),
            this.right().mutableCopy()
        );
    }

    default ImmutableTrinaryNode<A, B, C, Terminals, R> immutableCopy() {
        return new ImmutableTrinaryNode<>(
            this.name(),
            this.combiner(),
            this.left().immutableCopy(),
            this.middle().immutableCopy(),
            this.right().immutableCopy()
        );
    }
}
