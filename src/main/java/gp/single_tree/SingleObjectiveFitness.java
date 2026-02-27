package gp.single_tree;

import gp.fitness.Comparison;
import gp.fitness.Fitness;
import gp.fitness.Goal;

/**
 * A record representing fitness for single-objective optimization.
 * @param score The fitness score
 * @param goal The optimization goal (maximize or minimize)
 */
public record SingleObjectiveFitness(Double score, Goal goal)
        implements Fitness<SingleObjectiveFitness> {
    @Override
    public Comparison compareWith(
            final SingleObjectiveFitness other
    ) {
        Comparison result = Comparison.of(this.score, other.score);

        return switch (goal) {
            case MAXIMIZE -> result;
            case MINIMIZE -> result.flip();
        };
    }
}
