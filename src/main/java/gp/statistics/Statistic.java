package gp.statistics;

import gp.Population;

public interface Statistic<I> extends SideEffect<Population<I>> {
    @Override
    default Population<I> sideEffect(Population<I> population) {
        log(population);
        return population;
    }


    void log(Population<I> population);
}