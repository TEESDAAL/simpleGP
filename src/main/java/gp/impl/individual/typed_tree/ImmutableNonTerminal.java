package gp.impl.individual.typed_tree;

public non-sealed interface ImmutableNonTerminal<
        Terminals, R,
        Self extends ImmutableNonTerminal<Terminals, R, Self, Mutable>,
        Mutable extends MutableNonTerminal<Terminals, R, Mutable, Self>
> extends NonTerminal<
        Terminals, R,
        Self,
        ImmutableNode<Terminals, ?, ?, ?>,
        Self,
        Mutable
>, ImmutableNode<
        Terminals, R,
        Self,
        Mutable
> {}
