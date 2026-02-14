package gp.utils;

import gp.Population;
import gp.breeder.Initializer;
import gp.breeder.Parallelizeable;

import java.util.stream.IntStream;

public interface IndividualInitializer<I> extends Initializer<I>, Parallelizeable {
    @Override
    default Population<I> initialize() {
//        return Population.of(
//                this.generateN(this::createIndividual, this.populationSize())
//                        .toList()
//        );
        return Population.of(
                IntStream.range(0, this.populationSize())
                        .mapToObj(ignored -> this.createIndividual())
                        .toList()
        );
    }
    I createIndividual();
    int populationSize();
}


