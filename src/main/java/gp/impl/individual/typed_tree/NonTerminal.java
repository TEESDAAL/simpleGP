package gp.impl.individual.typed_tree;


import java.util.List;
import java.util.stream.Stream;

public sealed interface NonTerminal<
    Terminals, R,
    Self extends NonTerminal<Terminals, R, Self, Child, Immutable, Mutable>,
    Child extends Node<Terminals, ?, ?, ?, ?>,
    Immutable extends ImmutableNonTerminal<Terminals, R, Immutable, Mutable>,
    Mutable extends MutableNonTerminal<Terminals, R, Mutable, Immutable>
    > extends Node<
    Terminals, R,
    Self,
    Immutable,
    Mutable
> permits BinaryNode, ImmutableNonTerminal,
    MutableNonTerminal, QuaternaryNode,
    TrinaryNode, UnaryNode {
    /**
     * @return The list of input types to this node.
     */
    NodeFunction<R, ?> combiner();

    @Override
    default Class<R> returnType() {
        return this.combiner().returnType();
    }

    /**
     * @return The list of all the children in the tree.
     */
    List<Child> children();

    /**
     * @param index The index of the child to get (starting from 0).
     * @return The child at index, index.
     */
    default Child getChild(int index) {
        return this.children().get(index);
    }

    @Override
    default Stream<Node<Terminals, ?, ?, ?,?>> stream() {
        return Stream.concat(Stream.of(this), children().stream().flatMap(Node::stream));
    }

    R fromList(List<?> inputs);
}
