package gp.fitness;

import gp.single_tree.SingleObjectiveFitness;

import java.util.List;

/**
 * Record representing multi-objective fitness with multiple
 * single-objective scores.
 * @param fitnesses The list of single-objective fitness values
 */
public record MultiObjectiveFitness(
        List<SingleObjectiveFitness> fitnesses
) implements Fitness<MultiObjectiveFitness> {
    /**
     * Compares this multi-objective fitness with another.
     * Uses Pareto dominance for comparison.
     * @param other The other fitness
     * @return The comparison result
     */
    @Override
    public Comparison compareWith(final MultiObjectiveFitness other) {
        assert this.fitnesses.size() == other.fitnesses.size();
        boolean anyBetter = false;
        boolean anyWorse = false;

        for (int i = 0; i < this.fitnesses.size(); i++) {
            Comparison comparison = this.fitnesses.get(i)
                    .compareWith(other.fitnesses.get(i));
            if (comparison == Comparison.BETTER) {
                anyBetter = true;
            } else if (comparison == Comparison.WORSE) {
                anyWorse = true;
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

    /**
     * Checks if this fitness dominates another using Pareto dominance.
     * @param other The other fitness
     * @return True if this dominates the other
     */
    public boolean dominates(final MultiObjectiveFitness other) {
        return switch (this.compareWith(other)) {
            case BETTER -> true;
            case WORSE, EQUAL -> false;
        };
    }

    /**
     * Checks if this fitness is dominated by another using Pareto dominance.
     * @param other The other fitness
     * @return True if this is dominated by the other
     */
    public boolean isDominatedBy(final MultiObjectiveFitness other) {
        return switch (this.compareWith(other)) {
            case WORSE -> true;
            case BETTER, EQUAL -> false;
        };
    }
}
