package gp.utils;

/**
 * A utility interface for executing a runnable operation multiple times.
 */
public interface Repeat {
    /**
     * Executes the given runnable the specified number of times.
     * @param numTimes The number of times to run the operation
     * @param runnable The runnable to execute
     */
    static void of(int numTimes, Runnable runnable) {
        for (int i = 0; i < numTimes; i++) {
            runnable.run();
        }
    }
}
