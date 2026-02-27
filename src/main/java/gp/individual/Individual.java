package gp.individual;

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
}
