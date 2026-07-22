package gp.impl.individual.tree;

import utils.operators.UnaryOperator;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents a terminal node in a genetic programming tree.
 * A terminal is a leaf node that extracts a value from terminal inputs.
 * @param <Terminals> The type of terminal inputs
 * @param <Output> The output type of this terminal
 */
public sealed abstract class Terminal<Terminals, Output> implements Node<
        Terminals, Terminals, Output,
        MutableTerminal<Terminals, Output>,
        ImmutableTerminal<Terminals, Output>
> permits MutableTerminal, ImmutableTerminal {
    /** The extractor function for this terminal. */
    protected UnaryOperator<Terminals, Output> extractor;
    /** The return type of this terminal. */
    protected Class<Output> returnType;
    /** The name of this terminal. */
    protected String name;

    public Terminal(
        String name,
        UnaryOperator<Terminals,Output> extractor,
        Class<Output> returnType
    ) {
        this.name = Objects.requireNonNull(name);
        this.extractor = Objects.requireNonNull(extractor);
        this.returnType = Objects.requireNonNull(returnType);
    }

    /**
     * Creates a mutable copy of this terminal.
     * @return A mutable copy of this terminal
     */
    public MutableTerminal<Terminals, Output> mutableCopy() {
        return new MutableTerminal<>(
                this.name,
                this.extractor,
                this.returnType
        );
    }

    /**
     * Creates an immutable copy of this terminal.
     * @return An immutable copy of this terminal
     */
    public ImmutableTerminal<Terminals, Output> immutableCopy() {
        return ImmutableTerminal.of(
                this.name,
                this.extractor,
                this.returnType
        );
    }

    @Override
    public String name() {
        return this.name;
    }

    /**
     * Gets the extractor of this terminal.
     * @return The extractor function
     */
    public UnaryOperator<Terminals, Output> extractor() {
        return this.extractor;
    }

    /**
     * Gets the return type of this terminal.
     * @return The output type class
     */
    @Override
    public Class<Output> returnType() {
        return returnType;
    }

    /**
     * Evaluates this terminal on the given inputs.
     * @param input The terminal inputs
     * @return The output value
     */
    @Override
    public Output evaluate(Terminals input) {
        return this.extractor.produce(input);
    }

    @Override
    public Output performantEvaluate(Terminals terminals, Object[] inputs) {
        return this.extractor.produce(terminals);
    }



    @Override
    public Stream<Node<Terminals, ?, ?, ?, ?>> stream() {
        return Stream.of(this);
    }

    @Override
    public int depth() {
        return 0;
    }
}
