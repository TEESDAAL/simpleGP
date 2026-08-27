package gp.core.fitness;


import gp.Population;

/**
 * Interface for individual fitness.
 * @param <Self> The type of fitness being compared
 */
public interface Fitness<Self extends Fitness<Self>> {
    Comparer<Self> fromPopulation(Population<Self> other);
}

interface DirectlyComparableFitness<Self extends DirectlyComparableFitness<Self>>
    extends Fitness<Self>, Comparer<Self>, Comparable<Self> {

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
