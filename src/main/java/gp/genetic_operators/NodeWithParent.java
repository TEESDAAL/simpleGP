package gp.genetic_operators;

import gp.tree.MutableNode;
import gp.tree.MutableNonTerminal;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.ArrayList;
import java.util.List;

/**
 * A record that holds a mutable node along with its parent
 * and the index of the child in the parent's children list.
 * @param node The mutable child node
 * @param childIndex The index of this node in its parent's children list,
 *                   if it has a parent
 * @param parent The parent non-terminal of this node, if it has one
 * @param <T> The terminal type
 * @param <Out> The output type of the child node
 */
public record NodeWithParent<T, Out>(
        MutableNode<T, ?, Out, ?, ?> node,
        OptionalInt childIndex,
        Optional<MutableNonTerminal<T, Out, ?>> parent
) {
    static <T> List<NodeWithParent<T, ?>> collect(MutableNode<T, ?, ?, ?, ?> node) {
        List<NodeWithParent<T, ?>> result = new ArrayList<>();

        result.add(new NodeWithParent<>(
                node,  OptionalInt.empty(), Optional.empty()
        ));
        collectHelper(node, result);
        return List.copyOf(result);
    }

    @SuppressWarnings("unchecked")
    static <T, I> void collectHelper(
            MutableNode<T, ?, ?, ?, ?> node, List<NodeWithParent<T, ?>> result
    ) {
        if (node instanceof MutableNonTerminal<?, ?, ?> nonTerminal) {
            MutableNonTerminal<T, I, ?> actualNonTerminal =
                    (MutableNonTerminal<T, I, ?>) nonTerminal;

            int i = 0;
            for (MutableNode<T, ?, I, ?, ?> child : actualNonTerminal.children()) {
                result.add(new NodeWithParent<>(
                        child,
                        OptionalInt.of(i),
                        Optional.of(actualNonTerminal)
                ));

                i += 1;
                collectHelper(child, result);
            }
        }
    }

    boolean isRoot() {
        return parent.isEmpty();
    }
}
