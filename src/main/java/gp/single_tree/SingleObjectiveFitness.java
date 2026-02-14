package gp.single_tree;

import gp.fitness.Comparison;
import gp.fitness.Fitness;
import gp.fitness.Goal;

public record SingleObjectiveFitness(Double score, Goal goal) implements Fitness<SingleObjectiveFitness> {
    @Override
    public Comparison compareWith(SingleObjectiveFitness other) {
        Comparison result = Comparison.of(this.score, other.score);

        return switch(goal) {
            case MAXIMIZE -> result;
            case MINIMIZE -> result.flip();
        };
    }
}
