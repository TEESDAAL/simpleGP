package gp.initializers;

import java.util.List;
import java.util.Map;

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
            final Map<Class<?>, List<TypedTerminal<T, ?>>> terminals,
            final Class<R> returnType
    ) {
        //noinspection unchecked, rawtypes
        return (List<TypedTerminal<T, R>>) (List) terminals.get(returnType);
    }

    /**
     * Gets valid non-terminals of the given return type.
     * @param <R> The return type
     * @param nonTerminals Map of non-terminals by return type
     * @param returnType The desired return type
     * @return List of non-terminals with the given return type
     */
    public static <R> List<TypedNonTerminal<?, R>> validNonTerminals(
            final Map<Class<?>, List<TypedNonTerminal<?, ?>>> nonTerminals,
            final Class<R> returnType
    ) {
        //noinspection unchecked, rawtypes
        return (List<TypedNonTerminal<?, R>>) (List)
                nonTerminals.get(returnType);
    }
}
