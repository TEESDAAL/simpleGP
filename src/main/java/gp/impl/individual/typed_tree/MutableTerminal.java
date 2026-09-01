package gp.impl.individual.typed_tree;

import util.Updater;

import java.util.function.Function;

public class MutableTerminal<Terminals, R>
        implements Terminal<
        Terminals, R,
        MutableTerminal<Terminals, R>
>, MutableNode<
        Terminals, R,
        MutableTerminal<Terminals, R>,
        ImmutableTerminal<Terminals, R>
> {
    String name;
    Function<Terminals, R> extractor;
    Class<R> returnType;

    public MutableTerminal(
            String name,
            Function<Terminals, R> extractor,
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

    public MutableTerminal<Terminals, R> setName(Updater<String> updater) {
        this.name = updater.newValue(this.name);
        return this;
    }

    @Override
    public Function<Terminals, R> extractor() {
        return this.extractor;
    }

    public MutableTerminal<Terminals, R> setExtractor(
            Updater<Function<Terminals, R>> updater
    ) {
        this.extractor = updater.newValue(this.extractor);
        return this;
    }


    @Override
    public ImmutableTerminal<Terminals, R> immutableCopy() {
        return new ImmutableTerminal<>(
                this.name(),
                this.extractor(),
                this.returnType()
        );
    }

    @Override
    public Class<R> returnType() {
        return this.returnType;
    }

    @Override
    public MutableTerminal<Terminals, R> self() {
        return this;
    }

    @Override
    public R evaluate(Terminals terminals) {
        return this.extractor().apply(terminals);
    }
}
