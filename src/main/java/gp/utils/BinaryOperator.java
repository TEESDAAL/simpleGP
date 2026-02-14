package gp.utils;

import java.util.List;

public interface BinaryOperator<I, O> extends Operator<I, O> {
    @Override
    default O produce(List<I> parents) {
        assert parents.size() == 2;
        return produce(parents.get(0), parents.get(1));
    }

    @Override
    default Integer size() {
        return 2;
    }

    O produce(I parent1, I parent2);
}
