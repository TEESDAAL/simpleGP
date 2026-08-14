package example.function_approximation;

import gp.Population;
import gp.core.initializer.Initialiser;
import gp.core.initializer.PrimitiveSet;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.initializers.Initializers;
import utils.Pair;
import utils.random.RandomSource;

public class DefaultInitialiser implements Initialiser<SingleTreeIndividual<Pair<Double, Double>, Double>> {
    final Initialiser<SingleTreeIndividual<Pair<Double, Double>, Double>> initialiser;
    final int maxDepth = 7;
    int startingPopulationSize = 1000;
    protected final PrimitiveSet<Pair<Double, Double>> primitiveSet;

    public DefaultInitialiser(RandomSource random, PrimitiveSet<Pair<Double, Double>> primitiveSet) {
        this.initialiser = Initializers.rampedHalfAndHalf(
            maxDepth, 
            random,
            primitiveSet,
            startingPopulationSize,
            100,
            Double.class
        );
        this.primitiveSet = primitiveSet;
    }

    @Override
    public Population<SingleTreeIndividual<Pair<Double, Double>, Double>> initialize() {
        return this.initialiser.initialize();
    }
}

