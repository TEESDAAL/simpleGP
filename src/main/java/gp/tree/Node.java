package gp.tree;

import gp.utils.Operator;
import gp.utils.UnaryOperator;

import java.util.List;
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
        Mutable extends MutableNode<Terminals, Input, Output, Mutable, Immutable>,
        Immutable extends ImmutableNode<Terminals, Input, Output, Immutable, Mutable>
        > permits Terminal, NonTerminal, MutableNode, ImmutableNode {
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
     * @param extractor The function that extracts values from terminals
     * @param returnType The output type class
     * @return An immutable terminal node
     */
    static <Term, Out> ImmutableTerminal<Term, Out> term(
            final UnaryOperator<Term, Out> extractor,
            final Class<Out> returnType
    ) {
        return ImmutableTerminal.of(extractor, returnType);
    }

    /**
     * Creates a non-terminal node.
     * @param <Term> The terminal type
     * @param <In> The input type
     * @param <Out> The output type
     * @param function The operator function
     * @param children The child nodes
     * @param inputType The input type class
     * @param outputType The output type class
     * @return An immutable non-terminal node
     */
    static <Term, In, Out> ImmutableNonTerminal<Term, In, Out> nonTerm(
            final Operator<In, Out> function,
            final List<ImmutableNode<Term, ?, In, ?, ?>> children,
            final Class<In> inputType,
            final Class<Out> outputType
    ) {
        return new ImmutableNonTerminal<>(
                function, children, inputType, outputType
        );
    }
}
