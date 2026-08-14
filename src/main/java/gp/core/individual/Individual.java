package gp.core.individual;

import java.util.List;

/**
 * Interface representing a GP individual.
 * @param <T> The terminal type
 * @param <R> The return type
 */
public interface Individual<T, R> {
    /**
     * Evaluates this individual on the given terminals.
     * @param terminals The terminal inputs
     * @return The output value
     */
    R evaluate(T terminals);

    /**
     * Evaluate this individual over all the given terminals
     *  producing an output R for each one.
     * Depending on the implementation, calling this may be more performant
     *  than calling .evaluate multiple times.
     * @param terminals The List of terminals to evaluate on.
     * @return An output list where the `i`th element of the output corresponds
     *  to the `i`th element in terminals
     */
    default List<R> evaluateAll(List<T> terminals) {
        return terminals.stream().map(this::evaluate).toList();
    }
}
