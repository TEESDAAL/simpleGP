package gp.impl.genetic_operator.mutation;

import gp.core.individual.Individual;
import gp.core.primitive_set.PrimitiveSet;
import gp.impl.genetic_operator.UnaryGeneticOperator;
import gp.impl.individual.NodeSelector;
import gp.impl.individual.SelectionMethod;
import gp.impl.individual.typed_tree.*;
import gp.impl.initializer.NodeInitialiser;
import gp.impl.selector.random.RandomSampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Repeat;
import util.Updater;
import util.random.RandomSource;
import util.sequence.Single;

import java.util.*;

/**
 * Subtree mutation operator that replaces a subtree
 * with a new randomly generated one. Guaranteed to
 *
 */
public abstract class SubtreeMutation<
        T, Out,
        P extends Individual<T, Out>,
        I extends Individual<T, Out>,
        Self extends SubtreeMutation<T, Out, P, I, Self>
> extends UnaryGeneticOperator<
        T, Out,
        P,
        Node<T, Out, ?, ?, ?, ?>,
        ImmutableNode<T, Out, ?, ?>,
        I,
        Self
> {
    private static final Logger logger = LoggerFactory.getLogger(SubtreeMutation.class);
    private final RandomSource random;
    private final PrimitiveSet<T> primitiveSet;
    private final int depthLimit;
    private final int attemptLimit;

    protected NodeSelector selectionMethod() {
        return SelectionMethod.INCLUDE_ROOT;
    }
    /**
     * @param random       The random number generator
     * @param primitiveSet The set of terminals and non-terminals.
     * @param depthLimit   Maximum depth for generated subtrees
     * @param attemptLimit Maximum attempts to generate a valid subtree
     */
    public SubtreeMutation(
            RandomSource random,
            PrimitiveSet<T> primitiveSet,
            int depthLimit,
            int attemptLimit
    ) {
        this.random = random;
        this.primitiveSet = primitiveSet;
        this.depthLimit = depthLimit;
        this.attemptLimit = attemptLimit;
    }


    /**
     * Applies the mutation operator to a node.
     *
     * @param root The root node to mutate
     * @return A mutated immutable copy of the tree
     */
    @Override
    @SuppressWarnings("unchecked") // Type erasure :(
    public Single<ImmutableNode<T, Out, ?, ?>> produceUntracked(
            final Node<T, Out, ?, ?, ?, ?> root
    ) {
        final ImmutableNode<T, Out, ?, ?> immRoot = root.immutableCopy();
        return Repeat.<ImmutableNode<T, Out, ?, ?>>untilSuccess(
                attemptLimit,
                _ -> switch (immRoot) {
                    case ImmutableTerminal<?, ?> term -> this.selectionMethod()
                            .sampleTerminalOfType(
                                    this.random,
                                    immRoot,
                                    (Class<Out>) term.returnType()
                            ).map(Terminal::recoverMutability);
                    case ImmutableNonTerminal<?, ?, ?, ?> nonTerminal -> mutateSubTree(
                            (ImmutableNonTerminal<T, Out, ?, ?>) nonTerminal
                    );
                }
        )
                .map(Single::of)
                .orElseGet(() -> Single.of(onFailure(immRoot)));
    }

    protected Single<ImmutableNode<T, Out, ?, ?>> onFailure(
            ImmutableNode<T, Out, ?, ?> immRoot
    ) {
        return Single.of(immRoot);
    }

    // Another type erasure L
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Optional<ImmutableNode<T, Out, ?, ?>> mutateSubTree(
            final ImmutableNonTerminal<T, Out, ?, ?> root
    ) {
        final boolean shouldMutateRoot = random.nextInt(0, root.size()) == 0;
        if (shouldMutateRoot) {
            return this.createSubTree(depthLimit, root.returnType());
        }
        // Select a random non-terminal node to mutate
        final List<MutableNonTerminal> nonTerminals = root.mutableCopy().stream()
                .filter(n -> n instanceof MutableNonTerminal)
                .map(n -> (MutableNonTerminal) n)
                .toList();

        final MutableNonTerminal mutationPoint = RandomSampler.sampleOrThrow(
                nonTerminals, random
        );

        final int depthOfMutationPoint = root.depth() - mutationPoint.depth();
        // Unsure why this needs to be a variable and can't be inlined :(
        final Updater<MutableNode<T, ?, ?, ?>> updater = t -> createSubTree(
                depthLimit - depthOfMutationPoint,
                t.returnType()
        ).orElseThrow().mutableCopy();

        try {
            mutationPoint.replaceChild(
                    random.nextInt(0, mutationPoint.combiner().arity()),
                    updater
            );
            return Optional.of(root);
        } catch (NoSuchElementException _) {
            return Optional.empty();
        }
    }


    private <OutputType> Optional<ImmutableNode<T, OutputType, ?, ?>> createSubTree(
            final int maxDepthParam,
            final Class<OutputType> returnType
    ) {
        return NodeInitialiser.grow(
                random, primitiveSet, 1,
                attemptLimit, maxDepthParam, Objects.requireNonNull(returnType)
        ).recursivelyConstructIndividual(0, returnType);
    }



    public final PrimitiveSet<T> primitiveSet() {
        return primitiveSet;
    }

    public final int depthLimit() {
        return depthLimit;
    }

    public final int attemptLimit() {
        return attemptLimit;
    }

    @Override
    public String toString() {
        return "SubtreeMutation[" +
                "primitiveSet=" + primitiveSet + ", " +
                "depthLimit=" + depthLimit + ", " +
                "attemptLimit=" + attemptLimit + ']';
    }

}


