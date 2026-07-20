package gp.impl.individual.multitree.impls;

import gp.impl.individual.SingleTreeIndividual;
import gp.impl.individual.multitree.End;
import gp.impl.individual.multitree.Get;
import gp.impl.individual.multitree.MultiTree;

/**
 * The interface for a fully typed 8 tree multiGPTree.
 * @param <T1> the terminal type for the first tree
 * @param <R1> the return type for the first tree
 * @param <T2> The terminal type for second tree.
 * @param <R2> The return type for the second tree.
 * @param <T3> The terminal type for third tree.
 * @param <R3> The return type for the third tree.
 * @param <T4> The terminal type for fourth tree.
 * @param <R4> The return type for the fourth tree.
 * @param <T5> The terminal type for fifth tree.
 * @param <R5> The return type for the fifth tree.
 * @param <T6> The terminal type for sixth tree.
 * @param <R6> The return type for the sixth tree.
 * @param <T7> The terminal type for seventh tree.
 * @param <R7> The return type for the seventh tree.
 * @param <T8> The terminal type for eighth tree.
 * @param <R8> The return type for the eighth tree.
 * @param <Self>
 */
public interface EightTreeIndividual<
    T1, R1,
    T2, R2,
    T3, R3,
    T4, R4,
    T5, R5,
    T6, R6,
    T7, R7,
    T8, R8,
    Self extends EightTreeIndividual<T1, R1, T2, R2, T3, R3, T4, R4, T5, R5, T6, R6, T7, R7, T8, R8, Self>
    > extends MultiTree<
    T1, R1,
    MultiTree<T2, R2,
        MultiTree<T3, R3,
            MultiTree<T4, R4,
                MultiTree<T5, R5,
                    MultiTree<T6, R6,
                        MultiTree<T7, R7,
                            MultiTree<T8, R8, End, ?>,
                            ?>,
                        ?>,
                    ?>,
                ?>,
            ?>,
        ?>,
    EightTreeIndividual<T1, R1, T2, R2, T3, R3, T4, R4, T5, R5, T6, R6, T7, R7, T8, R8, Self>
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

    default SingleTreeIndividual<T4, R4> fourth() {
        return Get.fourth(this);
    }

    default SingleTreeIndividual<T5, R5> fifth() {
        return Get.fifth(this);
    }

    default SingleTreeIndividual<T6, R6> sixth() {
        return Get.sixth(this);
    }

    default SingleTreeIndividual<T7, R7> seventh() {
        return Get.seventh(this);
    }

    default SingleTreeIndividual<T8, R8> eighth() {
        return Get.eighth(this);
    }
}


