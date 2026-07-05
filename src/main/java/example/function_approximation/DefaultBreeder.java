package example.function_approximation;

import example.function_approximation.parameters.DoubleNonTerminals;
import gp.Population;
import gp.core.breeder.Breeder;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.EvaluatedIndividual;
import gp.core.initializers.TypedNonTerminal;
import gp.core.initializers.TypedTerminal;
import gp.impl.selectors.random.ProbabilisticElement;
import gp.impl.selectors.random.WeightedRandomSampler;
import gp.impl.breeder.NaiveBreeder;
import gp.impl.genetic_operators.CrossOver;
import gp.impl.genetic_operators.Identity;
import gp.impl.genetic_operators.SubtreeMutation;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.selectors.Elitism;
import gp.impl.selectors.TournamentSelection;
import utils.Pair;
import utils.operators.Operator;
import utils.random.RandomSource;

import java.util.Collections;
import java.util.List;

public class DefaultBreeder implements Breeder<EvaluatedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>, SingleTreeIndividual<Pair<Double, Double>, Double>> {
    protected final RandomSource random;
    protected final int populationSize = 1000;
    protected final int tournamentSize = 7;
    protected final int elitismCount = 10;
    protected final List<TypedTerminal<Pair<Double, Double>, ?>> terminals = List.of(
        TypedTerminal.nonCached("x", Pair::first, Double.class),
        TypedTerminal.nonCached("y", Pair::second, Double.class)
    );
    protected final List<TypedNonTerminal<?, ?>> nonTerminals = Collections.unmodifiableList(DoubleNonTerminals.all());
    protected final Breeder<EvaluatedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>, SingleTreeIndividual<Pair<Double, Double>, Double>> breeder;
    protected final List<ProbabilisticElement<Operator<
        SingleTreeIndividual<Pair<Double, Double>, Double>,
        List<SingleTreeIndividual<Pair<Double, Double>, Double>>
    >>> operators;
    public DefaultBreeder(RandomSource random) {
        this.random = random;
        this.operators = ProbabilisticElement.withFallback(
            new Identity<>(),
            List.of(
                ProbabilisticElement.of(0.01, SingleTreeIndividual.operator(new SubtreeMutation<>(
                    random, terminals, nonTerminals, 7, 100
                ))),
                ProbabilisticElement.of(0.65, SingleTreeIndividual.operator(new CrossOver<>(
                    random
                )))
            )
        );
        this.breeder = new NaiveBreeder<
            Pair<Double, Double>, Double,
            SingleTreeIndividual<Pair<Double, Double>, Double>,
            SingleObjectiveFitness
        >(
            new WeightedRandomSampler<>(random, operators),
            populationSize,
            new TournamentSelection<>(random, this.tournamentSize),
            new Elitism<>(elitismCount, p -> SingleObjectiveFitness::compareTo)
        );
    }

    @Override
    public Population<SingleTreeIndividual<Pair<Double, Double>, Double>> breed(
            Population<EvaluatedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>> population
    ) {
        return this.breeder.breed(population);
    }
}

