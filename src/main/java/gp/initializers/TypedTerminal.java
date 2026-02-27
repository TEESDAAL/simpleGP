package gp.initializers;

import gp.utils.UnaryOperator;


/**
 * Record representing a typed terminal node with its return type.
 * @param <T> The terminal input type
 * @param <R> The return type
 * @param terminal The extractor function
 * @param returnType The return type class
 */
public record TypedTerminal<T, R>(
        UnaryOperator<T, R> terminal, Class<R> returnType) {
    /**
     * Creates a new typed terminal.
     * @param <T> The terminal input type
     * @param <R> The return type
     * @param terminal The extractor function
     * @param returnType The return type class
     * @return A new typed terminal
     */
    public static <T, R> TypedTerminal<T, R> of(
            final UnaryOperator<T, R> terminal,
            final Class<R> returnType
    ) {
        return new TypedTerminal<>(terminal, returnType);
    }
}
