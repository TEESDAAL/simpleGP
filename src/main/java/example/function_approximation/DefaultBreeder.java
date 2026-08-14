package example.function_approximation;

import gp.Population;
import gp.core.breeder.Breeder;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.AssessedIndividual;
import gp.core.initializer.PrimitiveSet;
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

import java.util.List;

public class DefaultBreeder implements Breeder<AssessedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>, SingleTreeIndividual<Pair<Double, Double>, Double>> {
    protected final RandomSource random;
    protected final int populationSize = 1000;
    protected final int tournamentSize = 7;
    protected final int elitismCount = 10;
    protected final PrimitiveSet<Pair<Double, Double>> primitiveSet;

    protected final Breeder<AssessedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>, SingleTreeIndividual<Pair<Double, Double>, Double>> breeder;
    protected final List<ProbabilisticElement<Operator<
        SingleTreeIndividual<Pair<Double, Double>, Double>,
        List<SingleTreeIndividual<Pair<Double, Double>, Double>>
    >>> operators;

    public DefaultBreeder(RandomSource random, PrimitiveSet<Pair<Double, Double>> primitiveSet) {
        this.random = random;
        this.primitiveSet = primitiveSet;
        this.operators = ProbabilisticElement.withFallback(
            new Identity<>(),
            List.of(
                ProbabilisticElement.of(0.01, SingleTreeIndividual.operator(new SubtreeMutation<>(
                    random, primitiveSet, 7, 100
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
            new Elitism<>(elitismCount, _ -> SingleObjectiveFitness::compareTo)
        );
    }

    @Override
    public Population<SingleTreeIndividual<Pair<Double, Double>, Double>> breed(
            Population<AssessedIndividual<Pair<Double, Double>, Double, SingleTreeIndividual<Pair<Double, Double>, Double>, SingleObjectiveFitness>> population
    ) {
        return this.breeder.breed(population);
    }
}

