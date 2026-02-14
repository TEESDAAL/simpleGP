package gp.selectors;

import gp.breeder.SelectorBuilder;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;
import gp.random.RandomSampler;
import gp.statistics.Selector;

import java.util.Collection;
import java.util.Random;
import java.util.stream.IntStream;

public record TournamentSelection<R, T, I extends Individual<T, R>, F extends Fitness<F>>(
        Random random,
        int tournamentSize
) implements SelectorBuilder<EvaluatedIndividual<T, R, I, F>> {
    public TournamentSelection {
        if (tournamentSize <= 0) {
            throw new IllegalArgumentException("tournament size must be greater than 0.");
        }
    }

    @Override
    public Selector<EvaluatedIndividual<T, R, I, F>> prime(Collection<EvaluatedIndividual<T, R, I, F>> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Must be able to select an individual.");
        }
        return () -> IntStream.range(0, tournamentSize)
                    .mapToObj(ignored -> RandomSampler.sample(items, random).orElseThrow())
                    .reduce((bestIndividual, currentIndividual) -> switch (currentIndividual.fitness().compareWith(bestIndividual.fitness())) {
                            case BETTER -> currentIndividual;
                            case EQUAL, WORSE -> bestIndividual;
                        }).orElseThrow();
    }
}
