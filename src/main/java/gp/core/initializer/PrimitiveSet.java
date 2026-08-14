package gp.core.initializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface PrimitiveSet<T> {
    /**
     * Create a primitive set from a list of
     *  terminals, ephemeralConstants, and NonTerminals.
     * @param terminals The Terminals
     * @param ephemeralConstants The ephemeralConstants
     * @param nonTerminals The nonTerminals.
     * @return A primitive set bundling terminals, ephemeralConstants, and NonTerminals.
     * @param <T> The terminal type.
     */
    static <T> PrimitiveSet<T> of(
        List<TypedTerminal<T, ?>> terminals,
        List<EphemeralConstant<?>> ephemeralConstants,
        List<TypedNonTerminal<?, ?>> nonTerminals
    ) {
        return new PrimitiveSetImpl<>(terminals, ephemeralConstants, nonTerminals);
    }

    /**
     * Returns the full terminal set including instantiated ephemeral constants.
     * Note: This means that if you have any ephemeral constants `this.terminals()`
     *  may give different results when calling it.
     *
     * @return The full terminal set.
     */
    List<TypedTerminal<T, ?>> terminals();

    /**
     * @return the list of nonTerminals.
     */
    List<TypedNonTerminal<?, ?>> nonTerminals();

    /**
     * Return an array of all the terminals that return a value
     *  that can be assigned to a given type (are that type or a subtype).
     * Note: This means all terminals will be return if you pass in `Class<Object>`.
     * @param returnType The return type we want to filter by.
     * @return An array of valid terminals
     * @param <R> The desired return type.
     */
    <R> TypedTerminal<T, R>[] terminalsOfType(Class<R> returnType);

    /**
     * Return an array of all the non-terminals that return a value
     *  that can be assigned to a given type (are that type or a subtype).
     * Note: All non-terminals will be return if you pass in `Class<Object>`.
     * @param returnType The return type we want to filter by.
     * @return An array of valid non-terminals.
     * @param <R> The desired return type.
     */
    <R> TypedNonTerminal<?, R>[] validNonTerminals(Class<R> returnType);

    /**
     * @return the number of terminals in this set.
     */
    default int numTerminals() {
        return terminals().size();
    }

    /**
     * @return the number of non-terminals in this set.
     */
    default int numNonTerminals() {
        return nonTerminals().size();
    }
}

@SuppressWarnings({"rawtypes", "unchecked"})
final class PrimitiveSetImpl<T> implements PrimitiveSet<T> {
    private final TypedTerminal<T, ?>[] terminals;
    private final TypedNonTerminal<?, ?>[] nonTerminals;
    private final EphemeralConstant<?>[] ephemeralConstants;

    private final Map<Class<?>, TypedNonTerminal[]> nonterminalMap = new HashMap<>();

    PrimitiveSetImpl(
        List<TypedTerminal<T, ?>> terminals,
        List<EphemeralConstant<?>> ephemeralConstants,
        List<TypedNonTerminal<?, ?>> nonTerminals
    ) {
        this.terminals = terminals.toArray(TypedTerminal[]::new);
        this.nonTerminals = nonTerminals.toArray(TypedNonTerminal<?, ?>[]::new);
        this.ephemeralConstants = ephemeralConstants.toArray(EphemeralConstant[]::new);
    }

    @Override
    public <R> TypedTerminal<T, R>[] terminalsOfType(Class<R> returnType) {
        return Stream.concat(
            Arrays.stream(ephemeralConstants)
                .filter(t -> returnType.isAssignableFrom(t.returnType()))
                .map(EphemeralConstant::<T>instantiate),
            Arrays.stream(terminals)
                .filter(term -> returnType.isAssignableFrom(term.returnType()))
        ).map(term -> (TypedTerminal<T, R>) term)
            .toArray(TypedTerminal[]::new);
    }

    @Override
    public <R> TypedNonTerminal<?, R>[] validNonTerminals(
        final Class<R> returnType
    ) {
        return nonterminalMap.computeIfAbsent(
            returnType,
            t -> Arrays.stream(nonTerminals)
                .filter(term -> t.isAssignableFrom(term.returnType()))
                .map(term -> (TypedNonTerminal<?, R>) term)
                .toArray(TypedNonTerminal[]::new)
        );
    }

    @Override
    public List<TypedTerminal<T, ?>> terminals() {
        return List.of(terminals);
    }

    @Override
    public List<TypedNonTerminal<?, ?>> nonTerminals() {
        return List.of(nonTerminals);
    }

    @Override
    public int numTerminals() {
        return terminals.length + ephemeralConstants.length;
    }

    @Override
    public int numNonTerminals() {
        return nonTerminals.length;
    }
}
