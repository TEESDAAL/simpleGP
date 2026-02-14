package gp.genetic_operators;

import gp.initializers.BaseInitializer;
import gp.initializers.OperatorSelector;
import gp.initializers.TypedNonTerminal;
import gp.initializers.TypedTerminal;
import gp.random.RandomSampler;
import gp.tree.*;
import gp.utils.UnaryOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record SubtreeMutation<T, Out>(
        Random random,
        List<TypedTerminal<T, ?>> terminals,
        List<TypedNonTerminal<?, ?>> nonTerminals,
        int maxDepth,
        int maxTries
        ) implements UnaryOperator<Node<T, ?, Out, ?, ?>, ImmutableNode<T, ?, Out, ?, ?>> {

    public static <T, Out> SubtreeMutation<T, Out> of(
            Random random,
            List<TypedTerminal<T, ?>> terminals,
            List<TypedNonTerminal<?, ?>> nonTerminals,
            int maxDepth,
            int maxTries
    ) {
        return new SubtreeMutation<>(random, terminals, nonTerminals, maxDepth, maxTries);
    }

    @Override
    @SuppressWarnings("unchecked") // Type erasure :(
    public ImmutableNode<T, ?, Out, ?, ?> produce(Node<T, ?, Out, ?, ?> root) {
        return switch (root) {
            case Terminal<?, ?> term -> {
                Terminal<T, Out> actualTermTypes = (Terminal<T, Out>) term;
                yield randomTerminal(actualTermTypes.returnType());
            }
            case NonTerminal<?, ?, ?, ?> nonTerminal -> {
                NonTerminal<T, ?, Out, ?> actualNonTerminalTypes = (NonTerminal<T, ?, Out, ?>) nonTerminal;
                yield replaceChild(actualNonTerminalTypes.mutableCopy());
            }
        };
    }

    @SuppressWarnings("unchecked")
    private <MutationPointInputType> ImmutableNonTerminal<T,?,Out> replaceChild(MutableNonTerminal<T,?,Out> root) {
        List<MutableNonTerminal<T, ?, ?>> nonTerminals = new ArrayList<>();
        for (Node<T, ?, ?, ?, ?> node : root.stream().toList()) {
            if (node instanceof MutableNonTerminal<?, ?, ?> nonTerm) {
                nonTerminals.add((MutableNonTerminal<T, ?, ?>) nonTerm);
            }
        }

        MutableNonTerminal<T, MutationPointInputType, ?> mutationPoint = (MutableNonTerminal<T, MutationPointInputType, ?>) RandomSampler.sample(
                nonTerminals, random
        ).orElseThrow(() -> new IllegalStateException("Tree somehow has no nodes?"));
        int depthOfMutationPoint = root.depth() - mutationPoint.depth();
        MutableNode<T, ?, MutationPointInputType, ?, ?> subTree = this.createSubTree(
                maxDepth - depthOfMutationPoint,
                mutationPoint.inputType()
        );

        mutationPoint.replaceChild(
                RandomSampler.sampleIndex(mutationPoint.children(), random).orElseThrow(),
                subTree
        );
        return root.immutableCopy();
    }


    private <OutputType> MutableNode<T, ?, OutputType, ?, ?> createSubTree(
            int maxDepth, Class<OutputType> returnType
    ) {
        return BaseInitializer.grow(
                random, terminals, nonTerminals, 1,
                maxDepth, maxTries, returnType
        ).createIndividual().mutableCopy();
    }


    private <R> ImmutableTerminal<T, R> randomTerminal(Class<R> returnType) {
        return RandomSampler.sample(
                OperatorSelector.validTerminals(this.terminals, returnType),
                this.random()
        ).map(term -> Node.term(term.terminal(), term.returnType()))
                .orElseThrow(() -> new IllegalStateException(
                        "Should be impossible as you can always reselect the same terminal"
                ));
    }
}
