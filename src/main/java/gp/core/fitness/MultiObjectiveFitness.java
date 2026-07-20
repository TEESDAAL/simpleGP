package gp.core.fitness;

import utils.Pair;
import utils.stream_utils.StreamZipper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A fitness composed of multiple single-objective fitness values.
 *
 * @param <Self> the concrete multi-objective fitness type
 */
public interface MultiObjectiveFitness<Self extends MultiObjectiveFitness<Self>>
        extends Fitness<Self> {
    /**
     * Gets the list of single-objective fitness values.
     *
     * @return the list of single-objective fitness values
     */
    List<SingleObjectiveFitness> fitnesses();

    /**
     * Gets the single-objective fitness value at the specified index.
     *
     * @param index the index of the fitness value to retrieve
     * @return the single-objective fitness value at the specified index
     */
    default SingleObjectiveFitness getFitness(final int index) {
        return fitnesses().get(index);
    }

    /**
     * Compares this multi-objective fitness with another.
     * Uses Pareto dominance for comparison.
     *
     * @param other the other fitness
     * @return the comparison result
     */
    @Override
    default Comparison compareWith(Self other) {
        assert this.fitnesses().size() == other.fitnesses().size();

        final List<Comparison> comparisons = StreamZipper.zip(
                this.fitnesses().stream(),
                other.fitnesses().stream(),
                SingleObjectiveFitness::compareWith
        ).distinct().toList();

        final boolean anyBetter = comparisons.contains(Comparison.BETTER);
        final boolean anyWorse = comparisons.contains(Comparison.WORSE);

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
     *
     * @param other the other fitness
     * @return true if this dominates the other
     */
    default boolean dominates(Self other) {
        return switch (this.compareWith(other)) {
            case BETTER -> true;
            case WORSE, EQUAL -> false;
        };
    }

    /**
     * Checks if this fitness is dominated by another using Pareto dominance.
     *
     * @param other the other fitness
     * @return true if this is dominated by the other
     */
    default boolean isDominatedBy(Self other) {
        return switch (this.compareWith(other)) {
            case WORSE -> true;
            case BETTER, EQUAL -> false;
        };
    }

    /**
     * Checks if this fitness is Pareto equal to another,
     * meaning neither fitness dominates the other.
     *
     * @param other the other fitness
     * @return true if neither fitness dominates the other
     */
    default boolean paretoEqual(Self other) {
        return this.compareWith(other) == Comparison.EQUAL;
    }

    /**
     * Computes the Pareto front from a list of multi-objective fitnesses.
     *
     * @param individuals the individuals to form a front from
     * @param extractor the extractor to pull out the fitness
     * @param <I> the type of individuals
     * @param <MOF> the type of multi-objective fitness
     * @return a list of individuals on the Pareto front
     */
    static <I, MOF extends MultiObjectiveFitness<MOF>> List<I> paretoFront(
            final List<I> individuals,
            final Function<I, MOF> extractor
    ) {

        final List<Pair<I, MOF>> front = new ArrayList<>();
        for (I currentInd : individuals) {
            final MOF currentFitness = extractor.apply(currentInd);
            final boolean dominated = front.stream()
                    .map(Pair::second)
                    .anyMatch(currentFitness::isDominatedBy);
            if (!dominated) {
                front.removeIf(p -> currentFitness.dominates(p.second()));
                front.add(Pair.of(currentInd, currentFitness));
            }
        }

        return front.stream().map(Pair::first).toList();
    }


    /**
     * Computes the Pareto ranks from a list of multi-objective fitnesses.
     * Performs this by recursively finding pareto fronts.
     *
     * @param fitnesses the individuals to rank
     * @param extractor the extractor to pull out the fitness
     * @param <I> the type of individuals
     * @param <MOF> the type of multi-objective fitness
     * @return a map from rank to list of individuals with that rank,
     *     where rank 0 is the Pareto front
     */
    static <I, MOF extends MultiObjectiveFitness<MOF>> Map<Integer, List<I>>
    paretoRanks(
            final Collection<I> fitnesses,
            final Function<I, MOF> extractor
    ) {
        final List<I> individualsCopy = new ArrayList<>(fitnesses);
        final Map<Integer, List<I>> ranks = new HashMap<>();
        int ranking = 0;

        while (!individualsCopy.isEmpty()) {
            final List<I> front = paretoFront(individualsCopy, extractor);
            ranks.put(ranking, front);
            individualsCopy.removeAll(front);
            ranking++;
        }

        return Collections.unmodifiableMap(ranks);
    }

    /**
     * Computes the Pareto ranks from a list of multi-objective fitnesses.
     * Performs this by recursively finding pareto fronts.
     *
     * @param fitnesses the individuals to rank
     * @param extractor the extractor to pull out the fitness
     * @param <I> the type of individuals
     * @param <MOF> the type of multi-objective fitness
     * @return a map from each individual to its Pareto rank
     */
    static <I, MOF extends MultiObjectiveFitness<MOF>> Map<I, Integer> rankings(
            final Collection<I> fitnesses,
            final Function<I, MOF> extractor
    ) {
        final Map<Integer, List<I>> ranks = paretoRanks(fitnesses, extractor);
        final Map<I, Integer> rankMap = new HashMap<>();
        for (Map.Entry<Integer, List<I>> entry : ranks.entrySet()) {
            for (I individual : entry.getValue()) {
                rankMap.put(individual, entry.getKey());
            }
        }
        return Collections.unmodifiableMap(rankMap);
    }


    /**
     * Returns an ordering that sorts by Pareto rank.
     *
     * @param population the population to rank
     * @param extractor the extractor to pull out the fitness
     * @param <I> the type of individuals
     * @param <MOF> the type of multi-objective fitness
     * @return a comparator that sorts by rank
     */
    static <I, MOF extends MultiObjectiveFitness<MOF>> Comparator<I> ordering(
            final Collection<I> population,
            final Function<I, MOF> extractor
    ) {
        final Map<I, Integer> ranks = rankings(population, extractor);
        return Comparator.comparingInt(ranks::get);
    }
}

