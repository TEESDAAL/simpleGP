package gp.impl.individual.testTree;

import gp.impl.individual.testTree.visitor.TreeVisitor;

import java.util.function.Function;
import java.util.stream.Stream;

public sealed interface TerminalNode<T, R, M extends Mutability> extends Node<
        T, R, M, Terminal
> permits MutableTerminal, ImmutableTerminal {
    Function<T, R> extractor();

    @Override
    default R evaluate(T terminals) {
        return this.extractor().apply(terminals);
    }

    @Override
    default MutableTerminal<T, R> mutableCopy() {
        return new MutableTerminal<>(
                this.name(),
                this.extractor(),
                this.returnType()
        );
    }

    @Override
    default Stream<Node<T, ?, M, ?>> stream() {
        return Stream.of(this);
    }

    @Override
    default <U> U visit(TreeVisitor<T, R, U, M> visitor) {
        return visitor.terminal(this);
    }
}
