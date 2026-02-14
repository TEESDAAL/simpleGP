package gp.initializers;

import gp.utils.UnaryOperator;


public record TypedTerminal<T, R>(UnaryOperator<T, R> terminal, Class<R> returnType) {
    public static <T, R> TypedTerminal<T, R> of(UnaryOperator<T, R> terminal, Class<R> returnType) {
        return new TypedTerminal<>(terminal, returnType);
    }
}
