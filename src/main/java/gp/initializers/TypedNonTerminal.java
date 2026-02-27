package gp.initializers;

import gp.utils.Operator;

/**
 * Record representing a typed non-terminal node with input and return types.
 * @param <In> The input type for the operator
 * @param <Out> The return type
 * @param nonTerminal The operator function
 * @param inputType The input type class
 * @param returnType The return type class
 */
public record TypedNonTerminal<In, Out>(
        Operator<In, Out> nonTerminal,
        Class<In> inputType,
        Class<Out> returnType
) {
    /**
     * Creates a new typed non-terminal.
     * @param <In> The input type
     * @param <Out> The return type
     * @param nonTerminal The operator function
     * @param inputType The input type class
     * @param returnType The return type class
     * @return A new typed non-terminal
     */
    public static <In, Out> TypedNonTerminal<In, Out> of(
            final Operator<In, Out> nonTerminal,
            final Class<In> inputType,
            final Class<Out> returnType
    ) {
        return new TypedNonTerminal<>(nonTerminal, inputType, returnType);
    }
}
