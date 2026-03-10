package gp.fitness.single_objective;

import gp.fitness.Comparison;
import gp.fitness.Fitness;
import gp.fitness.Goal;

/**
 * Interface representing single-objective fitness,
 * which includes a score and an optimization goal.
 */
public interface SingleObjectiveFitness extends Fitness<SingleObjectiveFitness>,
        Comparable<SingleObjectiveFitness> {
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
    default Comparison compareWith(
            final SingleObjectiveFitness other
    ) {
        Comparison result = Comparison.of(this.score(), other.score());

        return switch (this.goal()) {
            case MAXIMIZE -> result;
            case MINIMIZE -> result.flip();
        };
    }

    @Override
    default int compareTo(SingleObjectiveFitness o) {
        return compareWith(o).ord();
    }


}
