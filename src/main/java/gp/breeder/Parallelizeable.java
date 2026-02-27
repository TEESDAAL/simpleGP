package gp.breeder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Interface for operations that can be parallelized.
 */
public interface Parallelizeable {
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
    default <T> Stream<T> generateN(
            Supplier<T> supplier, int n) {
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
            if (batchSize() == 1) {
                return stream.parallel().map(mapper);
            }
            return group(stream, this.batchSize())
                    .parallel()
                    .flatMap(l -> l.map(mapper));
        }

        return stream.map(mapper);
    }

    /**
     * Groups a stream into batches.
     * @param <T> The element type
     * @param stream The input stream
     * @param groupSize The size of each group
     * @return A stream of grouped streams
     */
    static <T> Stream<Stream<T>> group(Stream<T> stream, int groupSize) {
       Iterator<T> source = stream.iterator();
       return Stream.generate(() -> {
            List<T> list = new ArrayList<>();

            for (int i = 0; i < groupSize; i++) {
                if (!source.hasNext()) {
                    break;
                }
                list.add(source.next());
            }
            return list;
        }).takeWhile(list -> !list.isEmpty())
               .map(Collection::stream);
    }
}


