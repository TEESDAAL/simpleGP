package utils.stream_utils;


import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.Spliterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Groups a stream into batches of `groupSize` elements.
 */
public final class GroupingIterable {

    private GroupingIterable() {
        // Prevent instantiation
    }

    /**
     * Groups a spliterator into batches of `groupSize` elements.
     *
     * @param <T>            The element type
     * @param sourceIterator The input Spliterator
     * @param groupSize      The size of each group
     * @return A Spliterator of Stream<T> where each stream
     *      contains up to groupSize elements
     */
    public static <T> Spliterator<Stream<T>> spliterator(
            final Spliterator<T> sourceIterator, int groupSize
    ) {
        return new Spliterator<>() {
            @Override
            public boolean tryAdvance(Consumer<? super Stream<T>> action) {
                List<T> batch = new ArrayList<>(groupSize);
                for (int i = 0; i < groupSize; i++) {
                    sourceIterator.tryAdvance(batch::add);
                }
                if (batch.isEmpty()) {
                    return false;
                }
                action.accept(batch.stream());
                return true;
            }

            @Override
            public Spliterator<Stream<T>> trySplit() {
                var sourceSplit = sourceIterator.trySplit();
                if (sourceSplit == null) {
                    return null;
                }
                return GroupingIterable.spliterator(sourceSplit, groupSize);
            }

            @Override
            public long estimateSize() {
                if (sourceIterator.estimateSize() == Long.MAX_VALUE) {
                    return Long.MAX_VALUE;
                }
                return (long) Math.ceil(
                        (double) sourceIterator.estimateSize() / groupSize
                );
            }

            @Override
            public int characteristics() {
                return sourceIterator.characteristics()
                        & ~(Spliterator.SIZED
                        | Spliterator.SUBSIZED
                        | Spliterator.SORTED);
            }
        };
    }

    /**
     * Groups a stream into batches of `groupSize` elements.
     * @param <T> The element type
     * @param source The input stream
     * @param batchSize The size of each group
     * @return A stream of Stream<T> where each stream contains
     *      up to batchSize elements
     */
    public static <T> Stream<Stream<T>> group(
            final Stream<T> source,
            final int batchSize
    ) {
        return StreamSupport.stream(
                GroupingIterable.spliterator(source.spliterator(), batchSize),
                source.isParallel()
        );
    }
}
