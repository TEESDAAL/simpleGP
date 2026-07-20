package gp.impl.individual.multitree.impls;

import gp.impl.individual.SingleTreeIndividual;
import gp.impl.individual.multitree.End;
import gp.impl.individual.multitree.Get;
import gp.impl.individual.multitree.MultiTree;

/**
 * The interface for a three tree multi-tree individual.
 * @param <T1> The terminal type for first tree.
 * @param <R1> The return type for the first tree.
 * @param <T2> The terminal type for second tree.
 * @param <R2> The return type for the second tree.
 * @param <T3> The terminal type for third tree.
 * @param <R3> The return type for the third tree.
 * @param <Self> The self type for this multi-tree GP individual
 */
public interface ThreeTreeIndividual<
    T1, R1,
    T2, R2,
    T3, R3,
    Self extends ThreeTreeIndividual<T1, R1, T2, R2, T3, R3, Self>
> extends MultiTree<
    T1, R1,
    MultiTree<T2, R2, MultiTree<T3, R3, End, ?>, ?>,
    ThreeTreeIndividual<T1, R1, T2, R2, T3, R3, Self>
> {
    default SingleTreeIndividual<T1, R1> first() {
        return Get.first(this);
    }

    default SingleTreeIndividual<T2, R2> second() {
        return Get.second(this);
    }

    default SingleTreeIndividual<T3, R3> third() {
        return Get.third(this);
    }
}


