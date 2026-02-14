package gp.tree;

public sealed interface MutableNode<
        Terminals, Input, Output,
        Self extends MutableNode<Terminals, Input, Output, Self, Immutable>,
        Immutable extends ImmutableNode<Terminals, Input, Output, Immutable, Self>
        >  extends Node<Terminals, Input, Output, Self, Immutable> permits MutableTerminal, MutableNonTerminal {
}
