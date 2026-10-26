package gp.impl.genetic_operator;

import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Preconditions;
import util.operator.BinaryOperator;
import util.random.RandomSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * A crossover operator that takes in two trees and performs random subtree crossover.
 * Includes crossover of the root of the tree too.
 */
// Sealed interface with private impl
public final class Crossover<T, Out> implements BinaryOperator<
        Node<T, Out, ?, ?, ?>,
        List<ImmutableNode<T, Out, ?, ?>>
> {
    final int maxDepth;
    final int maxTries;
    final RandomSource random;
    private static final Logger logger = LoggerFactory.getLogger(Crossover.class);

    public Crossover(int maxTries, int maxDepth, RandomSource random) {
        Preconditions.assertTrue(
                maxDepth >= 1,
                "The max Depth has to include at least 1 node."
        );
        Preconditions.assertTrue(
                maxTries >= 1,
                "We must be able to try to produce an individual at least 1 time."
        );
        Preconditions.allNonNull(Map.of("random", random));
        this.maxDepth = maxDepth;
        this.maxTries = maxTries;
        this.random = random;
    }

    @Override
    public List<ImmutableNode<T, Out, ?, ?>> produce(
            Node<T, Out, ?, ?, ?> parent1,
            Node<T, Out, ?, ?, ?> parent2
    ) {
        for (int i = 0; i < maxTries; i++) {
            final Optional<List<ImmutableNode<T, Out, ?, ?>>> children = CrossoverUtils.performCrossOver(
                    parent1.mutableCopy(),
                    parent2.mutableCopy(),
                    random,
                    n -> n.depth() <= maxDepth
            );
            if (children.isPresent()) {
                return children.get();
            }
        }

        logMaxAttemptsReached(parent1, parent2, maxTries);

        return List.of(
                parent1.immutableCopy(),
                parent2.immutableCopy()
        );
    }


    private static void logMaxAttemptsReached(
            Node<?, ?, ?, ?, ?> parent1,
            Node<?, ?, ?, ?, ?> parent2,
            int maxTries
    ) {
        logger.atError().log("""
                        Max attempts reached when trying to crossover:
                        PARENT 1: {}
                        PARENT 2: {}
                        Tried {} times""",
                parent1, parent2, maxTries
        );
    }


    @Override
    public String toString() {
        return "Crossover["
                + "random=" + random + ", "
                + "maxDepth=" + maxDepth + ", "
                + "maxTries=" + maxTries + "]";
    }
}
