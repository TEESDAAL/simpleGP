package gp.core.fitness;

import java.util.Comparator;

/**
 * An extension of Comparator, to use the {@link Comparison} objects.
 * @param <I> The type of individual being compared.
 */
public interface Comparer<I> extends Comparator<I> {
    static <I> Comparer<I> of(Comparator<I> ordering) {
        return (i1, i2) -> Comparison.of(ordering, i1, i2);
    }

    /**
     * Compares two fitness to find which is better.
     *
     * @param f1 The first fitness.
     * @param f2 The second fitness.
     * @return The comparison result, comparing the two individuals.
     * if f1 is better than f2 returns Comparison.BETTER.
     */
    Comparison compareWith(I f1, I f2);

    default int compare(I f1, I f2) {
        return this.compareWith(f1, f2).ord();
    }
}
