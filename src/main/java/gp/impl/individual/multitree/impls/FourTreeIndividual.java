package gp.impl.individual.multitree.impls;

import gp.impl.individual.SingleTreeIndividual;
import gp.impl.individual.multitree.End;
import gp.impl.individual.multitree.Get;
import gp.impl.individual.multitree.MultiTree;

/**
 * The interface for a four tree multiTreeGP individual
 * @param <T1> The terminal type for first tree.
 * @param <R1> The return type for the first tree.
 * @param <T2> The terminal type for second tree.
 * @param <R2> The return type for the second tree.
 * @param <T3> The terminal type for third tree.
 * @param <R3> The return type for the third tree.
 * @param <T4> The terminal type for fourth tree.
 * @param <R4> The return type for the fourth tree.
 * @param <Self> The self type for this multi-tree GP individual
 */
public interface FourTreeIndividual<
    T1, R1,
    T2, R2,
    T3, R3,
    T4, R4,
    Self extends FourTreeIndividual<T1, R1, T2, R2, T3, R3, T4, R4, Self>
> extends MultiTree<
    T1, R1,
    MultiTree<T2, R2, MultiTree<T3, R3, MultiTree<T4, R4, End, ?>, ?>, ?>,
    Self
> {
    @Override
    default Self constructor(MultiTree<T1, R1, MultiTree<T2, R2, MultiTree<T3, R3, MultiTree<T4, R4, End, ?>, ?>, ?>, ?> multiTree) {
        final SingleTreeIndividual<T1, R1> first = Get.first(multiTree);
        final SingleTreeIndividual<T2, R2> second = Get.second(multiTree);
        final SingleTreeIndividual<T3, R3> third = Get.third(multiTree);
        final SingleTreeIndividual<T4, R4> fourth = Get.fourth(multiTree);
        return constructor(first, second, third, fourth);
    }

    Self constructor(SingleTreeIndividual<T1,R1> first, SingleTreeIndividual<T2,R2> second, SingleTreeIndividual<T3,R3> third, SingleTreeIndividual<T4,R4> fourth);

    SingleTreeIndividual<T1, R1> first();

    SingleTreeIndividual<T2, R2> second();

    SingleTreeIndividual<T3, R3> third();

    SingleTreeIndividual<T4, R4> fourth();

    @Override
    default MultiTree<T1, R1, MultiTree<T2, R2, MultiTree<T3, R3, MultiTree<T4, R4, End, ?>, ?>, ?>, ?> multiTree() {
        return MultiTree.of(fourth()).addFirst(third()).addFirst(second()).addFirst(first());
    }
}

record FourTreeExample(
        SingleTreeIndividual<Double, Double> first,
        SingleTreeIndividual<Double, Double> second,
        SingleTreeIndividual<Double, Double> third,
        SingleTreeIndividual<Double, Double> fourth
) implements FourTreeIndividual<Double, Double, Double, Double, Double, Double, Double, Double, FourTreeExample> {

    @Override
    public FourTreeExample constructor(
            SingleTreeIndividual<Double, Double> first,
            SingleTreeIndividual<Double, Double> second,
            SingleTreeIndividual<Double, Double> third,
            SingleTreeIndividual<Double, Double> fourth
    ) {
        return new FourTreeExample(first, second, third, fourth);
    }
}