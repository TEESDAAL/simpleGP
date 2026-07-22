package gp.impl.individual.tree;

import utils.operators.UnaryOperator;

/**
 * A mutable terminal node implementation.
 * @param <Terminals> The type of terminal inputs
 * @param <Output> The output type
 */
public final class MutableTerminal<Terminals, Output> extends Terminal<Terminals, Output>
    implements MutableNode<
            Terminals, Terminals, Output,
            MutableTerminal<Terminals, Output>,
            ImmutableTerminal<Terminals, Output>
    > {

    /**
     * Constructs a mutable terminal with the given extractor.
     * @param name The name of this terminal
     * @param extractor The function to extract values from terminals
     * @param returnType The output type
     */
    public MutableTerminal(
            String name,
            UnaryOperator<Terminals, Output> extractor,
            Class<Output> returnType
    ) {
        super(name, extractor, returnType);
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

    /**
     * Sets the name of this terminal.
     * @param name The new name
     * @return The mutable terminal with the updated name for method chaining
     */
    public MutableTerminal<Terminals, Output> setName(final String name) {
        this.name = name;
        return this;
    }


}
