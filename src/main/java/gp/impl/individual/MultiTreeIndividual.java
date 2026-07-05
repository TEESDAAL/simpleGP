package gp.impl.individual;


import gp.core.individual.Individual;

import java.util.List;
import java.util.function.Function;

record MultiTree<T, R, Tail>(
    SingleTreeIndividual<T, R> tree,
    Tail tail
) {
    MultiTree {
        if (!(tail instanceof MultiTree || tail == null)) {
            throw new IllegalArgumentException("Tail must be a MultiTree or null");
        }
    }

    public <T1, R1> MultiTree<T1, R1, MultiTree<T, R, Tail>> add(
        SingleTreeIndividual<T1, R1> tree
    ) {
        return new MultiTree<>(tree, this);
    }

    public static <T, R> MultiTree<T, R, Void> of(
        SingleTreeIndividual<T, R> tree
    ) {
        return new MultiTree<>(tree, null);
    }
    public static <T, R, N extends MultiTree<?, ?, ?>> MultiTree<T, R, N> of(
        SingleTreeIndividual<T, R> tree, N next
    ) {
        return new MultiTree<>(tree, next);
    }

}


record MultiTreeIndividual<T, IR, R>(
    List<SingleTree<T, ?, IR>> individuals,
    Function<List<IR>, R> combiner
) implements Individual<T, R> {

    @Override
    public R evaluate(T terminals) {
        return combiner.apply(
            individuals.stream()
                .map(tree -> tree.evaluate(terminals))
                .toList()
        );
    }
}

record SingleTree<T, T1, R>(
    SingleTreeIndividual<T1, R> tree,
    Function<T, T1> mapper
) implements Individual<T, R> {

    @Override
    public R evaluate(T terminals) {
        return this.tree.evaluate(mapper.apply(terminals));
    }
}

