package gp.impl.individual.testTree;

public sealed interface ImmutableNode<Terminals, R, T extends NodeType> extends Node<
        Terminals, R, Immutable, T
> permits ImmutableNonTerminal, ImmutableTerminal {
    default ImmutableNode<Terminals, R, T> immutableCopy() {
        return this;
    }
}
