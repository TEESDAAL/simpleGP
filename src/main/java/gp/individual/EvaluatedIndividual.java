package gp.individual;

import gp.fitness.Fitness;

public record EvaluatedIndividual<T, R, I extends Individual<T, R>, F extends Fitness<F>>(I individual, F fitness) implements Individual<T, R> {
    @Override
    public R evaluate(T terminals) {
        return this.individual.evaluate(terminals);
    }
}

