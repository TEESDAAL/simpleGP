package gp.evaluators;

import gp.Population;
import gp.fitness.Fitness;
import gp.individual.EvaluatedIndividual;
import gp.individual.Individual;

public interface Evaluator<R, T, I extends Individual<R, T>, F extends Fitness<F>> {
    Population<EvaluatedIndividual<R, T, I, F>> evaluate(Population<I> population);
}
