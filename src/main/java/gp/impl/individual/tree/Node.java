package gp.impl.individual.tree;

import utils.operators.Operator;
import utils.operators.UnaryOperator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a node in a genetic programming tree.
 * @param <Terminals> The type of terminal inputs
 * @param <Input> The input type for non-terminal nodes
 * @param <Output> The output type
 * @param <Mutable> The mutable variant of this node
 * @param <Immutable> The immutable variant of this node
 */
public sealed interface Node<
    Terminals, Input, Output,
        Mutable extends MutableNode<
            Terminals, Input, Output, Mutable, Immutable
        >,
        Immutable extends ImmutableNode<
            Terminals, Input, Output, Immutable, Mutable
        >
> permits Terminal, NonTerminal, MutableNode, ImmutableNode {

    default <T> T extract(
        TreeExtractor<T> extractor
    ) {
        return switch (this) {
            case Terminal<?, ?> term -> extractor.terminal(term);
            case NonTerminal<?, ?, ?, ?> nonTerm -> extractor.nonTerminal(nonTerm);
        };
    }

    /**
     * @return The Lisp expression representing this tree.
     */
    default String getExpression() {
        return this.extract(new LispString());
    }

    /**
     * @return The name of this node.
     */
    String name();

    /**
     * Returns a stream of all nodes in this subtree.
     * @return A stream of all nodes
     */
    Stream<Node<Terminals, ?, ?, ?, ?>> stream();

    /**
     * Gets the return type of this node.
     * @return The class representing the output type
     */
    Class<Output> returnType();

    /**
     * Evaluates this node on the given terminal inputs.
     * @param terminals The terminal inputs
     * @return The output value
     */
    Output evaluate(Terminals terminals);

    /**
     * Creates a mutable copy of this node.
     * @return A mutable copy
     */
    Mutable mutableCopy();

    /**
     * Creates an immutable copy of this node.
     * @return An immutable copy
     */
    Immutable immutableCopy();

    /**
     * Returns the depth of this node in the tree.
     * @return The depth (0 for terminals)
     */
    int depth();

    /**
     * Creates a terminal node.
     * @param <Term> The terminal type
     * @param <Out> The output type
     * @param name The name of the terminal
     * @param extractor The function that extracts values from terminals
     * @param returnType The output type class
     * @return An immutable terminal node
     */
    static <Term, Out> ImmutableTerminal<Term, Out> term(
            String name,
            final UnaryOperator<Term, Out> extractor,
            final Class<Out> returnType
    ) {
        return ImmutableTerminal.of(name, extractor, returnType);
    }

    /**
     * Creates a non-terminal node.
     * @param <Term> The terminal type
     * @param <In> The input type
     * @param <Out> The output type
     * @param name The name of the non-terminal
     * @param function The operator function
     * @param children The child nodes
     * @param inputType The input type class
     * @param outputType The output type class
     * @return An immutable non-terminal node
     */
    static <Term, In, Out> ImmutableNonTerminal<Term, In, Out> nonTerm(
            final String name,
            final Operator<In, Out> function,
            final List<ImmutableNode<Term, ?, In, ?, ?>> children,
            final Class<In> inputType,
            final Class<Out> outputType
    ) {
        return new ImmutableNonTerminal<>(
                name, function, children, inputType, outputType
        );
    }
}

class LispString implements TreeExtractor<String> {
    @Override
    public String terminal(Terminal<?, ?> node) {
        return node.name();
    }

    @Override
    public String nonTerminal(NonTerminal<?, ?, ?, ?> node) {
        return node.name() + node.children().stream()
            .map(n -> n.extract(this))
            .collect(Collectors.joining(", ", "(", ")"));
    }
}