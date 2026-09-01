package gp.impl.genetic_operator;

import gp.impl.individual.typed_tree.MutableNode;
import gp.impl.individual.typed_tree.MutableNonTerminal;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.ArrayList;
import java.util.List;

/**
 * A record that holds a mutable node along with its parent
 * and the index of the child in the parent's children list.
 *
 * @param node          The mutable child node
 * @param indexInParent The index of this node in its parent's children list,
 *                      if it has a parent
 * @param parent        The parent non-terminal of this node, if it has one
 * @param <T>           The terminal type
 */
public record NodeWithParentOrRoot<T>(
        MutableNode<T, ?, ?, ?> node,
        OptionalInt indexInParent,
        Optional<MutableNonTerminal<T, ?, ?, ?>> parent
) {
    static <T> List<NodeWithParentOrRoot<T>> collect(MutableNode<T, ?, ?, ?> root) {
        final List<NodeWithParentOrRoot<T>> result = new ArrayList<>();
        result.add(NodeWithParentOrRoot.ofRoot(root));
        collectHelper(root, result);
        return List.copyOf(result);
    }

    private static <T> NodeWithParentOrRoot<T> ofRoot(MutableNode<T, ?, ?, ?> node) {
        return new NodeWithParentOrRoot<>(node, OptionalInt.empty(), Optional.empty());
    }

    @SuppressWarnings("unchecked")
    static <T> void collectHelper(
            MutableNode<T, ?, ?, ?> node, List<NodeWithParentOrRoot<T>> result
    ) {
        if (!(node instanceof MutableNonTerminal<?, ?, ?, ?>)) {
            return;
        }
        final MutableNonTerminal<T, ?, ?, ?> nonTerminal =
                (MutableNonTerminal<T, ?, ?, ?>) node;

        int i = 0;
        for (final MutableNode<T, ?, ?, ?> child : nonTerminal.children()) {
            result.add(new NodeWithParentOrRoot<>(
                    child,
                    OptionalInt.of(i),
                    Optional.of(nonTerminal)
            ));

            i += 1;
            collectHelper(child, result);
        }
    }

    boolean isRoot() {
        return parent.isEmpty();
    }

    NodeWithParent<T> notRootOrThrow() {
        return new NodeWithParent<>(
                node,
                indexInParent.orElseThrow(),
                parent.orElseThrow()
        );
    }
}

record NodeWithParent<T>(
        MutableNode<T, ?, ?, ?> node,
        int indexInParent,
        MutableNonTerminal<T, ?, ?, ?> parent
) {

}
