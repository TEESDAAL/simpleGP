package gp.impl.individual.tree;

public interface TreeExtractor<T> {
    T terminal(Terminal<?,?> node);

    T nonTerminal(NonTerminal<?,?,?,?> node);
}
