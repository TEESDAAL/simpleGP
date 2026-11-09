package gp.impl.individual;

import gp.impl.individual.testTree.Mutability;
import gp.impl.individual.testTree.Node;
import util.random.RandomSource;

import java.util.Optional;


public interface NodeSelector {
    /**
     * Sample a random node from the tree.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     */
    <T, M extends Mutability> Node<T,?,M,?> sampleNodeOrThrow(
            RandomSource random,
            Node<T,?,M,?> rootNode
    ) throws Exception;

    /**
     * Sample a random node from the tree which returns a type compatible with {@link R}.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     * @param <R> The desired return type of the node.
     */
    <T, R> Node<T,R,?,?,?> sampleNodeOfTypeOrThrow(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode,
            Class<R> returnType
    );

    /**
     * Sample a random terminal node from the tree.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     */
    <T> Terminal<T, ?, ?> sampleTerminalOrThrow(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode
    );

    /**
     * Sample a random terminal from the tree which returns a type compatible with {@link R}.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @param returnType The return type the node must be compatible with.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     * @param <R> The desired return type of the node.
     */
    <T, R> Terminal<T, R, ?> sampleTerminalOfTypeOrThrow(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode,
            Class<R> returnType
    );

    /**
     * Sample a random non-terminal node from the tree.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     */
    <T> NonTerminal<T, ?, ?, ?, ?, ?> sampleNonTerminalOrThrow(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode
    );

    /**
     * Sample a random non-terminal from the tree which returns
     *  a type compatible with {@link R}.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @param returnType The return type the node must be compatible with.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     * @param <R> The desired return type of the node.
     */
    <T, R> NonTerminal<T, R, ?, ?, ?, ?> sampleNonTerminalOfTypeOrThrow(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode,
            Class<R> returnType
    );

    /**
     * Sample a random node from the tree.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     */
    <T> Optional<ImmutableNode<T,?,?,?>> sampleNode(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode
    ) throws Exception;

    /**
     * Sample a random node from the tree which returns a type compatible with {@link R}.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     * @param <R> The desired return type of the node.
     */
    <T, R> Optional<Node<T,R,?,?,?>> sampleNodeOfType(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode,
            Class<R> returnType
    );

    /**
     * Sample a random terminal node from the tree.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     */
    <T> Optional<Terminal<T, ?, ?>> sampleTerminal(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode
    );

    /**
     * Sample a random terminal from the tree which returns a type compatible with {@link R}.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @param returnType The return type the node must be compatible with.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     * @param <R> The desired return type of the node.
     */
    <T, R, Result extends Terminal<T, R, >> Optional<Terminal<T, R, ?, Self>> sampleTerminalOfType(
            RandomSource random,
            Node<T,?,M,?,?,?> rootNode,
            Class<R> returnType
    );

    /**
     * Sample a random non-terminal node from the tree.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     */
    <T> Optional<NonTerminal<T, ?, ?, ?, ?, ?>> sampleNonTerminal(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode
    );

    /**
     * Sample a random non-terminal from the tree which returns
     *  a type compatible with {@link R}.
     * @param random The source of randomness.
     * @param rootNode The root of the tree.
     * @param returnType The return type the node must be compatible with.
     * @return A randomly selected node, empty if there are no valid nodes.
     * @param <T> The terminal type of the node.
     * @param <R> The desired return type of the node.
     */
    <T, R> Optional<NonTerminal<T, R, ?, ?, ?, ?>> sampleNonTerminalOfType(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode,
            Class<R> returnType
    );
}
