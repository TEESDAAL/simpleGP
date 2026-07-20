package gp.impl.individual.multitree;


import gp.impl.individual.SingleTreeIndividual;
import gp.impl.individual.multitree.impls.SevenTreeIndividual;
import gp.impl.individual.multitree.impls.TwoTreeIndividual;

import java.util.List;

public non-sealed interface MultiTree<
    T, R,
    Tail extends Next,
    Self extends MultiTree<T, R, Tail, Self>
> extends Next {
    /**
     * Produce a starting MultiTree with 1 tree.
     * @param tree The starting tree.
     * @return A MultiTree with 1 tree.
     * @param <T> The input type of the tree.
     * @param <R> The output type of the tree.
     */
    static <T, R> MultiTree<T, R, End, ?> of(final SingleTreeIndividual<T, R> tree) {
        return new MultiTreeImpl<>(tree, End.INSTANCE);
    }

    /**
     * Produce a starting MultiTree with 1 tree.
     * @param tree The starting tree.
     * @return A MultiTree with 1 tree.
     * @param <T> The input type of the tree.
     * @param <R> The output type of the tree.
     */
    static <T, R, N extends MultiTree<?, ?, ?, ?>> MultiTree<T, R, N, ?> of(
        final SingleTreeIndividual<T, R> tree, final N next
    ) {
        return new MultiTreeImpl<>(tree, next);
    }

    /**
     * A way of constructing self again from a multiTree.
     * Needed to allow general MultiTreeGP operators to reconstruct self.
     * @param multiTree The multi-tree to construct from.
     * @return A new instance of the self type.
     */
    Self constructor(MultiTree<T, R, Tail, ?> multiTree);

    /**
     * Get the multiTree representation of this individual.
     * @return the multiTree representation of this individual.
     */
    MultiTree<T, R, Tail, ?> multiTree();

    default SingleTreeIndividual<T, R> tree() {
        return multiTree().tree();
    }
    default Tail next() {
        return multiTree().next();
    }

    default <T1, R1> MultiTree<T1, R1, MultiTree<T, R, Tail, ?>, ?> addFirst(
        final SingleTreeIndividual<T1, R1> tree
    ) {
        return new MultiTreeImpl<>(tree, this);
    }
}

record MyExample<T1, T2>(
    SingleTreeIndividual<T1, Double> tree1,
    SingleTreeIndividual<T2, Double> tree2
) implements TwoTreeIndividual<T1, Double, T2, Double, MyExample<T1, T2>> {

    @Override
    public TwoTreeIndividual<T1, Double, T2, Double, MyExample<T1, T2>> constructor(
        MultiTree<T1, Double, MultiTree<T2, Double, End, ?>, ?> multiTree
    ) {
        return new MyExample<>(Get.first(multiTree), Get.second(multiTree));
    }

    @Override
    public MultiTree<T1, Double, MultiTree<T2, Double, End, ?>, ?> multiTree() {
        return MultiTree.of(tree2).addFirst(tree1);
    }
}



record BlueExample<T1, T2, T3, T4, T5, T6, T7>(
    SingleTreeIndividual<T1, List<Double>> d1Policy,
    SingleTreeIndividual<T2, List<Double>> d2Policy,
    SingleTreeIndividual<T3, List<Double>> d3Policy,
    SingleTreeIndividual<T4, List<Double>> d4Policy,
    SingleTreeIndividual<T5, List<Double>> d5Policy,
    SingleTreeIndividual<T6, List<Double>> d6Policy,
    SingleTreeIndividual<T7, List<Double>> d7Policy
) implements SevenTreeIndividual<
    T1, List<Double>,
    T2, List<Double>,
    T3, List<Double>,
    T4, List<Double>,
    T5, List<Double>,
    T6, List<Double>,
    T7, List<Double>,
    BlueExample<T1, T2, T3, T4, T5, T6, T7>
> {

    @Override
    public SevenTreeIndividual<
        T1, List<Double>,
        T2, List<Double>,
        T3, List<Double>,
        T4, List<Double>,
        T5, List<Double>,
        T6, List<Double>,
        T7, List<Double>,
        BlueExample<T1, T2, T3, T4, T5, T6, T7>
    > constructor(MultiTree<
        T1, List<Double>, MultiTree<
        T2, List<Double>, MultiTree<
        T3, List<Double>, MultiTree<
        T4, List<Double>, MultiTree<
        T5, List<Double>, MultiTree<
        T6, List<Double>, MultiTree<
        T7, List<Double>, End,
        ?>, ?>, ?>, ?>, ?>, ?>, ?> multiTree
    ) {
        return new BlueExample<>(
            Get.first(multiTree),
            Get.second(multiTree),
            Get.third(multiTree),
            Get.fourth(multiTree),
            Get.fifth(multiTree),
            Get.sixth(multiTree),
            Get.seventh(multiTree)
        );
    }

    @Override
    public MultiTree<
        T1, List<Double>, MultiTree<
        T2, List<Double>, MultiTree<
        T3, List<Double>, MultiTree<
        T4, List<Double>, MultiTree<
        T5, List<Double>, MultiTree<
        T6, List<Double>, MultiTree<
        T7, List<Double>, End,
    ?>, ?>, ?>, ?>, ?>, ?>, ?> multiTree() {
        return MultiTree.of(d7Policy)
            .addFirst(d6Policy)
            .addFirst(d5Policy)
            .addFirst(d4Policy)
            .addFirst(d3Policy)
            .addFirst(d2Policy)
            .addFirst(d1Policy);
    }
}