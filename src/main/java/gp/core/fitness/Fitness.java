package gp.core.fitness;


import gp.Population;

/**
 * Interface for individual fitness.
 *
 * @param <Self> The type of fitness being compared
 */
public interface Fitness<Self extends Fitness<Self>> {
    Comparer<Self> fromPopulation(Population<Self> other);
}
