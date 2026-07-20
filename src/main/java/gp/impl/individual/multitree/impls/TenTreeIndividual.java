package gp.impl.individual.multitree.impls;

import gp.impl.individual.SingleTreeIndividual;
import gp.impl.individual.multitree.End;
import gp.impl.individual.multitree.Get;
import gp.impl.individual.multitree.MultiTree;

/**
 * The interface for a 10 multi-tree GP individual.
 * @param <T1> The terminal type for first tree.
 * @param <R1> The return type for the first tree.
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
 * @param <T9> The terminal type for nineth tree.
 * @param <R9> The return type for the nineth tree.
 * @param <T10> The terminal type for tenth tree.
 * @param <R10> The return type for the tenth tree.
 * @param <Self> The self type for this multi-tree GP individual
 */
public interface TenTreeIndividual<
    T1, R1,
    T2, R2,
    T3, R3,
    T4, R4,
    T5, R5,
    T6, R6,
    T7, R7,
    T8, R8,
    T9, R9,
    T10, R10,
    Self extends TenTreeIndividual<
        T1, R1,
        T2, R2,
        T3, R3,
        T4, R4,
        T5, R5,
        T6, R6,
        T7, R7,
        T8, R8,
        T9, R9,
        T10, R10,
        Self
        >
    > extends MultiTree<
    T1, R1,
    MultiTree<T2, R2,
        MultiTree<T3, R3,
            MultiTree<T4, R4,
                MultiTree<T5, R5,
                    MultiTree<T6, R6,
                        MultiTree<T7, R7,
                            MultiTree<T8, R8,
                                MultiTree<T9, R9,
                                    MultiTree<T10, R10, End, ?>,
                                    ?>,
                                ?>,
                            ?>,
                        ?>,
                    ?>,
                ?>,
            ?>,
        ?>,
    TenTreeIndividual<
        T1, R1,
        T2, R2,
        T3, R3,
        T4, R4,
        T5, R5,
        T6, R6,
        T7, R7,
        T8, R8,
        T9, R9,
        T10, R10,
        Self
        >
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

    default SingleTreeIndividual<T9, R9> ninth() {
        return Get.ninth(this);
    }

    default SingleTreeIndividual<T10, R10> tenth() {
        return Get.tenth(this);
    }
}
