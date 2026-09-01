package util;

import java.util.function.Function;

/**
 * A generic mutable wrapper class for a single value.
 *
 * @param <T> The type of the wrapped value
 */
public class Mutable<T> {
    /**
     * Wrapped mutable value.
     */
    private T value;

    Mutable(final T initialValue) {
        this.value = initialValue;
    }

    /**
     * Creates a new Mutable with the given value.
     *
     * @param <T>   The type of the value
     * @param value The initial value
     * @return A new Mutable instance
     */
    public static <T> Mutable<T> of(final T value) {
        return new Mutable<>(value);
    }

    /**
     * @return The wrapped value.
     */
    public T get() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param newValue The new value
     * @return This Mutable instance for chaining
     */
    public Mutable<T> set(final T newValue) {
        this.value = newValue;
        return this;
    }

    /**
     * Update the value in this mutable using the updater.
     *
     * @param updater A function mapping the old value to the new value.
     * @return this
     */
    public Mutable<T> update(Function<T, T> updater) {
        this.set(updater.apply(this.value));
        return this;
    }
}
