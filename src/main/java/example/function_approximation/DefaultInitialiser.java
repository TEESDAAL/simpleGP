package example.function_approximation;

import example.function_approximation.parameters.DoubleNonTerminals;
import gp.Population;
import gp.core.initializers.Initialiser;
import gp.core.initializers.TypedNonTerminal;
import gp.core.initializers.TypedTerminal;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.initializers.Initializers;
import utils.Pair;
import utils.random.RandomSource;

import java.util.Collections;
import java.util.List;

public class DefaultInitialiser implements Initialiser<SingleTreeIndividual<Pair<Double, Double>, Double>> {
    final Initialiser<SingleTreeIndividual<Pair<Double, Double>, Double>> initialiser;
    final int maxDepth = 7;
    int startingPopulationSize = 1000;
    protected final List<TypedTerminal<Pair<Double, Double>, ?>> terminals = List.of(
        TypedTerminal.nonCached("x", Pair::first, Double.class),
        TypedTerminal.nonCached("y", Pair::second, Double.class)
    );
    protected final List<TypedNonTerminal<?, ?>> nonTerminals = Collections.unmodifiableList(DoubleNonTerminals.all());
    
    public DefaultInitialiser(RandomSource random) {
        this.initialiser = Initializers.rampedHalfAndHalf(
            maxDepth, 
            random,
            terminals, 
            nonTerminals,
            startingPopulationSize,
            100,
            Double.class
        );
    }

    @Override
    public Population<SingleTreeIndividual<Pair<Double, Double>, Double>> initialize() {
        return this.initialiser.initialize();
    }
}

