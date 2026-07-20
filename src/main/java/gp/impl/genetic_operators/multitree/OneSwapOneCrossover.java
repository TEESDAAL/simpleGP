package gp.impl.genetic_operators.multitree;

import gp.impl.genetic_operators.CrossOver;
import gp.impl.individual.multitree.MultiTreeIndividual;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.individual.tree.ImmutableNode;
import utils.Pair;
import utils.Preconditions;
import utils.operators.BinaryOperator;
import utils.random.RandomSource;

import java.util.ArrayList;
import java.util.List;

/**
 * A GP operator that chooses two random indices i,j. Swaps the trees at index i,
 *  *  and crosses over the trees at index j.
 * @param <I> The type of individual to return afterwards.
 */
public class OneSwapOneCrossover<I extends MultiTreeIndividual<I>>
    implements BinaryOperator<I, List<I>> {
    private final RandomSource rand;

    @SuppressWarnings("rawtypes")
    /// The method which applies cross-over to the two trees,
    /// we use a raw type here as we have to trust two trees we are given
    private final CrossOver crossOver;

    /**
     * Create a OneSwapOneCrossover operator.
     * @param rand The source of randomness, used to choose the indices i,j.
     */
    public OneSwapOneCrossover(RandomSource rand) {
        this.rand = rand;
        this.crossOver = new CrossOver<>(rand);
    }

    @Override
    public List<I> produce(I parent1, I parent2) {
        Preconditions.assertTrue(
                parent1.numTrees() == parent2.numTrees(),
                "Can only cross-over parents with the same number of trees"
        );
        Preconditions.assertTrue(
                parent1.numTrees() >= 2,
                "Must have at least 2 trees to perform swap crossover"
        );

        return swapAndCrossOver(
            rand, crossOver, parent1.trees(), parent2.trees()
        ).reduce((c1, c2) -> List.of(
            parent1.constructor(c1),
            parent2.constructor(c2)
        ));
    }

    /**
     * Perform the one-swap, one-crossover operation on the two parents.
     * This method is static so that it can be used in other contexts.
     * @param rand The source of randomness
     * @param crossOver The object to perform crossover
     * @param parent1Trees The trees of the first parent.
     * @param parent2Trees The trees of the second parent.
     * @return The two children from this operation.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Pair<
        List<SingleTreeIndividual<?, ?>>,
        List<SingleTreeIndividual<?, ?>>
    > swapAndCrossOver(
        RandomSource rand, CrossOver crossOver,
        List<SingleTreeIndividual<?, ?>> parent1Trees,
        List<SingleTreeIndividual<?, ?>> parent2Trees
    ) {
        final int crossOverIndex = rand.nextInt(0, parent1Trees.size());
        int swapIndex = rand.nextInt(0, parent1Trees.size() - 1);
        // pretend we removed the crossover index.
        if (swapIndex >= crossOverIndex) {
            swapIndex += 1;
        }

        final List<ImmutableNode> children = crossOver.produce(
            parent1Trees.get(crossOverIndex).tree(),
            parent2Trees.get(crossOverIndex).tree()
        );
        assert children.size() == 2;
        final List<SingleTreeIndividual<?, ?>> child1 = new ArrayList<>(
            parent1Trees
        );
        final List<SingleTreeIndividual<?, ?>> child2 = new ArrayList<>(
            parent1Trees
        );

        child1.set(crossOverIndex, SingleTreeIndividual.of(children.getFirst()));
        child2.set(crossOverIndex, SingleTreeIndividual.of(children.getLast()));

        swapIndex(swapIndex, child1, child2);

        return Pair.of(child1, child2);
    }

    private static <T> void swapIndex(int index, List<T> list1, List<T> list2) {
        final T elem1 = list1.set(index, list2.get(index));
        list2.set(index, elem1);
    }
}
