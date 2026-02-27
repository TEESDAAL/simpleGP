package gp.tree;

import gp.utils.UnaryOperator;

import java.util.stream.Stream;

/**
 * Represents a terminal node in a genetic programming tree.
 * A terminal is a leaf node that extracts a value from terminal inputs.
 * @param <Terminals> The type of terminal inputs
 * @param <Output> The output type of this terminal
 */
public sealed interface Terminal<Terminals, Output>
        extends Node<
        Terminals, Terminals, Output,
        MutableTerminal<Terminals, Output>,
        ImmutableTerminal<Terminals, Output>
        > permits MutableTerminal, ImmutableTerminal {

    /**
     * Gets the extractor function for this terminal.
     * @return The unary operator that extracts a value from terminals
     */
    UnaryOperator<Terminals, Output> extractor();

    /**
     * Creates a mutable copy of this terminal.
     * @return A mutable copy of this terminal
     */
    default MutableTerminal<Terminals, Output> mutableCopy() {
        return new MutableTerminal<>(
                extractor(),
                returnType()
        );
    }


    /**
     * Creates an immutable copy of this terminal.
     * @return An immutable copy of this terminal
     */
    default ImmutableTerminal<Terminals, Output> immutableCopy() {
        return new ImmutableTerminal<>(
                extractor(),
                returnType()
        );
    }


    /**
     * Evaluates this terminal on the given inputs.
     * @param input The terminal inputs
     * @return The output value
     */
    default Output evaluate(Terminals input) {
        return this.extractor().produce(input);
    }

    @Override
    default Stream<Node<Terminals, ?, ?, ?, ?>> stream() {
        return Stream.of(this);
    }

    @Override
    default int depth() {
        return 0;
    }
}
