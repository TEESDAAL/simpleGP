package gp;

public interface TerminationCriterion<T> {
    boolean shouldTerminate(Integer iteration, T value);
    static <T> TerminationCriterion<T> nIters(int n) {
        return (i, ignored) -> i >= n;
    }
}
