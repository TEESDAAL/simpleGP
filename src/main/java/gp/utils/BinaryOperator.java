package gp.utils;

import java.util.List;

/**
 * Binary operator interface that applies an operation to exactly two parents.
 * @param <I> The input type
 * @param <O> The output type
 */
public interface BinaryOperator<I, O> extends Operator<I, O> {
    /**
        * Produces output from a list of parents by delegating
        * to the two-parent method.
     * @param parents A list containing exactly two parents
     * @return The produced output
     */
    @Override
    default O produce(List<I> parents) {
        assert parents.size() == 2;
        return produce(parents.get(0), parents.get(1));
    }

    /**
     * @return The number of operands, always 2 for binary operators.
     */
    @Override
    default Integer size() {
        return 2;
    }

    /**
     * Produces an output from two parents.
     * @param parent1 The first parent
     * @param parent2 The second parent
     * @return The produced output
     */
    O produce(I parent1, I parent2);
}
