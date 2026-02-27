package gp.utils;

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

    /**
     * Produces output from a single parent.
     * @param parent The parent to operate on
     * @return The produced output
     */
    O produce(I parent);

    @Override
    default Integer size() {
        return 1;
    }
}

