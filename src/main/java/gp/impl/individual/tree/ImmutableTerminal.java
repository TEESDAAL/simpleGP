package gp.impl.individual.tree;

import utils.operators.UnaryOperator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An immutable terminal node record implementation.
 * Each terminal follows the flyweight pattern to
 * ensure that identical terminals are shared.
 * @param <Terminals> The terminal type
 * @param <Output> The output type
 */
public final class ImmutableTerminal<Terminals, Output> implements
    Terminal<Terminals, Output>,
    ImmutableNode<
            Terminals, Terminals, Output,
            ImmutableTerminal<Terminals, Output>,
            MutableTerminal<Terminals, Output>
    > {
    final String name;
    final UnaryOperator<Terminals, Output> extractor;
    final Class<Output> returnType;
    final boolean cached;

    private ImmutableTerminal(
        String name,
        UnaryOperator<Terminals, Output> extractor,
        Class<Output> returnType,
        boolean cached
    ) {
        this.name = name;
        this.extractor = extractor;
        this.returnType = returnType;
        this.cached = cached;
    }

    /** Cache for flyweight pattern. */
    private static final Map<
            ImmutableTerminal<?, ?>,
            ImmutableTerminal<?, ?>
    > CACHE = new ConcurrentHashMap<>();

    @Override
    public UnaryOperator<Terminals, Output> extractor() {
        return this.extractor;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Class<Output> returnType() {
        return this.returnType;
    }

    /**
     * Creates or retrieves a singleton immutable terminal.
     * @param <Terminals> The terminal type
     * @param <Output> The output type
     * @param extractor The extractor function
     * @param returnType The output type class
     * @return A cached immutable terminal
     */
    public static <Terminals, Output> ImmutableTerminal<Terminals, Output> of(
            final String name,
            final UnaryOperator<Terminals, Output> extractor,
            final Class<Output> returnType
    ) {
        ImmutableTerminal<Terminals, Output> term = new ImmutableTerminal<>(
                name, extractor, returnType, true
        );

        //noinspection unchecked
        return (ImmutableTerminal<Terminals, Output>)
                CACHE.computeIfAbsent(term, k -> term);
    }


}
