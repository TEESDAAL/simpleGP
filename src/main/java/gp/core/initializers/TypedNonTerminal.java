package gp.core.initializers;

import utils.operators.Operator;

/**
 * Record representing a typed non-terminal node with input and return types.
 * @param <In> The input type for the operator
 * @param <Out> The return type
 * @param name The name of the non-terminal
 * @param nonTerminal The operator function
 * @param inputType The input type class
 * @param returnType The return type class
 */
public record TypedNonTerminal<In, Out>(
        String name,
        Operator<In, Out> nonTerminal,
        Class<In> inputType,
        Class<Out> returnType
) {
    /**
     * Creates a new typed non-terminal.
     * @param <In> The input type
     * @param <Out> The return type
     * @param name The name of the non-terminal
     * @param nonTerminal The operator function
     * @param inputType The input type class
     * @param returnType The return type class
     * @return A new typed non-terminal
     */
    public static <In, Out> TypedNonTerminal<In, Out> of(
            final String name,
            final Operator<In, Out> nonTerminal,
            final Class<In> inputType,
            final Class<Out> returnType
    ) {
        return new TypedNonTerminal<>(name, nonTerminal, inputType, returnType);
    }

    public static <T> TypedNonTerminal<T, T> of(
            final String name,
            final Operator<T, T> nonTerminal,
            final Class<T> type
    ) {
        return new TypedNonTerminal<>(name, nonTerminal, type, type);
    }
}
