package gp.fitness;

/**
 * Interface for individual fitness.
 * @param <Self> The type of fitness being compared
 */
public interface Fitness<Self> extends Comparable<Self> {
    @Override
    default int compareTo(final Self other) {
        return this.compareWith(other).ord();
    }

    /**
     * Compares this fitness to another and returns a Comparison result.
     * @param other The other fitness
     * @return The comparison result
     */
    Comparison compareWith(Self other);
}
