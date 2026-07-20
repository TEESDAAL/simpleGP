package gp.impl.genetic_operators.multitree;

import gp.impl.genetic_operators.CrossOver;
import gp.impl.individual.SingleTreeIndividual;
import gp.impl.individual.multitree.End;
import gp.impl.individual.multitree.MultiTree;
import gp.impl.individual.multitree.Next;
import utils.operators.BinaryOperator;
import utils.random.RandomSource;

import java.util.ArrayList;
import java.util.List;

/**
 * A GP operator that chooses two random indices i,j. Swaps the trees at index i,
 *  and crosses over the trees at index j.
 * @param <T> The terminal type of the first tree.
 * @param <R> The return type of the second tree.
 * @param <Tail> The type information of the rest of the trees.
 * @param <I> The individual type.
 */
public class FullyTypedOneSwapOneCrossover<
    T, R, Tail extends MultiTree<T, R, Tail, ?>,
    I extends MultiTree<T, R, Tail, I>
> implements BinaryOperator<I, List<I>> {
    private final RandomSource rand;
    private final CrossOver<?, ?> crossOver;

    /**
     * Create a FullyTypedOneSwapOneCrossover.
     * @param rand The source of randomness, used to choose the indices i,j.
     */
    public FullyTypedOneSwapOneCrossover(final RandomSource rand) {
        this.rand = rand;
        this.crossOver = new CrossOver<>(rand);
    }

    @Override
    public List<I> produce(final I parent1, final I parent2) {
        final Trees trees = trees(parent1, parent2);
        return OneSwapOneCrossover.swapAndCrossOver(
            rand, crossOver, trees.parent1(), trees.parent2()
        ).reduce((c1, c2) -> List.of(
            parent1.constructor(listToMultiTree(c1)),
            parent2.constructor(listToMultiTree(c2))
        ));

    }

    private MultiTree<T, R, Tail, ?> listToMultiTree(
        final List<SingleTreeIndividual<?, ?>> trees
    ) {
        MultiTree<?, ?, ?, ?> multiTree = MultiTree.of(trees.getLast());

        for (int i = trees.size() - 2; i >= 0; i--) {
            multiTree = multiTree.addFirst(trees.get(i));
        }
        //noinspection unchecked
        return (MultiTree<T, R, Tail, ?>) multiTree;
    }

    private static <T, R, Tail extends Next> Trees trees(
        final MultiTree<T, R, Tail, ?> tree1,
        final MultiTree<T, R, Tail, ?> tree2
    ) {
        final Trees trees = Trees.mutable();
        getAllTrees(tree1, tree2, trees);
        return trees;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T, R, Tail extends Next> void getAllTrees(
        final MultiTree<T, R, Tail, ?> tree1,
        final MultiTree<T, R, Tail, ?> tree2,
        final Trees trees
    ) {
        trees.parent1().add(tree1.tree());
        trees.parent2().add(tree2.tree());
        if (tree1.next() instanceof End && tree2.next() instanceof End) {
            return;
        }

        if (tree1.next() instanceof final MultiTree t1
            && tree2.next() instanceof final MultiTree t2) {
            getAllTrees(t1, t2, trees);
            return;
        }

        throw new IllegalStateException(
            "MultiTrees aren't the same length. "
            + "Note this should be impossible given the "
            + "types of the trees are encoded at the type level."
        );
    }
}

/**
 * A Record which stores the trees of both parents.
 * @param parent1
 * @param parent2
 */
record Trees(
    List<SingleTreeIndividual<?, ?>> parent1,
    List<SingleTreeIndividual<?, ?>> parent2
) {
    Trees {
        assert parent1.size() == parent2.size();
    }

    public static Trees mutable() {
        return new Trees(new ArrayList<>(), new ArrayList<>());
    }
}
