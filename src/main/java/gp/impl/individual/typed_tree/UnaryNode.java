package gp.impl.individual.typed_tree;

import utils.typed_functions.TypedFunction;

import java.util.List;

public sealed interface UnaryNode<
    A, Terminals, R,
    Self extends UnaryNode<A, Terminals, R, Self, Child>,
    Child extends Node<Terminals, ?, ?, ?, ?>
> extends NonTerminal<
    Terminals, R,
    Self, Child,
    ImmutableUnaryNode<A, Terminals, R>,
    MutableUnaryNode<A, Terminals, R>
> permits MutableUnaryNode, ImmutableUnaryNode {
    static <Terminals, R, A> ImmutableUnaryNode<A, Terminals, R> of(
        String name,
        TypedFunction<A,R> typedFunction,
        ImmutableNode<Terminals,A,?,?> input
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

    default MutableUnaryNode<A, Terminals, R> mutableCopy() {
        return new MutableUnaryNode<>(
            this.name(),
            this.input().mutableCopy(),
            this.combiner()
        );
    }

    default ImmutableUnaryNode<A, Terminals, R> immutableCopy() {
        return new ImmutableUnaryNode<>(
            this.name(),
            this.combiner(),
            this.input().immutableCopy()
        );
    }
}
