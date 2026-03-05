package gp.utils;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Interface for operations that can be parallelized.
 */
public interface Parallelizeable {

    /**
     * Creates a Parallelizeable instance with the specified batch size.
     * @param batchSize The batch size for parallel operations.
     *                  Must be greater than 0.
     * @return A Parallelizeable instance with the specified batch size
     */
    static Parallelizeable of(int batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("Batch size must be greater than 0");
        }
        return new Parallelizeable() {
            @Override
            public boolean shouldParallelize() {
                return true;
            }

            @Override
            public int batchSize() {
                return batchSize;
            }
        };
    }

    /**
     * Whether operations should be parallelized.
     * @return true if should parallelize
     */
    boolean shouldParallelize();

    /**
     * Gets the batch size for parallel operations.
     * @return the amount of jobs to run in serial E.g. if this returns
     *     5, the parallelize operation will group the stream into n
     *     groups of 5 Then it will run n task in parallel, and each of
     *     those n tasks will run the operation in 5 times in series.
     */
    default int batchSize() {
        return 1;
    }

    /**
     * Generates a stream using a supplier.
     * @param <T> The element type
     * @param supplier The supplier function
     * @return A generated stream
     */
    default <T> Stream<T> generate(Supplier<T> supplier) {
        return Stream.generate(
                () -> Stream.generate(supplier).limit(batchSize())
        ).parallel().flatMap(Function.identity());
    }
    /**
     * Generates n elements using a supplier.
     * @param <T> The element type
     * @param supplier The supplier function
     * @param n The number of elements to generate
     * @return A generated stream
     */
    default <T> Stream<T> generateN(Supplier<T> supplier, int n) {
        return this.parallelize(
                IntStream.range(0, n).boxed(),
                ignored -> supplier.get());
    }

    /**
     * Parallelizes a stream operation.
     * @param <T> The input type
     * @param <U> The output type
     * @param stream The input stream
     * @param mapper The mapping function
     * @return The mapped stream
     */
    default <T, U> Stream<U> parallelize(
            Stream<T> stream,
            Function<? super T, ? extends U> mapper) {
        if (shouldParallelize()) {
            assert batchSize() > 0 : "Batch size must be greater than 0";

            if (batchSize() == 1) {
                return stream.parallel().map(mapper);
            }
            return GroupingIterable.group(stream.parallel(), this.batchSize())
                    .flatMap(l -> l.map(mapper));
        }

        return stream.map(mapper);
    }
}


