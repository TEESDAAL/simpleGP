package example.function_approximation;

import gp.Population;
import gp.core.breeder.Breeder;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.AssessedIndividual;
import gp.core.initializer.PrimitiveSet;
import gp.impl.breeder.NaiveBreeder;
import gp.impl.genetic_operator.Crossover;
import gp.impl.genetic_operator.Identity;
import gp.impl.genetic_operator.SubtreeMutation;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.selector.Elitism;
import gp.impl.selector.TournamentSelection;
import gp.impl.selector.random.DistributionBuilder;
import util.random.RandomSource;

public class DefaultBreeder<T, R> implements Breeder<AssessedIndividual<T, R, SingleTreeIndividual<T, R>, SingleObjectiveFitness>, SingleTreeIndividual<T, R>> {
    protected final RandomSource random;
    protected final int populationSize = 1000;
    protected final int tournamentSize = 7;
    protected final int elitismCount = 0;
    protected final PrimitiveSet<T> primitiveSet;

    protected final Breeder<AssessedIndividual<
        T, R,
        SingleTreeIndividual<T, R>,
        SingleObjectiveFitness
    >, SingleTreeIndividual<T, R>> breeder;


    public DefaultBreeder(RandomSource random, PrimitiveSet<T> primitiveSet) {
        this.random = random;
        this.primitiveSet = primitiveSet;
        final var sampler = DistributionBuilder.startingWith(
            0.01, SingleTreeIndividual.<T, R>operator(new SubtreeMutation<>(
                random, primitiveSet, 7, 100
            )))
            .addElement(0.65, SingleTreeIndividual.operator(new Crossover<>(
                random, 7, 1
            )))
            .addDefault(new Identity<>())
            .toSampler(random);

        this.breeder = new NaiveBreeder<>(
            sampler,
            populationSize,
            new TournamentSelection<>(random, this.tournamentSize, SingleObjectiveFitness.directComparison()),
            new Elitism<>(elitismCount, SingleObjectiveFitness.directComparison())
        );
    }

    @Override
    public Population<SingleTreeIndividual<T, R>> breed(
            Population<AssessedIndividual<T, R, SingleTreeIndividual<T, R>, SingleObjectiveFitness>> population
    ) {
        return this.breeder.breed(population);
    }
}

