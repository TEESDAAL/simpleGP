package gp.initializers;

import gp.utils.Operator;

public record TypedNonTerminal<In, Out>(Operator<In, Out> nonTerminal, Class<In> inputType, Class<Out> returnType) {
    public static <In, Out> TypedNonTerminal<In, Out> of(Operator<In, Out> nonTerminal, Class<In> inputType, Class<Out> returnType) {
        return new TypedNonTerminal<>(nonTerminal, inputType, returnType);
    }
}
