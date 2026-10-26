package gp.impl.genetic_operator;

import gp.impl.individual.typed_tree.MutableNode;
import gp.impl.individual.typed_tree.MutableNonTerminal;
import util.result.Result;

import java.util.ArrayList;
import java.util.List;


/**
 * A record that holds a mutable node along with its parent
 * and potentially the index of the child in the parent's children list.
 */
public sealed interface MutableTreeNode<T> permits ChildNode, RootNode {
    static <T> List<MutableTreeNode<T>> collect(MutableNode<T, ?, ?, ?> root) {
        final List<MutableTreeNode<T>> result = new ArrayList<>();
        result.add(ofRoot(root));
        collectHelper(root, result);
        return List.copyOf(result);
    }

    @SuppressWarnings("unchecked")
    static <T> void collectHelper(
            MutableNode<T, ?, ?, ?> node,
            List<MutableTreeNode<T>> result
    ) {
        if (!(node instanceof MutableNonTerminal<?, ?, ?, ?>)) {
            return;
        }
        final MutableNonTerminal<T, ?, ?, ?> nonTerminal =
                (MutableNonTerminal<T, ?, ?, ?>) node;

        int i = 0;
        for (final MutableNode<T, ?, ?, ?> child : nonTerminal.children()) {
            result.add(new ChildNode<>(child, i, nonTerminal));
            i += 1;
            collectHelper(child, result);
        }
    }
    private static <T> MutableTreeNode<T> ofRoot(MutableNode<T, ?, ?, ?> node) {
        return new RootNode<>(node);
    }
    
    MutableNode<T, ?, ?, ?> node();
    <Out> MutableNode<T, Out, ?, ?> castNode(Class<Out> returnType);
    boolean isRoot();
    Result<ChildNode<T>, RootNode<T>> asChild();
    Result<RootNode<T>, ChildNode<T>> asRoot();
}
