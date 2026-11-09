package gp.impl.individual.testTree;

import gp.impl.individual.testTree.visitor.TreeVisitor;
import gp.impl.individual.testTree.visitor.TypeVisitor;

import java.util.stream.Stream;

public sealed interface Node<
        Terminals, R,
        M extends Mutability,
        N extends NodeType
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

    static <T, R> ImmutableNode<T, R, ?> recoverImmutable(Node<T, R, Immutable, ?> node) {
        return (ImmutableNode<T, R, ?>) node;
    }

    static <T, R> MutableNode<T, R, ?> recoverMutable(Node<T, R, Mutable, ?> node) {
        return (MutableNode<T, R, ?>) node;
    }

    static <T, R> TerminalNode<T, R, ?> recoverTerminal(Node<T, R, ?, Terminal> node) {
        return (TerminalNode<T, R, ?>) node;
    }

    static <T, R> NonTerminalNode<T, R, ?> recoverNonTerminal(Node<T, R, ?, NonTerminal> node) {
        return (NonTerminalNode<T, R, ?>) node;
    }

    R evaluate(Terminals terminals);

    String name();

    Class<R> returnType();

    ImmutableNode<Terminals, R, N> immutableCopy();
    MutableNode<Terminals, R, N> mutableCopy();

    <U> U visit(TreeVisitor<Terminals, R, U, M> visitor);

    Stream<Node<Terminals, ?, M, ?>> stream();

    int depth();

    int size();

    default String toLispExpression() {
        this.visit(new TreeVisitor<>() {
            @Override
            public String terminal(TerminalNode<Terminals, R, M> term) {
                return term.name();
            }

            @Override
            public String nonTerminal(NonTerminalNode<Terminals, R, M> nonTerm) {
                return nonTerm.name() + nonTerm.children().stream();
            }
        });
    }
}

