package gp.utils;

import java.util.List;

public interface UnaryOperator<I, O> extends Operator<I, O> {
    @Override
    default O produce(List<I> parents) {
        assert parents.size() == 1;
        return produce(parents.getFirst());
    }

    O produce(I parent);

    @Override
    default Integer size() {
        return 1;
    }
}

