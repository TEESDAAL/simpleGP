package gp.fitness.single_objective;

import gp.fitness.Goal;

import java.util.function.Function;

/**
 * Record representing single-objective fitness with a score and optimization goal.
 * @param score The fitness score
 * @param goal The optimization goal
 */
public record SingleObjectiveFit(double score, Goal goal)
        implements SingleObjectiveFitness {
    /**
     * Factory method to create a SingleObjectiveFitness instance.
     * @param score The fitness score
     * @param goal The optimization goal
     * @return A new SingleObjectiveFitness instance
     */
    public static SingleObjectiveFit of(double score, Goal goal) {
        return new SingleObjectiveFit(score, goal);
    }

    /**
     * Maps the score using the provided mapper function,
     *  returning a new SingleObjectiveFitness.
     * @param mapper The function to map the score
     * @return A new SingleObjectiveFitness with the mapped score and same goal
     */
    public SingleObjectiveFit map(final Function<Double, Double> mapper) {
        return SingleObjectiveFit.of(mapper.apply(this.score()), this.goal());
    }
}
