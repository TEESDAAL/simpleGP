package gp.impl.individual.testTree;

import util.Updater;

public non-sealed interface MutableNonTerminal<T, R> extends MutableNode<
        T, R, NonTerminal
>, NonTerminalNode<T, R, Mutable> {
    MutableNonTerminal<T, R> replaceChild(
            int index,
            Updater<MutableNode<T, ?, ?>> node
    );
}
