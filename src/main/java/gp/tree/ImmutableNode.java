package gp.tree;

public sealed interface ImmutableNode<
        Terminals, Input, Output,
        Self extends ImmutableNode<Terminals, Input, Output, Self, Mutable>,
        Mutable extends MutableNode<Terminals, Input, Output, Mutable, Self>
        > extends Node<Terminals, Input, Output, Mutable, Self> permits ImmutableTerminal, ImmutableNonTerminal {

}
