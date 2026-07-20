package gp.impl.individual.multitree;

import gp.impl.individual.SingleTreeIndividual;

import java.util.List;

public interface MultiTreeIndividual<Self extends MultiTreeIndividual<Self>> {
    default int numTrees() { return trees().size(); }
    Self constructor(List<SingleTreeIndividual<?, ?>> trees);
    List<SingleTreeIndividual<?, ?>> trees();
}

interface BiTreeIndividual<
    T1, R1,
    T2, R2,
    Self extends BiTreeIndividual<T1,R1, T2,R2,Self>
    > extends MultiTreeIndividual<Self> {
    SingleTreeIndividual<T1, R1> first();
    SingleTreeIndividual<T2, R2> second();
}


interface TriTreeIndividual<
    T1, R1,
    T2, R2,
    T3, R3,
    Self extends TriTreeIndividual<T1,R1, T2,R2, T3,R3,Self>
    > extends MultiTreeIndividual<Self> {
    SingleTreeIndividual<T1, R1> first();
    SingleTreeIndividual<T2, R2> second();
    SingleTreeIndividual<T3, R3> third();
}



