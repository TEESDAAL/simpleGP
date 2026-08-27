package gp.impl.individual.typed_tree;

public sealed interface ImmutableNode<
    Terminals, R,
    Self extends ImmutableNode<Terminals, R, Self, Mutable>,
    Mutable extends MutableNode<Terminals, R, Mutable, Self>
    > extends Node<Terminals, R, Self, Self, Mutable>
    permits ImmutableNonTerminal, ImmutableTerminal {}
