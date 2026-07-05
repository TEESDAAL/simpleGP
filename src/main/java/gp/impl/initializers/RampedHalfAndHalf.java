package gp.impl.initializers;

import gp.Population;
import gp.core.initializers.*;
import gp.impl.individual.SingleTreeIndividual;
import utils.random.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class RampedHalfAndHalf<Terminal, R> implements Initializer<
    SingleTreeIndividual<Terminal, R>
> {
    final int maxDepth;
    final IntFunction<IndividualInitializer<SingleTreeIndividual<Terminal, R>>> grow;
    final IntFunction<IndividualInitializer<SingleTreeIndividual<Terminal, R>>> full;
    private final int populationSize;

    public RampedHalfAndHalf(
        int maxDepth,
        RandomSource random,
        List<TypedTerminal<Terminal, ?>> terminals,
        List<TypedNonTerminal<?, ?>> nonTerminals,
        int populationSize,
        int maxTries,
        Class<R> returnType
    ) {
        if (maxDepth < 2) {
            throw new IllegalArgumentException("maxDepth must be at least 2");
        }
        this.populationSize = populationSize;
        this.maxDepth = maxDepth;
        this.full = depth -> Initializers.full(
            random,
            terminals,
            nonTerminals,
            populationSize,
            maxTries,
            depth,
            returnType
        );
        this.grow = depth -> Initializers.grow(
            random,
            terminals,
            nonTerminals,
            populationSize,
            maxTries,
            depth,
            returnType
        );
    }

    @Override
    public Population<SingleTreeIndividual<Terminal, R>> initialize() {
        List<SingleTreeIndividual<Terminal, R>> pop = new ArrayList<>(populationSize);
        while (pop.size() < populationSize) {
            for (int i = 2; i <= maxDepth; i++) {
                pop.add(
                    full.apply(i).createIndividual()
                );
                if (pop.size() == populationSize) { break; }
                pop.add(
                    grow.apply(i).createIndividual()
                );
            }
        }

        return Population.of(pop);
    }
}