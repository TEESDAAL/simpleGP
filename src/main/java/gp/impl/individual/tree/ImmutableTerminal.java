package gp.impl.individual.tree;

import utils.operators.UnaryOperator;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An immutable terminal node record implementation.
 * Each terminal follows the flyweight pattern to
 * ensure that identical terminals are shared.
 * A class to allow for the hiding the constructor.
 * @param <Terminals> The terminal type
 * @param <Output> The output type
 */
public final class ImmutableTerminal<Terminals, Output> extends Terminal<Terminals, Output> implements
    ImmutableNode<
            Terminals, Terminals, Output,
            ImmutableTerminal<Terminals, Output>,
            MutableTerminal<Terminals, Output>
    > {
    private ImmutableTerminal(
        String name,
        UnaryOperator<Terminals, Output> extractor,
        Class<Output> returnType
    ) {
        super(name, extractor, returnType);
    }

    /** Cache for flyweight pattern. */
    private static final Map<
            ImmutableTerminal<?, ?>,
            ImmutableTerminal<?, ?>
    > CACHE = new ConcurrentHashMap<>();

    /**
     * Creates or retrieves a singleton immutable terminal.
     * @param <Terminals> The terminal type
     * @param <Output> The output type
     * @param name The name of this terminal
     * @param extractor The extractor function
     * @param returnType The output type class
     * @return A cached immutable terminal
     */
    public static <Terminals, Output> ImmutableTerminal<Terminals, Output> of(
            final String name,
            final UnaryOperator<Terminals, Output> extractor,
            final Class<Output> returnType
    ) {
        final ImmutableTerminal<Terminals, Output> term = new ImmutableTerminal<>(
                name, extractor, returnType
        );

        //noinspection unchecked
        return (ImmutableTerminal<Terminals, Output>)
                CACHE.computeIfAbsent(term, k -> term);
    }

    @Override
    public String toString() {
        return "ImmutableTerminal[name="
            + this.name
            + ", extractor="+this.extractor
            + ", returnType="+this.returnType
            +"]";
    }
}
