package gp;

/**
 * Interface for defining termination criteria.
 * @param <T> The value type to check
 */
public interface TerminationCriterion<T> {
    /**
     * Checks if the process should terminate.
     * @param iteration The current iteration number
     * @param value The current value
     * @return true if should terminate
     */
    boolean shouldTerminate(Integer iteration, T value);

    /**
     * Creates a termination criterion based on iteration count.
     * @param <T> The value type
     * @param n The number of iterations
     * @return A termination criterion
     */
    static <T> TerminationCriterion<T> nIters(int n) {
        return (i, ignored) -> i >= n;
    }
}
