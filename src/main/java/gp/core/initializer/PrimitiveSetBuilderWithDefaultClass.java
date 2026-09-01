package gp.core.initializer;

import gp.impl.individual.typed_tree.NamedNodeFunction;
import util.QuadFunction;
import util.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PrimitiveSetBuilderWithDefaultClass<T, R> implements PrimitiveSetBuilder<
        T,
        PrimitiveSetBuilderWithDefaultClass<T, R>
> {

    private final PrimitiveSetBuilder<T, ?> innerBuilder;
    private final Class<R> defaultType;

    PrimitiveSetBuilderWithDefaultClass(
            PrimitiveSetBuilder<T, ?> innerBuilder,
            Class<R> defaultType
    ) {
        this.innerBuilder = innerBuilder;
        this.defaultType = defaultType;
    }

    public PrimitiveSetBuilderWithDefaultClass<T, R> addTerminal(
            String name,
            Function<T, R> terminalFunction
    ) {
        return this.addTerminal(TypedTerminal.of(
                name,
                terminalFunction,
                defaultType
        ));
    }

    @Override
    public PrimitiveSetBuilderWithDefaultClass<T, R> self() {
        return this;
    }

    @Override
    public PrimitiveSetBuilderWithDefaultClass<T, R> addTerminal(
            TypedTerminal<T, ?> terminal
    ) {
        innerBuilder.addTerminal(terminal);
        return this;
    }

    public PrimitiveSetBuilderWithDefaultClass<T, R> addUnaryFunction(
            String name,
            Function<R, R> nonTerminal
    ) {
        return this.addNonTerminal(
                name,
                nonTerminal,
                defaultType,
                defaultType
        );
    }

    public PrimitiveSetBuilderWithDefaultClass<T, R> addBinaryFunction(
            String name,
            BiFunction<R, R, R> nonTerminal
    ) {
        return this.addNonTerminal(
                name,
                nonTerminal,
                defaultType,
                defaultType,
                defaultType
        );
    }

    public PrimitiveSetBuilderWithDefaultClass<T, R> addTrinaryFunction(
            String name,
            TriFunction<R, R, R, R> nonTerminal
    ) {
        return this.addNonTerminal(
                name,
                nonTerminal,
                defaultType,
                defaultType,
                defaultType,
                defaultType
        );
    }

    public PrimitiveSetBuilderWithDefaultClass<T, R> addQuadFunction(
            String name,
            QuadFunction<R, R, R, R, R> nonTerminal
    ) {
        return this.addNonTerminal(
                name,
                nonTerminal,
                defaultType,
                defaultType,
                defaultType,
                defaultType,
                defaultType
        );
    }

    @Override
    public PrimitiveSetBuilderWithDefaultClass<T, R> addNonTerminal(
            NamedNodeFunction<?, ?> nonTerminal
    ) {
        innerBuilder.addNonTerminal(nonTerminal);
        return this;
    }

    @Override
    public <C> PrimitiveSetBuilderWithDefaultClass<T, R> addEphemeralConstant(
            EphemeralConstant<C> ephemeralConstant
    ) {
        innerBuilder.addEphemeralConstant(ephemeralConstant);
        return this;
    }

    @Override
    public PrimitiveSet<T> build() {
        return innerBuilder.build();
    }
}
