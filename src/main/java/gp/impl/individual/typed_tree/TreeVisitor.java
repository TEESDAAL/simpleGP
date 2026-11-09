package gp.impl.individual.typed_tree;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public interface TreeVisitor<Term, T, M> {
    static <Term, T, M> TreeVisitor<Term, T, M> of(
            Function<Terminal<Term, ?, M, ?>, T> terminalFunction,
            BiFunction<NonTerminal<Term, ?, M, ?, ?, ?>, Stream<T>, T> nonTermFunction
    ) {
        return new TreeVisitor<>() {
            @Override
            public T nonTerminal(NonTerminal<Term, ?, M, ?, ?, ?> nonTerm) {
                return nonTermFunction.apply(nonTerm, nonTerm.children().stream()
                        .map(n -> n.visit(this)));
            }

            @Override
            public T terminal(Terminal<Term, ?, M, ?> term) {
                return terminalFunction.apply(term);
            }
        };
    }

    T nonTerminal(NonTerminal<Term, ?, M, ?, ?, ?> nonTerm);

    T terminal(Terminal<Term, ?, M, ?> term);
}
