package gp.impl.individual.typed_tree;

import gp.core.individual.Individual;
import utils.typed_functions.TypedBiFunction;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public sealed interface Node<
    Terminals, R,
    Self extends Node<Terminals, R, Self, Immutable, Mutable>,
    Immutable extends ImmutableNode<Terminals, R, Immutable, Mutable>,
    Mutable extends MutableNode<Terminals, R, Mutable, Immutable>
> extends Individual<Terminals, R>
    permits MutableNode, ImmutableNode, NonTerminal, Terminal {
    /**
     * @return this, a helper method to recover the generic.
     */
    Self self();

    /**
     * Construct a new immutable terminal.
     * @param name The name of the terminal
     * @param terminal The extraction function that takes data from the terminal set.
     * @param returnType The return type of this terminal.
     * @return The newly constructed terminal.
     * @param <T> The type of the terminal set.
     * @param <R> The return type of this terminal
     */
    static <T, R> ImmutableTerminal<T, R> term(
        String name,
        Function<T,R> terminal,
        Class<R> returnType
    ) {
        return ImmutableTerminal.of(name, terminal, returnType);
    }

    static <A, B, ReturnType, T> ImmutableBinaryNode<A, B, T, ReturnType> nonTerm(
        String name, TypedBiFunction<A, B, ReturnType> combiner,
        ImmutableNode<T, A, ?, ?> left,
        ImmutableNode<T, B, ?, ?> right
    ) {
        return new ImmutableBinaryNode<>(
            name,
            combiner,
            left,
            right
        );
    }


    /**
     * Return this tree evaluated given the inputted terminals
     * @param terminals The terminal set to evaluate on.
     * @return The result of evaluating this tree.
     */
    R evaluate(Terminals terminals);

    /**
     * @return The string representation of this node.
     */
    String name();

    /**
     * @return The Class representing the return type of the individual.
     */
    Class<R> returnType();

    /**
     * @return An immutable Copy of this node.
     *  Returns this if this is already immutable.
     */
    Immutable immutableCopy();

    /**
     * @return A mutable copy of this node. Always makes a new node.
     */
    Mutable mutableCopy();


    /**
     * A way to extract custom values from a tree easily.
     *  explorer.nonTerminal is called if this is a non-terminal,
     *  and explorer is called if this is a terminal.
     * @param explorer The explorer to explore a given tree.
     * @return The result given by the explorer for this given node.
     * @param <T> The return type of this explorer.
     */
    @SuppressWarnings("unchecked") // All children must share the same terminals type
    default <T> T explore(TreeExplorer<Terminals, T> explorer) {
        return switch (this) {
            case NonTerminal<?, ?, ?, ?, ?, ?> nonTerm -> explorer.nonTerminal(
                (NonTerminal<Terminals, ?, ?, ?, ?, ?>) nonTerm
            );
            case Terminal<?, ?, ?> term -> explorer.terminal(
                (Terminal<Terminals, ?, ?>) term
            );
            default -> throw new IllegalStateException("Unexpected value: " + this);
        };
    }

    /**
     * @return The depth of this tree. That is the length of the
     *  longest path from this node to any leaf node
     */
    default int depth() {
        return this.explore(TreeExplorer.of(
            _ -> 0,
            (_, children) -> 1 + children.mapToInt(i->i)
                .max().orElseThrow()
        ));
    }

    /**
     * @return The total number of nodes in this subtree.
     */
    default int size() {
        return this.explore(TreeExplorer.of(
            _ -> 1,
            (_, children) -> 1 + children.mapToInt(i -> i).sum()
        ));
    }

    Stream<Node<Terminals, ?, ?, ?, ?>> stream();

    default String getExpression() {
        return this.explore(TreeExplorer.of(
            Node::name,
            (parent, children) -> parent.name()
                + children.collect(Collectors.joining(", ", "(", ")"))
        ));

    }
}

