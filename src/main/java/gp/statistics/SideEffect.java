package gp.statistics;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Functional interface for performing side effects on populations.
 * Side effects typically involve logging but do not modify the population.
 * @param <T> The type of population
 */
public interface SideEffect<T> extends Function<T, T> {
    /**
     * Perform some side-effect on the population,
     * usually logging to the terminal or a file.
     * @param population The population to evaluate
     * @return the population, should be unmodified.
     */
    T sideEffect(T population);

    @Override
    default T apply(final T t) {
        return sideEffect(t);
    }

    /**
     * Creates a side effect from a consumer.
     * @param <T> The population type
     * @param effect The consumer to apply
     * @return A side effect
     */
    static <T> SideEffect<T> of(Consumer<T> effect) {
        return t -> {
            effect.accept(t);
            return t;
        };
    }
}

