package gp.breeder;

import gp.Population;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;
import gp.utils.Operator;
import gp.statistics.Selector;
import gp.random.WeightedRandomSampler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public record NaiveBreeder<T, R, I extends Individual<T, R>, F extends Fitness<F>>(
        WeightedRandomSampler<Operator<I, I>> distribution,
        int desiredSize,
        SelectorBuilder<EvaluatedIndividual<T, R, I, F>> selectionMechanism
) implements Breeder<EvaluatedIndividual<T, R, I, F>, I> {

    @Override
    public Population<I> breed(Population<EvaluatedIndividual<T, R, I, F>> population) {
        Selector<EvaluatedIndividual<T, R, I, F>> selector = selectionMechanism.prime(population.individuals());
        List<I> nextGeneration = new ArrayList<>(this.desiredSize);
        for  (int i = 0; i < this.desiredSize; i++) {
            Operator<I, I> operator = this.distribution.sample();
            nextGeneration.add(
                    operator.sampleFrom(selector, EvaluatedIndividual::individual)
            );
        }
        return Population.of(
                nextGeneration
        );
//        return Population.of(
//                IntStream.range(0, desiredSize)
//                        .mapToObj( ignored -> distribution.sample())
//                        .map(operator -> operator.sampleFrom(selector, EvaluatedIndividual::individual))
//                        .toList()
//        );
    }
}
