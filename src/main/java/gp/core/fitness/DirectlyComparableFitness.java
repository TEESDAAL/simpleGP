package gp.core.fitness;

import gp.Population;

/**
 * An interface for a type of fitness that is able to be compared
 * without looking at the context of the whole population.
 * @param <Self> The fitness type
 */
public interface DirectlyComparableFitness<
        Self extends DirectlyComparableFitness<Self>
> extends Fitness<Self>, Comparer<Self>, Comparable<Self> {

    /**
     * Determine if this fitness is better, worse or equal to this individual.
     * If this is considered better than other Comparison.BETTER is returned.
     * @param other The other individual to compare against.
     * @return the comparison of this to other.
     */
    Comparison compare(Self other);

    @Override
    default Comparer<Self> fromPopulation(Population<Self> other) {
        return this;
    }

    @Override
    default Comparison compareWith(Self f1, Self f2) {
        return f1.compare(f2);
    }

    @Override
    default int compareTo(Self o) {
        return this.compare(o).ord();
    }
}
