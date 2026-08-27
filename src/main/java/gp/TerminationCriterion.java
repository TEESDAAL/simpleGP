package gp;

/**
 * Interface for defining termination criteria.
 * @param <T> The value type to check
 */
public interface TerminationCriterion<T> {
    /**
     * Checks if the process should terminate.
     * @param iteration The current iteration number, indexed from 0.
     * @param value The current value
     * @return true if should terminate
     */
    boolean shouldTerminate(int iteration, T value);

    /**
     * Terminate if either this or other criterion are true.
     * @param other The other criterion to evaluate.
     * @return A new Termination criterion that returns true if either are true.
     */
    default TerminationCriterion<T> or(TerminationCriterion<T> other) {
        return (i, t) -> this.shouldTerminate(i, t) || other.shouldTerminate(i, t);
    }

    /**
     * Terminate if both this and the other criterion are true.
     * @param other The other criterion to evaluate.
     * @return A new Termination criterion that returns true if both are true.
     */
    default TerminationCriterion<T> and(TerminationCriterion<T> other) {
        return (i, t) -> this.shouldTerminate(i, t) && other.shouldTerminate(i, t);
    }


    /**
     * Creates a termination criterion based on iteration count.
     * @param <T> The value type
     * @param n The number of iterations
     * @return A termination criterion
     */
    static <T> TerminationCriterion<T> nIters(int n) {
        return (i, _) -> i >= n;
    }
}
