package gp.core.initializer;

import utils.operators.Operator;
import utils.operators.UnaryOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class to help build primitive sets.
 * @param <T> The terminal type.
 */
public final class PrimitiveSetBuilder<T> {
    private final List<TypedTerminal<T, ?>> terminals = new ArrayList<>();
    private final List<TypedNonTerminal<?, ?>> nonTerminals = new ArrayList<>();
    private final List<EphemeralConstant<?>> ephemeralConstants = new ArrayList<>();

    private PrimitiveSetBuilder() {}

    /**
     * @return an empty PrimitiveSetBuilder.
     * @param <T> The terminal type.
     */
    public static <T> PrimitiveSetBuilder<T> empty() {
        return new PrimitiveSetBuilder<>();
    }

    /**
     * Add the terminal into the terminal set, terminals must be unique.
     * @param terminal the terminal to add.
     * @throws IllegalArgumentException if the terminal is already
     *  present in the terminal set.
     * @return this
     */
    public PrimitiveSetBuilder<T> addTerminal(TypedTerminal<T, ?> terminal)  {
        if (this.terminals.contains(terminal)) {
            throw new IllegalArgumentException("This terminal already exists");
        }
        this.terminals.add(terminal);
        return this;
    }

    /**
     * Adds a cached terminal to the terminal set. Must be unique.
     * @param name The name of the terminal.
     * @param terminal The extractor that gets the value from the given terminal.
     * @param returnType The return type of this terminal.
     * @throws IllegalArgumentException if the terminal is already
     *  present in the terminal set.
     * @return this
     * @param <R> The return type of this terminal.
     */
    public <R> PrimitiveSetBuilder<T> addTerminal(
        String name, UnaryOperator<T, R> terminal, Class<R> returnType
    ) {
        return this.addTerminal(TypedTerminal.of(name, terminal, returnType));
    }

    /**
     * Adds a non-cached terminal to the terminal set. Must be unique.
     * @param name The name of the terminal.
     * @param terminal The extractor that gets the value from the given terminal.
     * @param returnType The return type of this terminal.
     * @throws IllegalArgumentException if the terminal is already
     *  present in the terminal set.
     * @return this
     * @param <R> The return type of this terminal.
     */
    public <R> PrimitiveSetBuilder<T> addUncachedTerminal(
        String name, UnaryOperator<T, R> terminal, Class<R> returnType
    ) {
        return this.addTerminal(TypedTerminal.nonCached(name, terminal, returnType));
    }

    /**
     * Adds an ephemeral constant to the terminal set. Must be unique.
     * @param constantCreator The Supplier of the constants,
     *   it's expected that calling this multiple times will give different results.
     * @param returnType The return type of this constant.
     * @throws IllegalArgumentException if the EphemeralConstant is in the terminal set.
     * @return this
     * @param <R> The type of the returned constants.
     */
    public <R> PrimitiveSetBuilder<T> addEphemeralConstant(
        Supplier<R> constantCreator, Class<R> returnType
    ) {
        return this.addEphemeralConstant(EphemeralConstant.of(
            constantCreator, returnType
        ));
    }

    /**
     * Adds an ephemeral constant to the terminal set. Must be unique.
     * @param namingFunction The function which provides the string
     *   representation of this constant.
     * @param constantCreator The Supplier of the constants,
     *   it's expected that calling this multiple times will give different results.
     * @param returnType The return type of this constant.
     * @throws IllegalArgumentException if this EphemeralConstant is in the terminal set.
     * @return this
     * @param <R> The type of the returned constants.
     */
    public <R> PrimitiveSetBuilder<T> addEphemeralConstant(
        Function<R, String> namingFunction,
        Supplier<R> constantCreator,
        Class<R> returnType
    ) {
        return this.addEphemeralConstant(EphemeralConstant.of(
            namingFunction, constantCreator, returnType
        ));
    }

    /**
     * Adds the ephemeral constant to the terminal set. Must be unique.
     * @param ephemeralConstant The ephemeral constant to add to the terminal set.
     * @throws IllegalArgumentException if the EphemeralConstant is in the terminal set.
     * @return this
     * @param <R> The type of the returned constants.
     */
    public <R> PrimitiveSetBuilder<T> addEphemeralConstant(
        EphemeralConstant<R> ephemeralConstant
    ) {
        if (this.ephemeralConstants.contains(ephemeralConstant)) {
            throw new IllegalArgumentException("This non-terminal already exists");
        }
        this.ephemeralConstants.add(ephemeralConstant);
        return this;
    }

    /**
     * Add all the `EphemeralConstant`s in a batch to the primitive set.
     * @param ephemeralConstants a collection of terminals.
     * @throws IllegalArgumentException if any ephemeralConstants
     *  are already in the primitive set.
     * @return this
     */
    public PrimitiveSetBuilder<T> addAllEphemeralConstants(
        Collection<EphemeralConstant<?>> ephemeralConstants
    ) {
        ephemeralConstants.forEach(this::addEphemeralConstant);
        return this;
    }

    /**
     * Add all the terminals in a batch to the primitive set.
     * @param terminals a collection of terminals.
     * @throws IllegalArgumentException if any of the terminals are already
     *  in the primitive set.
     * @return this
     */
    public PrimitiveSetBuilder<T> addAllTerminals(
        Collection<TypedTerminal<T, ?>> terminals
    ) {
        terminals.forEach(this::addTerminal);
        return this;
    }

    /**
     * Adds a non-terminal to the primitive set. Must be unique
     * @param nonTerminal The TypedNonTerminal to add.
     * @throws IllegalArgumentException if nonTerminal is already in the primitive set.
     * @return this
     */
    public PrimitiveSetBuilder<T> addNonTerminal(TypedNonTerminal<?, ?> nonTerminal) {
        if (this.nonTerminals.contains(nonTerminal)) {
            throw new IllegalArgumentException("This non-terminal already exists");
        }
        this.nonTerminals.add(nonTerminal);
        return this;
    }

    /**
     * Adds a non-terminal to the primitive set. Must be unique.
     * @param name The name of the non-terminal.
     * @param nonTerminal The function to perform the extraction on
     * @param inputType The type of the input of this non-terminal.
     * @param returnType The return type of the non-terminal
     * @return this
     * @param <In> The input type of the terminal.
     * @param <Out> The output type of the terminal.
     */
    public <In, Out> PrimitiveSetBuilder<T> addNonTerminal(
            String name, Operator<In, Out> nonTerminal,
            Class<In> inputType,
            Class<Out> returnType
    ) {
        return this.addNonTerminal(TypedNonTerminal.of(
            name, nonTerminal, inputType, returnType
        ));
    }

    /**
     * Add all the non-terminals in a batch to the primitive set.
     * @param nonTerminals a collection of non-terminals.
     * @throws IllegalArgumentException if any of the non-terminals are
     *  already in the primitive set.
     * @return this
     */
    public PrimitiveSetBuilder<T> addAllNonTerminals(
        Collection<TypedNonTerminal<?, ?>> nonTerminals
    ) {
        nonTerminals.forEach(this::addNonTerminal);
        return this;
    }

    /**
     * Convert the mutable PrimitiveSetBuilder into an immutable PrimitiveSet.
     * @return An immutable primitive set.
     */
    public PrimitiveSet<T> build() {
        return PrimitiveSet.of(
                terminals, ephemeralConstants, nonTerminals
        );
    }
}
