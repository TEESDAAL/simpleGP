package gp.initializers;

import gp.tree.ImmutableNode;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.IntPredicate;

public record BaseInitializer<T, R>(
        Random random,
        List<TypedTerminal<T, ?>> terminals,
        List<TypedNonTerminal<?, ?>> nonTerminals,
        IntPredicate shouldTerminate,
        int populationSize,
        int maxTries,
        int maxDepth,
        Class<R> returnType
) implements TreeConstructor<T, R> {

    public static <T, R> BaseInitializer<T, R> grow(
            Random random,
            List<TypedTerminal<T, ?>> terminals,
            List<TypedNonTerminal<?, ?>> nonTerminals,
            int populationSize,
            int maxTries,
            int maxDepth,
            Class<R> returnType
    ) {
        double probabilityOfSamplingTerminal = terminals.size() / ((double) terminals.size() + nonTerminals.size());
        return new BaseInitializer<>(
                random,  terminals, nonTerminals,
                depth -> depth >= maxDepth || random.nextDouble() < probabilityOfSamplingTerminal,
                populationSize, maxTries, maxDepth,
                returnType
        );
    }

    public static <T, R> BaseInitializer<T, R> full(
            Random random,
            List<TypedTerminal<T, ?>> terminals,
            List<TypedNonTerminal<?, ?>> nonTerminals,
            int populationSize,
            int maxTries,
            int maxDepth,
            Class<R> returnType
    ) {
        return new BaseInitializer<T, R>(
                random,  terminals, nonTerminals,
                depth -> depth >= maxDepth,
                populationSize, maxTries, maxDepth,
                returnType
        );
    }

    @Override
    public boolean shouldTerminate(int depth) {
        return shouldTerminate.test(depth);
    }

    @Override
    public ImmutableNode<T, ?, R, ?, ?> createIndividual() throws IndividualCreationException {
        for (int i=0; i<maxTries; i++) {
            Optional<ImmutableNode<T, ?, R, ?, ?>> possibleTree = this.recursivelyConstructIndividual(
                0, returnType
            );
            if (possibleTree.isPresent()) {return possibleTree.get();}
        }
        throw new IndividualCreationException(
                "Failed to create an individual after " + maxTries + " attemps."
                        + "\n Check that this is possible with the given terminals & non-terminals"
                        + "\n NON-TERMINALS: " + nonTerminals
                        + "\n TERMINALS: " + terminals
        );
    }

    @Override
    public boolean shouldParallelize() {
        return true;
    }
}


