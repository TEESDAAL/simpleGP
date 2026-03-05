package gp.statistics;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Functional interface for performing side effects on populations.
 * Side effects typically involve logging but do not modify the population.
 * @param <T> The type to perform the side effect on
 */
public interface SideEffect<T> extends Function<T, T> {

    /**
     * Creates a side effect from a consumer.
     * @param <T> The type to perform the side effect on
     * @param effect The consumer to apply
     * @return A side effect
     */
    static <T> SideEffect<T> of(Consumer<T> effect) {
        return t -> {
            effect.accept(t);
            return t;
        };
    }

    /**
     * Combines multiple side effects into one.
     * @param <T> The type to perform the side effects on
     * @param sideEffects The side effects to combine
     * @return A single side effect that applies all the given side effects in order
     */
    static <T> SideEffect<T> of(Collection<SideEffect<T>> sideEffects) {
        Function<T, T> combined = sideEffects.stream()
                .map(f -> (Function<T, T>) f)
                .reduce(i -> i, Function::andThen);

        return combined::apply;
    }


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
}

