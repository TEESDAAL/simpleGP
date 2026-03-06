package gp.tree;

import gp.utils.operators.UnaryOperator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An immutable terminal node record implementation.
 * Each terminal follows the flyweight pattern to
 * ensure that identical terminals are shared.
 * @param <Terminals> The terminal type
 * @param <Output> The output type
 * @param name The name of this terminal
 * @param extractor The function to extract values from terminals
 * @param returnType The output type
 */
public record ImmutableTerminal<Terminals, Output>(
        String name,
        UnaryOperator<Terminals, Output> extractor,
        Class<Output> returnType
) implements
        Terminal<Terminals, Output>,
        ImmutableNode<
                Terminals, Terminals, Output,
                ImmutableTerminal<Terminals, Output>,
                MutableTerminal<Terminals, Output>
        > {
    /** Cache for flyweight pattern. */
    private static final Map<
            ImmutableTerminal<?, ?>,
            ImmutableTerminal<?, ?>
    > CACHE = new ConcurrentHashMap<>();

    /**
     * Creates or retrieves a cached immutable terminal.
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
                name, extractor, returnType
        );

        //noinspection unchecked
        return (ImmutableTerminal<Terminals, Output>)
                CACHE.computeIfAbsent(term, k -> term);
    }
}
