package gp.core.fitness;

import utils.Pair;
import utils.stream_utils.StreamZipper;

import java.util.*;
import java.util.function.Function;

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
     * @param individuals the individuals to form a front from.
     * @param extractor the extractor to pull out the fitness.
     * @param <I> the type of individuals.
     * @param <MOF> The type of multi-objective fitness
     * @return A list of fitnesses on the Pareto front
     */
    static <I, MOF extends MultiObjectiveFitness<MOF>> List<I> paretoFront(
            List<I> individuals,
            Function<I, MOF> extractor
    ) {

        List<Pair<I, MOF>> front = new ArrayList<>();
        for (I currentInd : individuals) {
            MOF currentFitness = extractor.apply(currentInd);
            if (front.stream().map(Pair::second).noneMatch(currentFitness::isDominatedBy)) {
                front.removeIf(p -> currentFitness.dominates(p.second()));
                front.add(Pair.of(currentInd, currentFitness));
            }
        }

        return front.stream().map(Pair::first).toList();
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
        I, MOF extends MultiObjectiveFitness<MOF>
    > Map<Integer, List<I>> paretoRanks(
        Collection<I> fitnesses,  Function<I, MOF> extractor
    ) {
       List<I> individualsCopy = new ArrayList<>(fitnesses);
       Map<Integer, List<I>> ranks = new HashMap<>();
       int ranking = 0;

       while (!individualsCopy.isEmpty()) {
          List<I> front = paretoFront(individualsCopy, extractor);
          ranks.put(ranking, front);
          individualsCopy.removeAll(front);
          ranking++;
       }

       return Collections.unmodifiableMap(ranks);
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
        I, MOF extends MultiObjectiveFitness<MOF>
        > Map<I, Integer> rankings(
        Collection<I> fitnesses,  Function<I, MOF> extractor
    ) {
        Map<Integer, List<I>> ranks = paretoRanks(fitnesses, extractor);
        Map<I, Integer> rankMap = new HashMap<>();
        for (Map.Entry<Integer, List<I>> entry : ranks.entrySet()) {
            for  (I individual: entry.getValue()) {
                rankMap.put(individual, entry.getKey());
            }
        }
        return Collections.unmodifiableMap(rankMap);
    }


    static <I, MOF extends MultiObjectiveFitness<MOF>> Comparator<I> ordering(
        Collection<I> population, Function<I, MOF> extractor
    ) {
        Map<I, Integer> ranks = rankings(population, extractor);
        return Comparator.comparingInt(ranks::get);
    }
}

