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
         *
         * @param name the name of the non-terminal
         * @param nonTerminal the operator function
         * @param inputType the input type class
         * @param returnType the return type class
         * @param <In> the input type
         * @param <Out> the return type
         * @return a new typed non-terminal
     */
    public static <In, Out> TypedNonTerminal<In, Out> of(
            final String name,
            final Operator<In, Out> nonTerminal,
            final Class<In> inputType,
            final Class<Out> returnType
    ) {
        return new TypedNonTerminal<>(name, nonTerminal, inputType, returnType);
    }

        /**
         * Creates a typed non-terminal where the input and output types match.
         *
         * @param name the name of the non-terminal
         * @param nonTerminal the operator function
         * @param type the input and output type class
         * @param <T> the type
         * @return a new typed non-terminal
         */
    public static <T> TypedNonTerminal<T, T> of(
            final String name,
            final Operator<T, T> nonTerminal,
            final Class<T> type
    ) {
        return new TypedNonTerminal<>(name, nonTerminal, type, type);
    }
}
