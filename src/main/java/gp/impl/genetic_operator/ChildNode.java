package gp.impl.genetic_operator;

import gp.impl.individual.typed_tree.MutableNode;
import gp.impl.individual.typed_tree.MutableNonTerminal;
import util.result.Result;

public record ChildNode<T>(
        MutableNode<T, ?, ?, ?> node,
        int indexInParent,
        MutableNonTerminal<T, ?, ?, ?> parent
) implements MutableTreeNode<T> {

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public Result<ChildNode<T>, RootNode<T>> asChild() {
        return Result.ok(this);
    }

    @Override
    public Result<RootNode<T>, ChildNode<T>> asRoot() {
        return Result.err(this);
    }
}
