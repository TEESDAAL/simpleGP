package gp.impl.genetic_operator;

import gp.impl.individual.typed_tree.*;
import gp.impl.selector.random.RandomSampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;
import util.random.RandomSource;

import java.util.*;
import java.util.function.Predicate;

public enum CrossoverUtils {;
    private static final Logger logger = LoggerFactory.getLogger(CrossoverUtils.class);

    public static <T> void swapChildren(
            MutableNonTerminal<T, ?, ?, ?> firstParent,
            int child1Index,
            MutableNonTerminal<T, ?, ?, ?> secondParent,
            int child2Index
    ) throws TypeMismatchException {
        final MutableNode<T, ?, ?, ?> firstChild = firstParent.getChild(child1Index);
        final MutableNode<T, ?, ?, ?> secondChild = secondParent.getChild(child2Index);

        checkTypesMatch(firstParent, child1Index, secondChild);
        checkTypesMatch(secondParent, child2Index, firstChild);

        firstParent.replaceChild(child1Index, _ -> secondChild);
        secondParent.replaceChild(child2Index, _ -> firstChild);
    }

    public static <T, Out> List<ImmutableNode<T, Out, ?, ?>> crossOverAtMutationPoints(
            MutableNode<T, Out, ?, ?> parent1,
            MutableNode<T, Out, ?, ?> parent2,
            ChildNode<T> mutationPoint1,
            ChildNode<T> mutationPoint2
    ) {
        swapChildren(
                mutationPoint1.parent(), mutationPoint1.indexInParent(),
                mutationPoint2.parent(), mutationPoint2.indexInParent()
        );
        return List.of(
                parent1.immutableCopy(),
                parent2.immutableCopy()
        );
    }

    static void checkTypesMatch(NonTerminal<?,?,?,?,?,?> parent, int childIndex, Node<?,?,?,?,?> replacementChild) {
        final Class<?> neededInputType = parent.combiner().inputTypes().get(childIndex);

        if (neededInputType.isAssignableFrom(replacementChild.returnType())) {
            throw new TypeMismatchException(
                    "Tried to replace the child at index "+childIndex+" with a tree that returns "+replacementChild.returnType()
                            + ", the return type must be compatible with "+neededInputType
            );
        }
    }


    /**
     * Get a Random mutation point in the tree of a given type.
     * By default, the root node can be selected.
     * If this is not desired override this method.
     *
     * @param parent     The parent to sample from.
     * @param returnType The returnType the mutation point must assignable to.
     * @return The selected node, with a link to it's parent (unless it's the root node).
     */
    public static <T, Out, R> Optional<MutableTreeNode<T>> sampleMutationPointOfType(
            MutableNode<T, Out, ?, ?> parent,
            Class<R> returnType,
            RandomSource random
    ) throws NoSuchElementException {
        assert returnType != null : "Return type cannot be null";
        final List<MutableTreeNode<T>> candidates = MutableTreeNode
                .collect(parent)
                .stream()
                .filter(n -> returnType.isAssignableFrom(n.node().returnType()))
                .toList();
        if (candidates.isEmpty()) {
            logFailureToFindAnyNodeWithType(parent, returnType);
        }

        return RandomSampler
                .sample(candidates, random);
    }

    /**
     * Get a Random mutation point in the tree.
     * By default, the root node can be selected.
     * If this is not desired override this method.
     *
     * @param parent The parent to sample from.
     * @return The selected node, with a link to it's parent (unless it's the root node).
     */
    public static <T, Out> MutableTreeNode<T> sampleMutationPoint(
            MutableNode<T, Out, ?, ?> parent,
            RandomSource random
    ) {
        return RandomSampler
                .sampleOrThrow(MutableTreeNode.collect(parent), random);
    }

    /**
     * Get a Random mutation point in the tree of a given type.
     * By default, the root node can be selected.
     * If this is not desired override this method.
     *
     * @param parent     The parent to sample from.
     * @param returnType The returnType the mutation point must assignable to.
     * @return The selected node, with a link to it's parent (unless it's the root node).
     */
    public static <T, Out, R> Optional<ChildNode<T>> sampleNonRootMutationPointOfType(
            MutableNode<T, Out, ?, ?> parent,
            Class<R> returnType,
            RandomSource random
    ) throws NoSuchElementException {
        assert returnType != null : "Return type cannot be null";
        final List<ChildNode<T>> candidates = MutableTreeNode.collect(parent)
                .stream()
                .filter(n -> returnType.isAssignableFrom(n.node().returnType()))
                .map(n -> switch (n) {
                    case ChildNode<T> childNode -> childNode;
                    case RootNode<T> _ -> null;
                })
                .filter(Objects::nonNull)
                .toList();

        if (candidates.isEmpty()) {
            logFailureToFindChildNodeWithType(parent, returnType);
        }

        return RandomSampler
                .sample(candidates, random);
    }

