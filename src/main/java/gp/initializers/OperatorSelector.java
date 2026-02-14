package gp.initializers;

import java.util.ArrayList;
import java.util.List;

public class OperatorSelector {
    // Can't construct
    private OperatorSelector() {}

    public static <T, R> List<TypedTerminal<T, R>> validTerminals(final List<TypedTerminal<T, ?>> terminals, Class<R> returnType) {
        List<TypedTerminal<T, R>> validTerminals = new ArrayList<>();
        for (TypedTerminal<T, ?> terminal : terminals) {
            if (!returnType.isAssignableFrom(terminal.returnType())) {
                continue;
            }
            //noinspection unchecked
            validTerminals.add((TypedTerminal<T, R>)  terminal);
        }
        return validTerminals;
    }

    public static <R> List<TypedNonTerminal<?, R>> validNonTerminals(final List<TypedNonTerminal<?, ?>> nonTerminals, Class<R> returnType) {
        List<TypedNonTerminal<?, R>> validNonTerminals = new ArrayList<>();

        for (TypedNonTerminal<?, ?> nonTerminal : nonTerminals) {
            if (!returnType.isAssignableFrom(nonTerminal.returnType())) {
                continue;
            }
            //noinspection unchecked
            validNonTerminals.add((TypedNonTerminal<?, R>)  nonTerminal);
        }

        return validNonTerminals;
    }
}
