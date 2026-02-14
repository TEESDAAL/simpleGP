package gp.evaluators;

import gp.Population;
import gp.breeder.Parallelizeable;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;

public interface IndividualEvaluator<R, T, I extends Individual<R, T>, F extends Fitness<F>>
        extends Evaluator<R, T, I, F>, Parallelizeable {
    EvaluatedIndividual<R, T, I, F> evaluate(I individual);
    @Override
    default Population<EvaluatedIndividual<R, T, I, F>> evaluate(Population<I> population) {
        return Population.of(
                this.parallelize(population.individuals().stream(), this::evaluate).toList()
        );
    }
}

