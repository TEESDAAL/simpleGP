package gp.statistics;

import java.util.function.Consumer;
import java.util.function.Function;

public interface SideEffect<T> extends Function<T, T> {
    /**
     * Perform some side-effect on the population,
     * usually logging to the terminal or a file.
     * @param population The population to evaluate
     * @return the population, should be unmodified.
     */
    T sideEffect(final T population);

    @Override
    default T apply(final T t) {
        return sideEffect(t);
    }

    static <T> SideEffect<T> of(Consumer<T> effect) {
        return t -> {
            effect.accept(t);
            return t;
        };
    }
}

