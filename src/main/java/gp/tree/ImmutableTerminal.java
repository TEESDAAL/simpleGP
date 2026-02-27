package gp.tree;

import gp.utils.UnaryOperator;

import java.util.HashMap;
import java.util.Map;

/**
 * An immutable terminal node record implementation.
 * Each terminal follows the flyweight pattern to
 * ensure that identical terminals are shared.
 * @param <Terminals> The terminal type
 * @param <Output> The output type
 * @param extractor The function to extract values from terminals
 * @param returnType The output type
 */
public record ImmutableTerminal<
        Terminals,
        Output
>(
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
    > CACHE = new HashMap<>();

    /**
     * Creates or retrieves a cached immutable terminal.
     * @param <Terminals> The terminal type
     * @param <Output> The output type
     * @param extractor The extractor function
     * @param returnType The output type class
     * @return A cached immutable terminal
     */
    public static <Terminals, Output>
            ImmutableTerminal<Terminals, Output> of(
            final UnaryOperator<Terminals, Output> extractor,
            final Class<Output> returnType
    ) {
        ImmutableTerminal<Terminals, Output> term = new ImmutableTerminal<>(
                extractor, returnType
        );

        //noinspection unchecked
        return (ImmutableTerminal<Terminals, Output>)
                CACHE.computeIfAbsent(term, k -> term);
    }
}