    private static void logFailureToFindChildNodeWithType(
            Node<?, ?, ?, ?, ?> parent,
            Class<?> returnType
    ) {
        logger.atWarn()
                .setMessage("""
                        Crossover attempt failed. No non-root nodes that return a type assignable to {}
                        PARENT: {}"""
                )
                .addArgument(returnType)
                .addArgument(parent)
                .log();
    }
    private static void logFailureToFindAnyNodeWithType(
            Node<?, ?, ?, ?, ?> parent,
            Class<?> returnType
    ) {
        logger.atWarn()
                .setMessage("""
                        Crossover attempt failed. No nodes exist in parent that return a type assignable to {}
                        PARENT: {}"""
                )
                .addArgument(returnType)
                .addArgument(parent)
                .log();
    }

    /**
     * Get a Random mutation point in the tree.
     * By default, the root node can be selected.
     * If this is not desired override this method.
     *
     * @param parent The parent to sample from.
     * @return The selected node, with a link to it's parent.
     */
    public static <T, Out> ChildNode<T> sampleNonRootMutationPoint(
            MutableNode<T, Out, ?, ?> parent,
            RandomSource random
    ) {
        final List<ChildNode<T>> nonRootNodes = MutableTreeNode.collect(parent).stream()
                .map(n -> switch (n) {
                    case ChildNode<T> childNode -> childNode;
                    case RootNode<T> _ -> null;
                })
                .filter(Objects::nonNull)
                .toList();

        return RandomSampler.sampleOrThrow(nonRootNodes, random);
    }

    /**
     * Randomly crossover two trees inclusive of the of root
     * @param parent1 The first parent to crossover
     * @param parent2 The second parent to crossover
     * @param random The source of randomness to select the crossover points
     * @param postConditions The post conditions all the children must pass
     * @return Optional.empty if the crossover fails, otherwise the Optional.of the produced children
     * @param <T> The terminal type of the nodes
     * @param <Out> The return type of the nodes
     */
    static <T, Out> Optional<List<ImmutableNode<T, Out, ?, ?>>> performCrossOver(
            MutableNode<T, Out, ?, ?> parent1,
            MutableNode<T, Out, ?, ?> parent2,
            RandomSource random,
            Predicate<ImmutableNode<T, Out, ?, ?>> postConditions
    ) {
        final MutableTreeNode<T> mutationPoint1 = sampleMutationPoint(parent1, random);
        final Optional<List<ImmutableNode<T, Out, ?, ?>>> children = sampleMutationPointOfType(
                parent2,
                mutationPoint1.node().returnType(),
                random
        ).map(mutationPoint2 -> switch (Pair.of(mutationPoint1, mutationPoint2)) {
            case Pair(RootNode<T> _, RootNode<T> _) -> List.of(parent1.immutableCopy(), parent2.immutableCopy());
            case Pair(RootNode<T> _, ChildNode<T> c2) -> crossoverAtRoot(parent1, c2, parent2);
            case Pair(ChildNode<T> c1, RootNode<T> _) -> crossoverAtRoot(parent2, c1, parent1);
            case Pair(ChildNode<T> c1, ChildNode<T> c2) -> crossOverAtMutationPoints(parent1, parent2, c1, c2);
        });
        for (final ImmutableNode<T, Out, ?, ?> child : children.stream().flatMap(Collection::stream).toList()) {
            if (!postConditions.test(child)) {
                logPostConditionFailure(parent1, parent2, child, children.get());
                return Optional.empty();
            }
        }
        return children;
    }

    private static <T, Out> void logPostConditionFailure(
            MutableNode<T,Out,?,?> parent1,
            MutableNode<T,Out,?,?> parent2,
            ImmutableNode<T,Out,?,?> child,
            List<ImmutableNode<T,Out,?,?>> children
    ) {
        logger.atWarn()
                .addArgument(child)
                .addArgument(parent1)
                .addArgument(parent2)
                .addArgument(children)
                .log("""
                Crossover post conditions failed for child {}.
                PARENT 1: {}
                PARENT 2: {}
                ALL CHILDREN: {}
                """);
    }

    private static <Out, T> List<ImmutableNode<T, Out, ?, ?>> crossoverAtRoot(
            MutableNode<T, Out, ?, ?> rootNode,
            ChildNode<T> childNode,
            MutableNode<T,Out,?,?> rootOfChild
    ) {
        childNode.parent().replaceChild(childNode.indexInParent(), _ -> rootNode);
        return List.of(
                childNode.castNode(rootNode.returnType()).immutableCopy(),
                rootOfChild.immutableCopy()
        );
    }
}
