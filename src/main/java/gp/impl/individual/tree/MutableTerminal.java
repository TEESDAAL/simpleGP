package gp.impl.individual.tree;

import utils.operators.UnaryOperator;

/**
 * A mutable terminal node implementation.
 * @param <Terminals> The type of terminal inputs
 * @param <Output> The output type
 */
public final class MutableTerminal<Terminals, Output> implements
        Terminal<Terminals, Output>,
        MutableNode<
                Terminals, Terminals, Output,
                MutableTerminal<Terminals, Output>,
                ImmutableTerminal<Terminals, Output>
        > {
    /** The extractor function for this terminal. */
    private UnaryOperator<Terminals, Output> extractor;
    /** The return type of this terminal. */
    private final Class<Output> returnType;
    /** The name of this terminal. */
    private String name;

    /**
     * Constructs a mutable terminal with the given extractor.
     * @param name The name of this terminal
     * @param extractor The function to extract values from terminals
     * @param returnType The output type
     */
    public MutableTerminal(
            final String name,
            final UnaryOperator<Terminals, Output> extractor,
            final Class<Output> returnType
    ) {
        this.name = name;
        this.extractor = extractor;
        this.returnType = returnType;
    }

    @Override
    public UnaryOperator<Terminals, Output> extractor() {
        return this.extractor;
    }

    /**
     * Sets the extractor for this terminal.
     * @param newExtractor The new extractor
     * @return This mutable terminal for method chaining
     */
    public MutableTerminal<Terminals, Output> setExtractor(
            final UnaryOperator<Terminals, Output> newExtractor
    ) {
        this.extractor = newExtractor;
        return this;
    }

    @Override
    public String name() {
        return this.name;
    }

    /**
     * Sets the name of this terminal.
     * @param name The new name
     * @return The mutable terminal with the updated name for method chaining
     */
    public MutableTerminal<Terminals, Output> setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the return type of this terminal.
     * @return The output type class
     */
    @Override
    public Class<Output> returnType() {
        return returnType;
    }
}
