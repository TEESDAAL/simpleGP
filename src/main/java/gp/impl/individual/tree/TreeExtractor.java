package gp.impl.individual.tree;

/**
 * An extractor for pulling information out of a tree structure.
 * @param <T> The return type of the extractor.
 */
public interface TreeExtractor<T> {
    /**
     * Extract information out a given terminal node.
     * @param node the terminal node to pull information out of.
     * @return The extracted T from the terminal node.
     */
    T terminal(Terminal<?, ?> node);

    /**
     * Extract information out of a given non-terminal node,
     *  often this will be done by recursively calling down the tree,
     *      `child.extract(this)`.
     * @param node The non-terminal to extract from.
     * @return The extracted T from the terminal node.
     */
    T nonTerminal(NonTerminal<?, ?, ?, ?> node);
}
