package gp.impl.individual.multitree;

import gp.impl.individual.SingleTreeIndividual;

public record MultiTreeImpl<T, R, Tail extends Next>(
    SingleTreeIndividual<T, R> tree, Tail next
) implements MultiTree<T, R, Tail, MultiTreeImpl<T, R, Tail>> {
    @Override
    public MultiTreeImpl<T, R, Tail> constructor(
        final MultiTree<T, R, Tail, ?> multiTree
    ) {
        return new MultiTreeImpl<>(multiTree.tree(), multiTree.next());
    }

    @Override
    public MultiTree<T, R, Tail, ?> multiTree() {
        return this;
    }
}
