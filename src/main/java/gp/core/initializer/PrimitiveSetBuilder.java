package gp.core.initializer;


import gp.impl.individual.typed_tree.NamedNodeFunction;
import util.QuadFunction;
import util.TriFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * A utility class to help build primitive sets.
 *
 * @param <T> The terminal type.
 */
public interface PrimitiveSetBuilder<T, Self extends PrimitiveSetBuilder<T, Self>> {

    /**
     * @param <T> The terminal type.
     * @return an empty PrimitiveSetBuilder.
     */
    static <T> PrimitiveSetBuilder<T, ?> empty() {
        return PrimitiveSetBuilderImpl.empty();
    }

    static <T, R> PrimitiveSetBuilderWithDefaultClass<T, R> withDefaultClass(
            Class<R> defaultType
    ) {
        return new PrimitiveSetBuilderWithDefaultClass<T, R>(
                empty(),
                defaultType
        );
    }

    Self self();

    /**
     * Add the terminal into the terminal set, terminals must be unique.
     *
     * @param terminal the terminal to add.
     * @return this
     * @throws IllegalArgumentException if the terminal is already
     *                                  present in the terminal set.
     */
    Self addTerminal(TypedTerminal<T, ?> terminal);


    /**
     * Adds a non-terminal to the primitive set. Must be unique
     *
     * @param nonTerminal The NodeFunction to add.
     * @return this
     * @throws IllegalArgumentException if nonTerminal is already in the primitive set.
     */
    Self addNonTerminal(NamedNodeFunction<?, ?> nonTerminal);

    /**
     * Adds the ephemeral constant to the terminal set. Must be unique.
     *
     * @param ephemeralConstant The ephemeral constant to add to the terminal set.
     * @param <R>               The type of the returned constants.
     * @return this
     * @throws IllegalArgumentException if the EphemeralConstant is in the terminal set.
     */
    <R> Self addEphemeralConstant(
            EphemeralConstant<R> ephemeralConstant
    );

    /**
     * Convert the mutable PrimitiveSetBuilder into an immutable PrimitiveSet.
     *
     * @return An immutable primitive set.
     */
    PrimitiveSet<T> build();

    /**
     * Adds a cached terminal to the terminal set. Must be unique.
     *
     * @param name       The name of the terminal.
     * @param terminal   The extractor that gets the value from the given terminal.
     * @param returnType The return type of this terminal.
     * @param <R>        The return type of this terminal.
     * @return this
     * @throws IllegalArgumentException if the terminal is already
     *                                  present in the terminal set.
     */
    default <R> Self addTerminal(
            String name, Function<T, R> terminal, Class<R> returnType
    ) {
        return this.addTerminal(TypedTerminal.of(name, terminal, returnType));
    }

    /**
     * Adds a non-cached terminal to the terminal set. Must be unique.
     *
     * @param name       The name of the terminal.
     * @param terminal   The extractor that gets the value from the given terminal.
     * @param returnType The return type of this terminal.
     * @param <R>        The return type of this terminal.
     * @return this
     * @throws IllegalArgumentException if the terminal is already
     *                                  present in the terminal set.
     */
    default <R> Self addUncachedTerminal(
            String name, Function<T, R> terminal, Class<R> returnType
    ) {
        return this.addTerminal(TypedTerminal.nonCached(name, terminal, returnType));
    }

    /**
     * Adds an ephemeral constant to the terminal set. Must be unique.
     *
     * @param constantCreator The Supplier of the constants,
     *                        it's expected that calling this multiple times
     *                        will give different results.
     * @param returnType      The return type of this constant.
     * @param <R>             The type of the returned constants.
     * @return this
     * @throws IllegalArgumentException if the EphemeralConstant is in the terminal set.
     */
    default <R> Self addEphemeralConstant(
            Supplier<R> constantCreator, Class<R> returnType
    ) {
        return this.addEphemeralConstant(EphemeralConstant.of(
                constantCreator, returnType
        ));
    }

    /**
     * Adds an ephemeral constant to the terminal set. Must be unique.
     *
     * @param namingFunction  The function which provides the string
     *                        representation of this constant.
     * @param constantCreator The Supplier of the constants,
     *                        it's expected that calling this multiple times
     *                        will give different results.
     * @param returnType      The return type of this constant.
     * @param <R>             The type of the returned constants.
     * @return this
     * @throws IllegalArgumentException if this EphemeralConstant is in the terminal set.
     */
    default <R> Self addEphemeralConstant(
            Function<R, String> namingFunction,
            Supplier<R> constantCreator,
            Class<R> returnType
    ) {
        return this.addEphemeralConstant(EphemeralConstant.of(
                namingFunction, constantCreator, returnType
        ));
    }

    /**
     * Add all the `EphemeralConstant`s in a batch to the primitive set.
     *
     * @param ephemeralConstants a collection of terminals.
     * @return this
     * @throws IllegalArgumentException if any ephemeralConstants
     *                                  are already in the primitive set.
     */
    default Self addAllEphemeralConstants(
            Collection<EphemeralConstant<?>> ephemeralConstants
    ) {
        ephemeralConstants.forEach(this::addEphemeralConstant);
        return self();
    }

