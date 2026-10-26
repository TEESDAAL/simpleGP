package gp.impl.genetic_operator;

import gp.impl.individual.typed_tree.MutableNode;
import util.result.Result;

public record RootNode<T>(MutableNode<T,?,?,?> node) implements MutableTreeNode<T> {

    @SuppressWarnings("unchecked")
    @Override
    public <Out> MutableNode<T, Out, ?, ?> castNode(
            Class<Out> returnType
    ) throws TypeMismatchException {
        if (!node.returnType().isAssignableFrom(returnType)) {
            throw new TypeMismatchException(
                    "Cannot cast node which returns " + node.returnType() + " to " +returnType+"."
            );
        }
        return (MutableNode<T, Out, ?, ?>) node;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public Result<ChildNode<T>, RootNode<T>> asChild() {
        return Result.err(this);
    }

    @Override
    public Result<RootNode<T>, ChildNode<T>> asRoot() {
        return Result.ok(this);
    }
}
