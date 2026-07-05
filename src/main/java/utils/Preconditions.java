package utils;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * A simple assertion utility class.
 */
public final class Preconditions {

    private Preconditions() {}

    /**
     * Asserts that a condition is true. If it is not,
     *      an IllegalArgumentException is thrown with the given message.
     * @param condition The condition to check
     * @param message The message to include in the error if the assertion fails
     * @throws IllegalArgumentException if the condition is false
     */
    public static void assertTrue(boolean condition, String message)
            throws IllegalArgumentException {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Asserts that a condition is true using a BooleanSupplier. If it is not,
     *      an IllegalArgumentException is thrown with the given message.
     * @param supplier The BooleanSupplier that provides the condition to check
     * @param message The message to include in the error if the assertion fails
     * @throws IllegalArgumentException if the condition is false
     */
    public static void assertTrue(BooleanSupplier supplier, String message)
            throws IllegalArgumentException {
        assertTrue(supplier.getAsBoolean(), message);
    }


    /**
     * Asserts that a condition is false.
     * If it is not, an IllegalArgumentException is thrown with the given message.
     * @param condition The condition to check
     * @param message The message to include in the error if the assertion fails
     * @throws IllegalArgumentException if the condition is true
     */
    public static void assertFalse(boolean condition, String message)
            throws IllegalArgumentException {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Lazily asserts that a condition is false using a BooleanSupplier.
     * If it is not, an IllegalArgumentException is thrown with the given message.
     * @param supplier The BooleanSupplier that provides the condition to check
     * @param message The message to include in the error if the assertion fails
     * @throws IllegalArgumentException if the condition is true
     */
    public static void assertFalse(BooleanSupplier supplier, String message)
            throws IllegalArgumentException {
        if (supplier.getAsBoolean()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Lazily asserts that two values are equal.
     * If they are not, an IllegalArgumentException is thrown with the given message.
     * @param <T> The type of the values being compared
     * @param expected The expected value
     * @param actual The supplier of the actual value
     * @param message The message to include in the error if the assertion fails
     * @throws IllegalArgumentException if the values are not equal
     */
    public static <T> void assertEquals(
            T expected, Supplier<T> actual, String message
    ) throws IllegalArgumentException {
        assertEquals(expected, actual.get(), message);
    }

    /**
     * Asserts that two values are equal.
     * If they are not, an IllegalArgumentException is thrown with the given message.
     * @param <T> The type of the values being compared
     * @param expected The expected value
     * @param actual The actual value
     * @param message The message to include in the error if the assertion fails
     * @throws IllegalArgumentException if the values are not equal
     */
    public static <T> void assertEquals(T expected, T actual, String message)
            throws IllegalArgumentException {
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
}
