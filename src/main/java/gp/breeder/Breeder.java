package gp.breeder;

import gp.Population;

public interface Breeder<Parent, Child> {
    Population<Child> breed(Population<Parent> population);
}


