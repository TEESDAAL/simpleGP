package gp.impl.individual.multitree;

import gp.impl.individual.SingleTreeIndividual;

/**
 * A getter for the nth tree of a multiTreeGP individual
 * Exists to avoid repeated calls to `.next`
 */
public final class Get {
    private Get() {}

    /**
     * Get the first tree in the multiTree. Equivalent to tree.tree();
     * @param tree the tree to extract from.
     * @return the first tree.
     * @param <T> the terminal type of the tree.
     * @param <R> the return type of the tree.
     */
    public static <T, R> SingleTreeIndividual<T, R> first(
        final MultiTree<T, R, ?, ?> tree
    ) {
        return tree.tree();
    }

    /**
     * Get the second tree in the multiTree. Equivalent to tree.next().tree();
     * @param tree the tree to extract from.
     * @return the second tree.
     * @param <T> the terminal type of the tree.
     * @param <R> the return type of the tree.
     */
    public static <
        T, R,
        N1 extends MultiTree<T, R, ?, ?>
    > SingleTreeIndividual<T, R> second(
        final MultiTree<?, ?, N1, ?> tree
    ) {
        return tree.next().tree();
    }

    /**
     * Get the third tree in the multiTree.
     * @param tree the tree to extract from.
     * @return the third tree.
     * @param <T> the terminal type of the tree.
     * @param <R> the return type of the tree.
     */
    public static <
        T, R,
        N1 extends MultiTree<?, ?, N2, ?>,
        N2 extends MultiTree<T, R, ?, ?>
    > SingleTreeIndividual<T, R> third(
        final MultiTree<?, ?, N1, ?> tree
    ) {
        return tree.next().next().tree();
    }

    /**
     * Get the fourth tree in the multiTree.
     * @param tree the tree to extract from.
     * @return the fourth tree.
     * @param <T> the terminal type of the tree.
     * @param <R> the return type of the tree.
     */
    public static <
        T, R,
        N1 extends MultiTree<?, ?, N2, ?>,
        N2 extends MultiTree<?, ?, N3, ?>,
        N3 extends MultiTree<T, R, ?, ?>
    > SingleTreeIndividual<T, R> fourth(
        final MultiTree<?, ?, N1, ?> tree
    ) {
        return tree.next().next().next().tree();
    }

    /**
     * Get the fifth tree in the multiTree.
     * @param tree the tree to extract from.
     * @return the fifth tree.
     * @param <T> the terminal type of the tree.
     * @param <R> the return type of the tree.
     */
    public static <
        T, R,
        N1 extends MultiTree<?, ?, N2, ?>,
        N2 extends MultiTree<?, ?, N3, ?>,
        N3 extends MultiTree<?, ?, N4, ?>,
        N4 extends MultiTree<T, R, ?, ?>
    > SingleTreeIndividual<T, R> fifth(
        final MultiTree<?, ?, N1, ?> tree
    ) {
        return tree.next().next().next().next().tree();
    }

    /**
     * Get the third tree in the multiTree.
     * @param tree the tree to extract from.
     * @return the third tree.
     * @param <T> the terminal type of the tree.
     * @param <R> the return type of the tree.
     */
    public static <
        T, R,
        N1 extends MultiTree<?, ?, N2, ?>,
        N2 extends MultiTree<?, ?, N3, ?>,
        N3 extends MultiTree<?, ?, N4, ?>,
        N4 extends MultiTree<?, ?, N5, ?>,
        N5 extends MultiTree<T, R, ?, ?>
    > SingleTreeIndividual<T, R> sixth(
        final MultiTree<?, ?, N1, ?> tree
    ) {
        return tree.next().next().next().next().next().tree();
    }

    /**
     * Get the seventh tree in the multiTree.
     * @param tree the tree to extract from.
     * @return the seventh tree.
     * @param <T> the terminal type of the tree.
     * @param <R> the return type of the tree.
     */
    public static <
        T, R,
        N1 extends MultiTree<?, ?, N2, ?>,
        N2 extends MultiTree<?, ?, N3, ?>,
        N3 extends MultiTree<?, ?, N4, ?>,
        N4 extends MultiTree<?, ?, N5, ?>,
        N5 extends MultiTree<?, ?, N6, ?>,
        N6 extends MultiTree<T, R, ?, ?>
    > SingleTreeIndividual<T, R> seventh(
        final MultiTree<?, ?, N1, ?> tree
    ) {
        return tree.next().next().next().next().next().next().tree();
    }

    /**
     * Get the eighth tree in the multiTree.
     * @param tree the tree to extract from.
     * @return the eighth tree.
     * @param <T> the terminal type of the tree.
     * @param <R> the return type of the tree.
     */
    public static <
        T, R,
        N1 extends MultiTree<?, ?, N2, ?>,
        N2 extends MultiTree<?, ?, N3, ?>,
        N3 extends MultiTree<?, ?, N4, ?>,
        N4 extends MultiTree<?, ?, N5, ?>,
        N5 extends MultiTree<?, ?, N6, ?>,
        N6 extends MultiTree<?, ?, N7, ?>,
        N7 extends MultiTree<T, R, ?, ?>
    > SingleTreeIndividual<T, R> eighth(
        final MultiTree<?, ?, N1, ?> tree
    ) {
        return tree.next().next().next().next().next()
            .next().next().tree();
    }

    /**
     * Get the nineth tree in the multiTree.
     * @param tree the tree to extract from.
     * @return the third tree.
     * @param <T> the terminal type of the tree.
     * @param <R> the return type of the tree.
     */
    public static <
        T, R,
        N1 extends MultiTree<?, ?, N2, ?>,
        N2 extends MultiTree<?, ?, N3, ?>,
        N3 extends MultiTree<?, ?, N4, ?>,
        N4 extends MultiTree<?, ?, N5, ?>,
        N5 extends MultiTree<?, ?, N6, ?>,
        N6 extends MultiTree<?, ?, N7, ?>,
        N7 extends MultiTree<?, ?, N8, ?>,
        N8 extends MultiTree<T, R, ?, ?>
    > SingleTreeIndividual<T, R> ninth(
        final MultiTree<?, ?, N1, ?> tree
    ) {
        return tree.next().next().next().next().next()
            .next().next().next().tree();
    }

    /**
     * Get the tenth tree in the multiTree.
     * @param tree the tree to extract from.
     * @return the third tree.
     * @param <T> the terminal type of the tree.
     * @param <R> the return type of the tree.
     */
    public static <
        T, R,
        N1 extends MultiTree<?, ?, N2, ?>,
        N2 extends MultiTree<?, ?, N3, ?>,
        N3 extends MultiTree<?, ?, N4, ?>,
        N4 extends MultiTree<?, ?, N5, ?>,
        N5 extends MultiTree<?, ?, N6, ?>,
        N6 extends MultiTree<?, ?, N7, ?>,
        N7 extends MultiTree<?, ?, N8, ?>,
        N8 extends MultiTree<?, ?, N9, ?>,
        N9 extends MultiTree<T, R, ?, ?>
    > SingleTreeIndividual<T, R> tenth(
        final MultiTree<?, ?, N1, ?> tree
    ) {
        return tree.next().next().next().next()
            .next().next().next().next().next().tree();
    }



}
