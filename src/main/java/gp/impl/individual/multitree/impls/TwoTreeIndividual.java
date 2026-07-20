package gp.impl.individual.multitree.impls;

import gp.impl.individual.SingleTreeIndividual;
import gp.impl.individual.multitree.End;
import gp.impl.individual.multitree.Get;
import gp.impl.individual.multitree.MultiTree;

/**
 * The interface for a two-tree multiTree individual.
 * @param <T1> The terminal type for first tree.
 * @param <R1> The return type for the first tree.
 * @param <T2> The terminal type for second tree.
 * @param <R2> The return type for the second tree.
 * @param <Self> The self type for this multi-tree GP individual
 */
public interface TwoTreeIndividual<
    T1, R1,
    T2, R2,
    Self extends TwoTreeIndividual<T1, R1, T2, R2, Self>
> extends MultiTree<
    T1, R1, MultiTree<T2, R2, End, ?>,
    TwoTreeIndividual<T1, R1, T2, R2, Self>
> {
    default SingleTreeIndividual<T1, R1> first() {
        return Get.first(this);
    }
    default SingleTreeIndividual<T2, R2> second() {
        return Get.second(this);
    }
}
