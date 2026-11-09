package gp.impl.individual.testTree.visitor;

import gp.impl.individual.testTree.Mutability;
import gp.impl.individual.testTree.NonTerminalNode;
import gp.impl.individual.testTree.TerminalNode;

public interface TreeVisitor<T, R, U, M extends Mutability> {
    U terminal(TerminalNode<T,R,M> term);
    U nonTerminal(NonTerminalNode<T,R,M> nonTerm);
}
