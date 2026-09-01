package gp.impl.individual.typed_tree;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public interface TreeExplorer<Term, T> {
    static <Term, T> TreeExplorer<Term, T> of(
            Function<Terminal<Term, ?, ?>, T> terminalFunction,
            BiFunction<NonTerminal<Term, ?, ?, ?, ?, ?>, Stream<T>, T> nonTermFunction
    ) {
        return new TreeExplorer<>() {
            @Override
            public T nonTerminal(NonTerminal<Term, ?, ?, ?, ?, ?> nonTerm) {
                return nonTermFunction.apply(nonTerm, nonTerm.children().stream()
                        .map(n -> n.explore(this)));
            }

            @Override
            public T terminal(Terminal<Term, ?, ?> term) {
                return terminalFunction.apply(term);
            }
        };
    }

    T nonTerminal(NonTerminal<Term, ?, ?, ?, ?, ?> nonTerm);

    T terminal(Terminal<Term, ?, ?> term);
}
