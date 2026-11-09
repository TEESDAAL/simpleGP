package util;

import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

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
    static void of(int numTimes, Runnable runnable) {
        Preconditions.assertTrue(numTimes > 0, "numTimes must be positive, is "+numTimes);
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
    static void of(int numTimes, IntConsumer runnable) {
        Preconditions.assertTrue(numTimes > 0, "numTimes must be positive, is "+numTimes);
        for (int i = 0; i < numTimes; i++) {
            runnable.accept(i);
        }
    }

    static <T> Optional<T> untilSuccess(int numTimes, IntFunction<Optional<T>> supplier) {
        Preconditions.assertTrue(numTimes > 0, "numTimes must be positive, is "+numTimes);
        for (int i = 0; i < numTimes; i++) {
            final var opt = supplier.apply(i);
            if (opt.isPresent()) {
                return opt;
            }
        }
        return Optional.empty();
    }
}
