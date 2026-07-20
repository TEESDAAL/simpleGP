package utils;

import java.util.function.IntConsumer;

/**
 * A utility interface for executing a runnable operation multiple times.
 */
public interface Repeat {
    /**
     * Executes the given runnable the specified number of times.
     *
     * @param numTimes The number of times to run the operation
     * @param runnable The runnable to execute
     */
    static void of(final int numTimes, final Runnable runnable) {
        for (int i = 0; i < numTimes; i++) {
            runnable.run();
        }
    }

    /**
     * Executes the given runnable the specified number of times.
     *
     * @param numTimes The number of times to run the operation
     * @param runnable The runnable to execute
     */
    static void of(final int numTimes, final IntConsumer runnable) {
        for (int i = 0; i < numTimes; i++) {
            runnable.accept(i);
        }
    }
}
