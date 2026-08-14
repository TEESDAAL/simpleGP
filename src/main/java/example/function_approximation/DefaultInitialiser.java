package example.function_approximation;

import gp.Population;
import gp.core.initializer.Initialiser;
import gp.core.initializer.PrimitiveSet;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.initializers.Initializers;
import utils.random.RandomSource;

public class DefaultInitialiser<T, R> implements Initialiser<SingleTreeIndividual<T, R>> {
    final Initialiser<SingleTreeIndividual<T, R>> initialiser;
    final int maxDepth = 7;
    int startingPopulationSize = 1000;
    protected final PrimitiveSet<T> primitiveSet;

    public DefaultInitialiser(RandomSource random, PrimitiveSet<T> primitiveSet, Class<R> returnType) {
        this.initialiser = Initializers.rampedHalfAndHalf(
            maxDepth, 
            random,
            primitiveSet,
            startingPopulationSize,
            100,
            returnType
        );
        this.primitiveSet = primitiveSet;
    }

    @Override
    public Population<SingleTreeIndividual<T, R>> initialize() {
        return this.initialiser.initialize();
    }
}

