package gp.impl.individual.typed_tree;

import util.typed_function.TypedFunction;

import java.util.List;

public sealed interface UnaryNode<
        Terminals, A, R,
        Self extends UnaryNode<Terminals, A, R, Self, Child>,
        Child extends Node<Terminals, ?, ?, ?, ?>
> extends NonTerminal<
        Terminals, R,
        Self, Child,
        ImmutableUnaryNode<Terminals, A, R>,
        MutableUnaryNode<Terminals, A, R>
> permits MutableUnaryNode, ImmutableUnaryNode {
    static <Terminals, A, R> ImmutableUnaryNode<Terminals, A, R> of(
            String name,
            TypedFunction<A, R> typedFunction,
            ImmutableNode<Terminals, A, ?, ?> input
    ) {
        return new ImmutableUnaryNode<>(
                name,
                typedFunction,
                input
        );
    }

    /**
     * @return The node providing input to this node.
     */
    Node<Terminals, A, ?, ?, ?> input();


    /**
     * @return The function which maps the input subtree.
     */
    TypedFunction<A, R> combiner();

    default R evaluate(Terminals terminals) {
        return this.combiner().apply(
                input().evaluate(terminals)
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    default R fromList(List<?> inputs) {
        assert inputs.size() == 1;
        return this.combiner().apply((A) inputs.getFirst());
    }

    default MutableUnaryNode<Terminals, A, R> mutableCopy() {
        return new MutableUnaryNode<>(
                this.name(),
                this.input().mutableCopy(),
                this.combiner()
        );
    }
}
