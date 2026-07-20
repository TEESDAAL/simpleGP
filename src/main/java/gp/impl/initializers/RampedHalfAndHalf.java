package gp.impl.initializers;

import gp.Population;
import gp.core.initializers.IndividualInitialiser;
import gp.core.initializers.Initialiser;
import gp.core.initializers.TypedNonTerminal;
import gp.core.initializers.TypedTerminal;
import gp.impl.individual.SingleTreeIndividual;
import utils.random.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

/**
 * An initializer that creates a population of individuals using
 * the Ramped Half-and-Half method.
 * This method ranges between 2..=maxDepth, and creates half of the individuals
 *  using the full method and half using the grow method.
 * @param <Terminal> The terminal type of the tree.
 * @param <R> The desired return type of the tree.
 */
public class RampedHalfAndHalf<Terminal, R> implements Initialiser<
    SingleTreeIndividual<Terminal, R>
> {
    private final int maxDepth;
    private final IntFunction<
        IndividualInitialiser<SingleTreeIndividual<Terminal, R>>
    > grow;

    private final IntFunction<
        IndividualInitialiser<SingleTreeIndividual<Terminal, R>>
    > full;

    private final int populationSize;

    /**
     * Create a RampedHalfAndHalf initializer.
     * @param maxDepth The final maxDepth of the tree.
     * @param random The source of randomness.
     * @param terminals The set of terminal to sample from.
     * @param nonTerminals The set of non-terminals to sample from.
     * @param populationSize The desired population size.
     * @param maxTries the number of times to try re-creating an individual.
     * @param returnType The desired return type of all the trees.
     */
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
        final List<SingleTreeIndividual<Terminal, R>> pop = new ArrayList<>(
            populationSize
        );

        while (pop.size() < populationSize) {
            for (int i = 2; i <= maxDepth; i++) {
                if (pop.size() == populationSize) { break; }
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
