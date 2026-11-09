package gp.impl.individual;

import gp.impl.individual.typed_tree.*;
import gp.impl.selector.random.RandomSampler;
import util.Repeat;
import util.random.RandomSource;
import util.result.Err;
import util.result.Ok;
import util.result.Result;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public enum SelectionMethod implements NodeSelector {
    INCLUDE_ROOT {
        @Override
        protected <T> Stream<ImmutableNode<T,?,?,?>> flattenTree(ImmutableNode<T,?,?,?> rootNode) {
            return rootNode.stream().map(Node::recover);
        }
    },
    EXCLUDE_ROOT {
        @Override
        protected <T> Stream<ImmutableNode<T,?,?,?>> flattenTree(ImmutableNode<T,?,?,?> rootNode) {
            return rootNode.visit(new TreeVisitor<>() {
                @Override
                public Stream<ImmutableNode<T,?,?,?>> nonTerminal(NonTerminal<T, ?, ?, ?, ?, ?> nonTerm) {
                    return nonTerm.children().stream().flatMap(Node::stream);
                }

                @Override
                public Stream<ImmutableNode<T,?,?,?>> terminal(Terminal<T, ?, ?> term) {
                    return Stream.empty();
                }
            });
        }
    };

    protected abstract <T, M extends Node<T, ?, M, ?, ?, ?>> Stream<M> flattenTree(
            Node<T,?,M,?,?,?> rootNode
    );

    @Override
    public <T, M extends Node<T, ?, M, ?, ?, ?>> ImmutableNode<T,?,?,?> sampleNodeOrThrow(RandomSource random, ImmutableNode<T,?,?,?> rootNode) {
        final int size = rootNode.size();
        final Iterator<ImmutableNode<T,?,?,?>> iterator = rootNode.stream().iterator();
        Repeat.of(random.nextInt(0, size - 1), iterator::next);

        return RandomSampler.sample(random, flattenTree(rootNode).toList());
    }

    @Override
    public <T, R> Node<T, R, ?, ?, ?> sampleNodeOfTypeOrThrow(
            RandomSource random,
            ImmutableNode<T,?,?,?> rootNode,
            Class<R> returnType
    ) {
        return RandomSampler.sample(
                random,
                flattenTree(rootNode)
                        .map(n -> n.returns(returnType).orElse(null))
                        .filter(Objects::nonNull)
                        .toList()
        ).map(i -> i);
    }

    @Override
    public <T> Terminal<T, ?, ?> sampleTerminalOrThrow(RandomSource random, ImmutableNode<T,?,?,?> rootNode) {
        return sampleTerminal(random, rootNode)
                .orElseThrow(); // all nodes should have at least one terminal.
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, R> Optional<Terminal<T, R, ?>> sampleTerminalOfTypeOrThrow(RandomSource random, ImmutableNode<T,?,?,?> rootNode, Class<R> returnType) {
        return RandomSampler.sample(
                random,
                flattenTree(rootNode)
                        .map(n -> Result.fromOpt(n.returns(returnType)))
                        .map(n -> switch (n) {
                            case Ok(Terminal<?, ?, ?> t) -> (Terminal<T, R, ?>) t;
                            case Ok(NonTerminal<?, ?, ?, ?, ?, ?> _), Err(_) -> null;
                        })
                        .filter(Objects::nonNull)
                        .toList()
        ).map(i -> i);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> NonTerminal<T, ?, ?, ?, ?, ?> sampleNonTerminalOrThrow(RandomSource random, ImmutableNode<T,?,?,?> rootNode) {
        return RandomSampler.sample(
                random,
                flattenTree(rootNode)
                        .filter(n -> n instanceof NonTerminal<?,?,?,?,?,?>)
                        .map(n -> (NonTerminal<T, ?, ?, ?, ?, ?>) n)
                        .toList()
        ).orElseThrow(() -> new NoSuchElementException(
                "There are no valid nodes to sample from."
        ));
    }

    @Override
    public <T, R> NonTerminal<T, R, ?, ?, ?, ?> sampleNonTerminalOfTypeOrThrow(RandomSource random, ImmutableNode<T,?,?,?> rootNode, Class<R> returnType) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<ImmutableNode<T,?,?,?>> sampleNode(RandomSource random, ImmutableNode<T,?,?,?> rootNode) throws Exception {
        return Optional.empty();
    }

    @Override
    public <T, R> Optional<Node<T, R, ?, ?, ?>> sampleNodeOfType(RandomSource random, ImmutableNode<T,?,?,?> rootNode, Class<R> returnType) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<Terminal<T, ?, ?>> sampleTerminal(RandomSource random, ImmutableNode<T,?,?,?> rootNode) {
        return Optional.empty();
    }

    @Override
    public <T, R> Optional<Terminal<T, R, ?>> sampleTerminalOfType(RandomSource random, ImmutableNode<T,?,?,?> rootNode, Class<R> returnType) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<NonTerminal<T, ?, ?, ?, ?, ?>> sampleNonTerminal(RandomSource random, ImmutableNode<T,?,?,?> rootNode) {
        return Optional.empty();
    }

    @Override
    public <T, R> Optional<NonTerminal<T, R, ?, ?, ?, ?>> sampleNonTerminalOfType(RandomSource random, ImmutableNode<T,?,?,?> rootNode, Class<R> returnType) {
        return Optional.empty();
    }

}
