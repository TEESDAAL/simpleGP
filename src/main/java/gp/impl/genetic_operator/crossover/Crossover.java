package gp.impl.genetic_operator.crossover;

import gp.core.individual.Individual;
import gp.impl.genetic_operator.BinaryGeneticOperator;
import gp.impl.individual.typed_tree.ImmutableNode;
import gp.impl.individual.typed_tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Preconditions;
import util.random.RandomSource;
import util.sequence.Pair;

import java.util.Map;
import java.util.Optional;

public abstract class Crossover<
        T, Out,
        P extends Individual<T, Out>,
        I extends Individual<T, Out>,
        Self extends Crossover<T, Out, P, I, Self>
> extends BinaryGeneticOperator<
        T, Out,
        P,
        Node<T, Out, ?, ?, ?, ?>,
        ImmutableNode<T, Out, ?, ?>,
        I,
        Self
> {
    private static final Logger logger = LoggerFactory.getLogger(Crossover.class);
    final int maxDepth;
    final int maxTries;
    final RandomSource random;

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


    /// JOE: Ask what Joe thinks about this...
    @Override
    protected final Self self() {
        //noinspection unchecked
        return (Self) this;
    }

    @Override
    public Pair<ImmutableNode<T, Out, ?, ?>> produceUntracked(
            Node<T, Out, ?, ?, ?, ?> parent1,
            Node<T, Out, ?, ?, ?, ?> parent2
    ) {
        for (int i = 0; i < maxTries; i++) {
            final Optional<Pair<ImmutableNode<T, Out, ?, ?>>> children = CrossoverUtils.performCrossOver(
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

        return onFailure(parent1, parent2);
    }

    protected Pair<ImmutableNode<T, Out, ?, ?>> onFailure(
            Node<T, Out, ?, ?, ?, ?> parent1,
            Node<T, Out, ?, ?, ?, ?> parent2
    ) {
        Pair.of(
                parent1.immutableCopy(),
                parent2.immutableCopy()
        );
    }

    private static void logMaxAttemptsReached(
            Node<?, ?, ?, ?, ?, ?> parent1,
            Node<?, ?, ?, ?, ?, ?> parent2,
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
                + "maxDepth=" + maxDepth + ", "
                + "maxTries=" + maxTries + "]";
    }
}

