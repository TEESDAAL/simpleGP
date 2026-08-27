package gp.impl.initializers;

import gp.Population;
import gp.core.initializer.IndividualInitialiser;
import gp.core.initializer.Initialiser;
import gp.core.initializer.PrimitiveSet;
import gp.impl.individual.SingleTreeIndividual;
import utils.IndSet;
import utils.random.RandomSource;
import utils.stream_utils.Product;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

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

    private final RandomSource random;
    private final PrimitiveSet<Terminal> primitiveSet;
    private final int maxTries;
    private final Class<R> returnType;

    /**
     * Create a RampedHalfAndHalf initializer.
     * @param maxDepth The final maxDepth of the tree.
     * @param random The source of randomness.
     * @param primitiveSet The set of terminals and non-terminals.
     * @param populationSize The desired population size.
     * @param maxTries the number of times to try re-creating an individual.
     * @param returnType The desired return type of all the trees.
     */
    public RampedHalfAndHalf(
            int maxDepth,
            RandomSource random,
            PrimitiveSet<Terminal> primitiveSet,
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
                primitiveSet,
                populationSize,
                maxTries,
                depth,
                returnType
        );
        this.grow = depth -> Initializers.grow(
            random,
            primitiveSet,
            populationSize,
            maxTries,
            depth,
            returnType
        );
        this.random = random;
        this.primitiveSet = primitiveSet;
        this.maxTries = maxTries;
        this.returnType = returnType;
    }

    @Override
    public Population<SingleTreeIndividual<Terminal, R>> initialize() {
        return Product.cycle(
                IntStream.range(2, maxDepth+1).boxed().toList(),
                List.of(full, grow),
                (depth, method) -> method.apply(depth).createIndividual()
        ).limit(populationSize)
                .collect(Population.toPopulation());
    }

    /**
     * Attempt to enforce uniqueness of the individuals created by this initializer.
     * @param numTries The number of times to try creating an individual.
     * @return A new RampedHalfAndHalf initializer that will
     *  attempt to enforce uniqueness.
     */
    public RampedHalfAndHalf<Terminal, R> attemptToEnforceUniqueness(int numTries) {
        return new RampedHalfAndHalf<>(
            maxDepth, random, primitiveSet, populationSize, maxTries, returnType
        ) {
            @Override
            public Population<SingleTreeIndividual<Terminal, R>> initialize() {
                final IndSet<SingleTreeIndividual<Terminal, R>> cache = IndSet.empty();

                return Product.cycle(
                        IntStream.range(2, maxDepth+1).boxed().toList(),
                        List.of(full, grow),
                        (depth, method) -> cache.repeatUntilAbsent(
                            method.apply(depth)::createIndividual, numTries
                        )
                ).limit(populationSize)
                        .collect(Population.toPopulation());
            }
        };
    }
}
