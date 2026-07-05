package gp.core.fitness;


/**
 * Interface for individual fitness.
 * @param <Self> The type of fitness being compared
 */
public interface Fitness<Self extends Fitness<Self>> extends CanCompare<Self> {
    /**
     * Compares this fitness to another and returns a Comparison result.
     * @param other The other fitness
     * @return The comparison result
     */
    Comparison compareWith(Self other);
}
