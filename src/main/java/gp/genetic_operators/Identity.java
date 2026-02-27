package gp.genetic_operators;

import gp.utils.UnaryOperator;

/**
 * Identity operator that returns its input unchanged.
 * Used as a preservation operator in genetic programming.
 * @param <T> The type of object
 */
public class Identity<T> implements UnaryOperator<T, T> {
    /**
     * Returns the parent unchanged.
     * @param parent The input
     * @return The same input
     */
    @Override
    public T produce(final T parent) {
        return parent;
    }
}
