package gp.core.fitness;

import gp.impl.fitness.DefaultSingleObjectiveFitness;

/**
 * Interface representing single-objective fitness,
 * which includes a score and an optimization goal.
 */
public interface SingleObjectiveFitness extends DirectlyComparableFitness<
        SingleObjectiveFitness
> {

    static SingleObjectiveFitness of(double score, Goal goal) {
        return DefaultSingleObjectiveFitness.of(score, goal);
    }

    /**
     * Gets the fitness score.
     * @return The fitness score
     */
    double score();

    /**
     * Gets the optimization goal.
     * @return The optimization goal
     */
    Goal goal();

    @Override
    default Comparison compare(
            SingleObjectiveFitness other
    ) {
        return switch (this.goal()) {
            case MAXIMIZE -> Comparison.compareMax(this.score(), other.score());
            case MINIMIZE -> Comparison.compareMin(this.score(), other.score());
        };
    }

    static Fitness<SingleObjectiveFitness> directComparison() {
        return _ -> SingleObjectiveFitness::compare;
    }
}
