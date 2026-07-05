package gp.core.fitness;

/**
 * Interface for objects that can be compared to others of the same type.
 * You should not consider this as equivalent to Java's Comparable,
 * As it doesn't guarantee that the comparison is
 * reflexive, symmetric, and transitive properties of a total order.
 * @param <Self> The type of object being compared
 */
public interface CanCompare<Self extends CanCompare<Self>> {
    /**
     * Compares this object to another and returns a Comparison result.
     * @param other The other object
     * @return The comparison result
     */
    Comparison compareWith(Self other);
}