    /**
     * Add all the terminals in a batch to the primitive set.
     *
     * @param terminals a collection of terminals.
     * @return this
     * @throws IllegalArgumentException if any of the terminals are already
     *                                  in the primitive set.
     */
    default Self addAllTerminals(
            Collection<TypedTerminal<T, ?>> terminals
    ) {
        terminals.forEach(this::addTerminal);
        return self();
    }

    default <A, R> Self addNonTerminal(
            String name,
            Function<A, R> nonTerminal,
            Class<A> inputType,
            Class<R> returnType
    ) {
        return this.addNonTerminal(NamedNodeFunction.of(
                name, nonTerminal, inputType, returnType
        ));
    }

    default <A, B, R> Self addNonTerminal(
            String name,
            BiFunction<A, B, R> nonTerminal,
            Class<A> leftType,
            Class<B> rightType,
            Class<R> returnType
    ) {
        this.addNonTerminal(NamedNodeFunction.of(
                name, nonTerminal, leftType, rightType, returnType
        ));
        return self();
    }

    default <A, B, C, R> Self addNonTerminal(
            String name,
            TriFunction<A, B, C, R> nonTerminal,
            Class<A> leftType,
            Class<B> middleType,
            Class<C> rightType,
            Class<R> returnType
    ) {
        this.addNonTerminal(NamedNodeFunction.of(
                name, nonTerminal, leftType, middleType, rightType, returnType
        ));
        return self();
    }

    /**
     * Add all the non-terminals in a batch to the primitive set.
     *
     * @param nonTerminals a collection of non-terminals.
     * @return this
     * @throws IllegalArgumentException if any of the non-terminals are
     *                                  already in the primitive set.
     */
    default Self addAllNonTerminals(
            Collection<NamedNodeFunction<?, ?>> nonTerminals
    ) {
        nonTerminals.forEach(this::addNonTerminal);
        return self();
    }

    default <A, B, C, D, R> Self addNonTerminal(
            String name,
            QuadFunction<A, B, C, D, R> nonTerminal,
            Class<A> leftType,
            Class<B> leftMiddleType,
            Class<C> rightMiddleType,
            Class<D> rightType,
            Class<R> returnType
    ) {
        return this.addNonTerminal(NamedNodeFunction.of(
                name,
                nonTerminal,
                leftType,
                leftMiddleType,
                rightMiddleType,
                rightType,
                returnType
        ));
    }
}

/**
 * A utility class to help build primitive sets.
 *
 * @param <T> The terminal type.
 */
final class PrimitiveSetBuilderImpl<T> implements PrimitiveSetBuilder<
        T,
        PrimitiveSetBuilderImpl<T>
        > {
    private final List<TypedTerminal<T, ?>> terminals = new ArrayList<>();
    private final List<NamedNodeFunction<?, ?>> nonTerminals = new ArrayList<>();
    private final List<EphemeralConstant<?>> ephemeralConstants = new ArrayList<>();

    private PrimitiveSetBuilderImpl() {
    }

    /**
     * @param <T> The terminal type.
     * @return an empty PrimitiveSetBuilder.
     */
    public static <T> PrimitiveSetBuilder<T, ?> empty() {
        return new PrimitiveSetBuilderImpl<>();
    }

    @Override
    public PrimitiveSetBuilderImpl<T> self() {
        return this;
    }

    /**
     * Add the terminal into the terminal set, terminals must be unique.
     *
     * @param terminal the terminal to add.
     * @return this
     * @throws IllegalArgumentException if the terminal is already
     *                                  present in the terminal set.
     */
    public PrimitiveSetBuilderImpl<T> addTerminal(TypedTerminal<T, ?> terminal) {
        if (this.terminals.contains(terminal)) {
            throw new IllegalArgumentException("This terminal already exists");
        }
        this.terminals.add(terminal);
        return this;
    }

    /**
     * Adds the ephemeral constant to the terminal set. Must be unique.
     *
     * @param ephemeralConstant The ephemeral constant to add to the terminal set.
     * @param <R>               The type of the returned constants.
     * @return this
     * @throws IllegalArgumentException if the EphemeralConstant is in the terminal set.
     */
    public <R> PrimitiveSetBuilderImpl<T> addEphemeralConstant(
            EphemeralConstant<R> ephemeralConstant
    ) {
        if (this.ephemeralConstants.contains(ephemeralConstant)) {
            throw new IllegalArgumentException("This non-terminal already exists");
        }
        this.ephemeralConstants.add(ephemeralConstant);
        return this;
    }

    /**
     * Adds a non-terminal to the primitive set. Must be unique
     *
     * @param nonTerminal The NodeFunction to add.
     * @return this
     * @throws IllegalArgumentException if nonTerminal is already in the primitive set.
     */
    @Override
    public PrimitiveSetBuilderImpl<T> addNonTerminal(
            NamedNodeFunction<?, ?> nonTerminal
    ) {
        if (this.nonTerminals.contains(nonTerminal)) {
            throw new IllegalArgumentException("This non-terminal already exists");
        }
        this.nonTerminals.add(nonTerminal);
        return this;
    }

    /**
     * Convert the mutable PrimitiveSetBuilder into an immutable PrimitiveSet.
     *
     * @return An immutable primitive set.
     */
    public PrimitiveSet<T> build() {
        return PrimitiveSet.of(
                terminals,
                ephemeralConstants,
                nonTerminals
        );
    }
}
