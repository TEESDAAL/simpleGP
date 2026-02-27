package gp.tree;

/**
 * Marker interface for immutable nodes in the genetic programming tree.
 * @param <Terminals> The type of terminal inputs
 * @param <Input> The input type for non-terminal nodes
 * @param <Output> The output type
 * @param <Self> The immutable node type
 * @param <Mutable> The mutable node type
 */
public sealed interface ImmutableNode<
        Terminals, Input, Output,
        Self extends ImmutableNode<Terminals, Input, Output, Self, Mutable>,
        Mutable extends MutableNode<Terminals, Input, Output, Mutable, Self>
        > extends Node<Terminals, Input, Output, Mutable, Self>
        permits ImmutableTerminal, ImmutableNonTerminal {
}
