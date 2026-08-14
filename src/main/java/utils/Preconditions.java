package utils;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simple assertion utility class.
 */
public enum Preconditions {;
    /**
     * Asserts that a condition is true. If it is not,
     * an IllegalArgumentException is thrown with the given message.
     *
     * @param condition the condition to check
     * @param message   the message to include in the error if the assertion fails
     * @throws IllegalArgumentException if the condition is false
     */
    public static void assertTrue(boolean condition, String message)
        throws IllegalArgumentException {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Asserts that a condition is true, while passing through the value.
     * If the predicate returns true,
     * `value` is returned, otherwise an error is thrown.
     *
     * @param value     the value to check
     * @param condition the condition to check against
     * @param message   the error message to display if the condition is false
     * @param <T>       the type of the value
     * @return value if the condition is true
     * @throws IllegalArgumentException if the condition is false
     */
    public static <T> T assertTrue(
        final T value,
        final Predicate<T> condition,
        final Function<T, String> message
    ) throws IllegalArgumentException {
        if (!condition.test(value)) {
            throw new IllegalArgumentException(message.apply(value));
        }

        return value;
    }

    /**
     * Asserts that a condition is false.
     * If it is not, an IllegalArgumentException is thrown with the given message.
     *
     * @param condition the condition to check
     * @param message   the message to include in the error if the assertion fails
     * @throws IllegalArgumentException if the condition is true
     */
    public static void assertFalse(boolean condition, String message)
        throws IllegalArgumentException {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Asserts that a condition is false, while passing through the value.
     * If the predicate returns false, `value` is returned,
     * otherwise an error is thrown.
     *
     * @param value     the value to check
     * @param condition the condition to check against
     * @param message   the error message to display if the condition is true
     * @param <T>       the type of the value
     * @return value if the condition is false
     * @throws IllegalArgumentException if the condition is true
     */
    public static <T> T assertFalse(
        final T value,
        final Predicate<T> condition,
        final String message
    )
        throws IllegalArgumentException {
        if (condition.test(value)) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    /**
     * Asserts that two values are equal.
     * If they are not, an IllegalArgumentException is thrown with the given message.
     *
     * @param expected the expected value
     * @param actual   the actual value
     * @param message  the message to include in the error if the assertion fails
     * @param <T>      the type of the values being compared
     * @throws IllegalArgumentException if the values are not equal
     */
    public static <T> void assertEquals(
        final T expected,
        final T actual,
        final String message
    ) throws IllegalArgumentException {
        if (expected == actual) {
            return;
        }

        if (expected == null || !expected.equals(actual)) {
            throw new IllegalArgumentException(
                message
                    + " Expected: " + expected
                    + ", Actual: " + actual
            );
        }
    }

    /**
     * Require all given objects to be non-null.
     * @param objects A set of objects which shouldn't be null.
     */
    public static void allNonNull(Object... objects) {
        for (final Object o : objects) {
            Objects.requireNonNull(o);
        }
    }
}
