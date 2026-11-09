package gp.impl.individual.testTree;

import gp.impl.individual.testTree.visitor.TreeVisitor;
import util.Updater;

import java.util.function.Function;

public final class MutableTerminal<T, R> implements MutableNode<
        T, R, Terminal
>, TerminalNode<T, R, Mutable> {
    String name;
    Function<T, R> extractor;
    Class<R> returnType;

    public MutableTerminal(
            String name,
            Function<T, R> extractor,
            Class<R> returnType
    ) {
        this.name = name;
        this.extractor = extractor;
        this.returnType = returnType;
    }

    @Override
    public String name() {
        return this.name;
    }

    public MutableTerminal<T, R> setName(Updater<String> updater) {
        this.name = updater.newValue(this.name);
        return this;
    }

    @Override
    public Function<T, R> extractor() {
        return this.extractor;
    }

    public MutableTerminal<T, R> setExtractor(
            Updater<Function<T, R>> updater
    ) {
        this.extractor = updater.newValue(this.extractor);
        return this;
    }


    @Override
    public ImmutableTerminal<T, R> immutableCopy() {
        return new ImmutableTerminal<>(
                this.name(),
                this.extractor(),
                this.returnType()
        );
    }

    @Override
    public <U> U visit(TreeVisitor<T, R, U> visitor) {
        return visitor.mutableTerminal(this);
    }

    @Override
    public Class<R> returnType() {
        return this.returnType;
    }

    @Override
    public R evaluate(T terminals) {
        return this.extractor().apply(terminals);
    }
}
