package gp.initializers;

import java.util.Collections;
import java.util.List;

/**
 * Utility class for selecting valid terminals and non-terminals by return type.
 */
public final class OperatorSelector {
    private OperatorSelector() {
        // Cannot construct
    }

    /**
     * Gets valid terminals of the given return type.
     * @param <T> The terminal input type
     * @param <R> The return type
     * @param terminals Map of terminals by return type
     * @param returnType The desired return type
     * @return List of terminals with the given return type
     */
    public static <T, R> List<TypedTerminal<T, R>> validTerminals(
            final List<TypedTerminal<T, ?>> terminals,
            final Class<R> returnType
    ) {
        //noinspection unchecked
        return terminals.stream()
                .filter(term -> returnType.isAssignableFrom(term.returnType()))
                .map(term -> (TypedTerminal<T, R>) term)
                .toList();
    }

    /**
     * Gets valid non-terminals of the given return type.
     * @param <R> The return type
     * @param nonTerminals Map of non-terminals by return type
     * @param returnType The desired return type
     * @return List of non-terminals with the given return type
     */
    public static <R> List<TypedNonTerminal<?, R>> validNonTerminals(
            final List<TypedNonTerminal<?, ?>> nonTerminals,
            final Class<R> returnType
    ) {

        // Collections.unmodifiableList needed to make the type system happy
        //noinspection unchecked
        return Collections.unmodifiableList(nonTerminals.stream()
                .filter(term -> returnType.isAssignableFrom(term.returnType()))
                .map(term -> (TypedNonTerminal<?, R>) term)
                .toList());
    }
}
