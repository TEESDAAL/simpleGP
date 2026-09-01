package gp.impl.individual.typed_tree;

import util.Updater;

public non-sealed interface MutableNonTerminal<
        Terminals, R,
        Self extends MutableNonTerminal<Terminals, R, Self, Immutable>,
        Immutable extends ImmutableNonTerminal<Terminals, R, Immutable, Self>
> extends NonTerminal<
        Terminals, R,
        Self,
        MutableNode<Terminals, ?, ?, ?>,
        Immutable,
        Self
>, MutableNode<
        Terminals, R,
        Self,
        Immutable
> {
    Self replaceChild(int index, Updater<MutableNode<Terminals, ?, ?, ?>> node);
}
