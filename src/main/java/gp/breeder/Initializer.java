package gp.breeder;

import gp.Population;


public interface Initializer<I> {
    Population<I> initialize();
}

