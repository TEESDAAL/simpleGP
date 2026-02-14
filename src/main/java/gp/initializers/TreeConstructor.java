package gp.initializers;

import gp.random.RandomSampler;
import gp.tree.ImmutableNode;
import gp.tree.Node;
import gp.utils.*;


import java.util.*;

public interface TreeConstructor<T, R> extends IndividualInitializer<ImmutableNode<T, ?, R, ?, ?>> {
    Random random();
    List<TypedTerminal<T, ?>> terminals();
    List<TypedNonTerminal<?, ?>> nonTerminals();
    boolean shouldTerminate(int depth);

    default <I, ReturnType> Optional<ImmutableNode<T, ?, ReturnType, ?, ?>> recursivelyConstructIndividual(int currentDepth, Class<ReturnType> returnType) {
        if (shouldTerminate(currentDepth)) {
            return RandomSampler.sample(
                    OperatorSelector.validTerminals(this.terminals(), returnType),
                    this.random()
            ).map(term -> Node.term(term.terminal(), term.returnType()));
        }


        Optional<TypedNonTerminal<?, ReturnType>> potentialNonTerminal = RandomSampler.sample(
                OperatorSelector.validNonTerminals(this.nonTerminals(), returnType),
                this.random()
        );

        if (potentialNonTerminal.isEmpty()) {
            return Optional.empty();
        }

        @SuppressWarnings("unchecked") // Add type I so we can talk about it later...
        TypedNonTerminal<I, ReturnType> nonTerminal = (TypedNonTerminal<I, ReturnType>) potentialNonTerminal.get();
        List<ImmutableNode<T, ?, I, ?, ?>> children = new ArrayList<>();

        for (int i=0; i< nonTerminals().size(); i++) {
            Optional<ImmutableNode<T, ?, I, ?, ?>> child = recursivelyConstructIndividual(
                    currentDepth + 1,
                    nonTerminal.inputType()
            );
            if (child.isEmpty()) {
                return Optional.empty();
            }
            children.add(child.get());
        }

        return Optional.of(Node.nonTerm(
                nonTerminal.nonTerminal(),
                Collections.unmodifiableList(children),
                nonTerminal.inputType(),
                nonTerminal.returnType()
        ));
    }


}

