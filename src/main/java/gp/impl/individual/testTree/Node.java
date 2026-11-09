package gp.impl.individual.testTree;

sealed interface Mutability permits Immutable, Mutable {}
enum Immutable implements Mutability {;}
enum Mutable implements Mutability {;}

sealed interface NodeType permits Terminal, NonTerminal {}
enum Terminal implements NodeType {}
enum NonTerminal implements NodeType {}

public sealed interface Node<
        Terminals, R, M extends Mutability, T extends NodeType
> permits MutableNode, ImmutableNode, TerminalNode, NonTerminalNode {
    static <T, R> MutableNonTerminal<T, R> recoverMutableNonTerminal(
            Node<T, R, Mutable, NonTerminal> node
    ) {
        return (MutableNonTerminal<T, R>) node;
    }

    static <T, R> MutableTerminal<T, R> recoverMutableTerminal(
            Node<T, R, Mutable, Terminal> node
    ) {
        return (MutableTerminal<T, R>) node;
    }

    static <T, R> ImmutableNonTerminal<T, R> recoverImmutableNonTerminal(
            Node<T, R, Immutable, NonTerminal> node
    ) {
        return (ImmutableNonTerminal<T, R>) node;
    }

    static <T, R> ImmutableTerminal<T, R> recoverImmutableTerminal(
            Node<T, R, Immutable, Terminal> node
    ) {
        return (ImmutableTerminal<T, R>) node;
    }

//    static <T, R, M extends Mutability, N extends NodeType,
//            X extends Node<T, R, M, N>>
//    X recover(Node<T, R, M, N> node) {
//        return (X) node;
//    }

    R evaluate(Terminals terminals);

    ImmutableNode<Terminals, R, T> immutableCopy();
    MutableNode<Terminals, R, T> mutableCopy();

}

sealed interface MutableNode<Terminals, R, T extends NodeType> extends Node<
        Terminals, R, Mutable, T
> permits MutableTerminal, MutableNonTerminal {}

sealed interface ImmutableNode<Terminals, R, T extends NodeType> extends Node<
        Terminals, R, Immutable, T
> permits ImmutableTerminal, ImmutableNonTerminal {}


sealed interface TerminalNode<T, R, M extends Mutability> extends Node<
        T, R, M, Terminal
> permits MutableTerminal, ImmutableTerminal {}


sealed interface NonTerminalNode<T, R, M extends Mutability> extends Node<
        T, R, M, NonTerminal
> permits MutableNonTerminal, ImmutableNonTerminal {}

non-sealed interface MutableTerminal<T, R> extends MutableNode<
        T, R, Terminal
>, TerminalNode<T, R, Mutable> {}

non-sealed interface ImmutableTerminal<T, R> extends ImmutableNode<
        T, R, Terminal
>, TerminalNode<T, R, Immutable> {}


non-sealed interface MutableNonTerminal<T, R> extends MutableNode<
        T, R, NonTerminal
        >, NonTerminalNode<T, R, Mutable> {}

non-sealed interface ImmutableNonTerminal<T, R> extends ImmutableNode<
        T, R, NonTerminal
>, NonTerminalNode<T, R, Immutable> {}
