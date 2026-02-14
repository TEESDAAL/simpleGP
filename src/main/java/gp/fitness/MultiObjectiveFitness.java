package gp.fitness;

import gp.single_tree.SingleObjectiveFitness;

import java.util.List;

public record MultiObjectiveFitness(List<SingleObjectiveFitness> fitnesses) implements Fitness<MultiObjectiveFitness> {
    @Override
    public Comparison compareWith(MultiObjectiveFitness other) {
        assert this.fitnesses.size() == other.fitnesses.size();
        boolean anyBetter = false;
        boolean anyWorse = false;

        for (int i = 0; i < this.fitnesses.size(); i++) {
            switch (this.compareWith(other)) {
                case BETTER ->  anyBetter = true;
                case WORSE -> anyWorse = true;
                default -> {}
            }
        }
        // The two individuals are on the same front
        if (anyBetter == anyWorse) {
            return Comparison.EQUAL;
        }
        if (anyBetter) {
            return Comparison.BETTER;
        }
        return Comparison.WORSE;
    }

    public boolean dominates(MultiObjectiveFitness other) {
        return switch (this.compareWith(other)) {
            case BETTER -> true;
            case WORSE, EQUAL -> false;
        };
    }
}
