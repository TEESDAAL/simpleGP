package gp.impl.individual.testTree;

import gp.impl.individual.testTree.visitor.TreeVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class ImmutableTerminal<T, R> implements ImmutableNode<
        T, R, Terminal
>, TerminalNode<T, R, Immutable> {
    @SuppressWarnings("rawtypes")
    private static final Map<ImmutableTerminal, ImmutableTerminal> CACHE = new HashMap<>();
    private final String name;
    final Function<T, R> extractor;
    final Class<R> returnType;

    ImmutableTerminal(
            String name,
            Function<T, R> extractor,
            Class<R> returnType
    ) {
        this.name = name;
        this.extractor = extractor;
        this.returnType = returnType;
    }

    public static <T, R> ImmutableTerminal<T, R> of(String name, Function<T, R> extractor, Class<R> returnType) {
        final ImmutableTerminal<T, R> key = new ImmutableTerminal<>(name, extractor, returnType);
        //noinspection unchecked
        return (ImmutableTerminal<T, R>) CACHE.computeIfAbsent(
                key,
                (k) -> k
        );
    }

    @Override
    public Function<T, R> extractor() {
        return extractor;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<R> returnType() {
        return returnType;
    }

    @Override
    public <U> U visit(TreeVisitor<T, R, U> visitor) {
        return visitor.immutableTerminal(this);
    }
}
