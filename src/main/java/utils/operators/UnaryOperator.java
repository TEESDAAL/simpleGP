package utils.operators;

import java.util.List;

/**
 * Unary operator interface that applies an operation to a single parent.
 * @param <I> The input type
 * @param <O> The output type
 */
public interface UnaryOperator<I, O> extends Operator<I, O> {
    @Override
    default O produce(List<I> parents) {
        assert parents.size() == 1;
        return produce(parents.getFirst());
    }

    @Override
    default O produce(I[] parents) {
        return produce(parents[0]);
    }

    /**
     * Produces output from a single parent.
     * @param parent The parent to operate on
     * @return The produced output
     */
    O produce(I parent);

    @Override
    default Integer arity() {
        return 1;
    }

    @Override
    default UnaryOperator<I, O> cached() {
        return CacherCache.unaryCached(this);
    }
}

