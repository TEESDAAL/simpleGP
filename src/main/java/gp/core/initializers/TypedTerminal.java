package gp.core.initializers;

import utils.operators.UnaryOperator;


/**
 * Record representing a typed terminal node with its return type.
 * @param <T> The terminal input type
 * @param <R> The return type
 * @param name The name of the terminal
 * @param terminal The extractor function
 * @param returnType The return type class
 */
public record TypedTerminal<T, R>(
        String name, UnaryOperator<T, R> terminal, Class<R> returnType
) {
    /**
     * Creates a new typed terminal.
     * @param <T> The terminal input type
     * @param <R> The return type
     * @param name The name of the terminal
     * @param terminal The extractor function
     * @param returnType The return type class
     * @return A new typed terminal
     */
    public static <T, R> TypedTerminal<T, R> of(
            String name,
            final UnaryOperator<T, R> terminal,
            final Class<R> returnType
    ) {
        return new TypedTerminal<>(name, terminal.cached(), returnType);
    }

    /**
     * Creates a terminal without caching.
     *
     * @param name the terminal name
     * @param terminal the terminal function
     * @param returnType the return type class
     * @param <T> the terminal input type
     * @param <R> the return type
     * @return a typed terminal
     */
    public static <T, R> TypedTerminal<T, R> nonCached(
            String name,
            final UnaryOperator<T, R> terminal,
            final Class<R> returnType
    ) {
        return new TypedTerminal<>(name, terminal, returnType);
    }
}
