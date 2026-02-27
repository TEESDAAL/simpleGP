package gp.tree;

/**
 * Marker interface for mutable nodes in the genetic programming
 * tree.
 * @param <Terminals> The type of terminal inputs
 * @param <Input> The input type for non-terminal nodes
 * @param <Output> The output type
 * @param <Self> The mutable node type
 * @param <Immutable> The immutable node type
 */
public sealed interface MutableNode<
        Terminals, Input, Output,
        Self extends MutableNode<Terminals, Input, Output, Self, Immutable>,
        Immutable extends ImmutableNode<Terminals, Input, Output,
                Immutable, Self>
        > extends Node<Terminals, Input, Output, Self, Immutable>
        permits MutableTerminal, MutableNonTerminal {
}

