package gp.fitness.multi_objective;

import gp.fitness.Comparison;
import gp.fitness.Fitness;
import gp.fitness.single_objective.SingleObjectiveFitness;
import gp.utils.StreamZipper;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public interface MultiObjectiveFitness<Self extends MultiObjectiveFitness<Self>>
        extends Fitness<Self> {
    /**
     * Gets the list of single-objective fitness values.
     * @return The list of single-objective fitness values
     */
    List<SingleObjectiveFitness> fitnesses();

    /**
     * Gets the single-objective fitness value at the specified index.
     * @param index The index of the fitness value to retrieve
     * @return The single-objective fitness value at the specified index
     */
    default SingleObjectiveFitness getFitness(int index) {
        return fitnesses().get(index);
    }

    /**
     * Compares this multi-objective fitness with another.
     * Uses Pareto dominance for comparison.
     * @param other The other fitness
     * @return The comparison result
     */
    @Override
    default Comparison compareWith(final Self other) {
        assert this.fitnesses().size() == other.fitnesses().size();

        List<Comparison> comparisons = StreamZipper.zip(
                this.fitnesses().stream(),
                other.fitnesses().stream(),
                SingleObjectiveFitness::compareWith
        ).distinct().toList();

        boolean anyBetter = comparisons.contains(Comparison.BETTER);
        boolean anyWorse = comparisons.contains(Comparison.WORSE);


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
    default boolean dominates(final Self other) {
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
    default boolean isDominatedBy(final Self other) {
        return switch (this.compareWith(other)) {
            case WORSE -> true;
            case BETTER, EQUAL -> false;
        };
    }

    /**
     * Checks if this fitness is Pareto equal to another,
     *      meaning neither fitness dominates the other.
     * @param other The other fitness
     * @return True if neither fitness dominates the other.
     */
    default boolean paretoEqual(final Self other) {
        return this.compareWith(other) == Comparison.EQUAL;
    }

    /**
     * Computes the Pareto front from a list of multi-objective fitnesses.
     * @param fitnesses The list of fitnesses to evaluate
     * @param <MOF> The type of multi-objective fitness
     * @return A list of fitnesses on the Pareto front
     */
    static <MOF extends MultiObjectiveFitness<MOF>> List<MOF> paretoFront(
            List<MOF> fitnesses
    ) {
        List<MOF> front = new ArrayList<>();
        for (MOF currentFitness : fitnesses) {
           if (front.stream().noneMatch(currentFitness::isDominatedBy)) {
               front.removeIf(currentFitness::dominates);
               front.add(currentFitness);
           }
        }

        return Collections.unmodifiableList(front);
    }


    /**
     * Computes the Pareto ranks from a list of multi-objective fitnesses.
     * Performs this by recursively finding pareto fronts.
     * @param fitnesses The list of fitnesses to evaluate
     * @return A map from rank to list of fitnesses with that rank,
     *      where rank 0 is the Pareto front
     * @param <MOF> The type of multi-objective fitness
     */
    static <
            MOF extends MultiObjectiveFitness<MOF>
    > Map<Integer, List<MOF>> paretoRanks(List<MOF> fitnesses) {
       List<MOF> fitnessesCopy = new ArrayList<>(fitnesses);
       Map<Integer, List<MOF>> ranks = new HashMap<>();
       int ranking = 0;

       while (!fitnessesCopy.isEmpty()) {
          List<MOF> front = paretoFront(fitnessesCopy);
          ranks.put(ranking, front);
          fitnessesCopy.removeAll(front);
          ranking++;
       }

       return Collections.unmodifiableMap(ranks);
    }
}

