package gp.breeder;

import gp.Population;

/**
 * Interface for breeding populations.
 * @param <Parent> The parent individual type
 * @param <Child> The child individual type
 */
public interface Breeder<Parent, Child> {
    /**
     * Breeds a new population from parents.
     * @param population The parent population
     * @return The child population
     */
    Population<Child> breed(Population<Parent> population);
}


