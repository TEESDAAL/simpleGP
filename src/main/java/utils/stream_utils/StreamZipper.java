package utils.stream_utils;

import utils.Mutable;
import utils.Pair;

import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility class for zipping two streams together.
 */
public final class StreamZipper {
    private StreamZipper() {
        // Prevent instantiation
    }
    /**
     * Zips two streams together using a provided function.
     * The resulting stream will have a length equal to the
     *      shorter of the two input streams.
     * @param a The first stream
     * @param b The second stream
     * @param fn The function to combine elements from the two streams
     * @return A stream of results from applying the function to
     *      pairs of elements from the input streams
     * @param <A> The type of elements in the first stream
     * @param <B> The type of elements in the second stream
     * @param <R> The type of elements in the resulting stream
     */
    public static <A, B, R> Stream<R> zip(
            Stream<A> a, Stream<B> b,
            BiFunction<A, B, R> fn
    ) {
        return StreamSupport.stream(
                spliterator(a.spliterator(), b.spliterator(), fn),
                a.isParallel() && b.isParallel()
        );
    }

    /**
     * Zips two streams together into a stream of pairs.
     * @param a The first stream
     * @param b The second stream
     * @return A stream of pairs of elements from the input streams
     * @param <A> The type of elements in the first stream
     * @param <B> The type of elements in the second stream
     */
    public static <A, B> Stream<Pair<A, B>> zip(Stream<A> a, Stream<B> b) {
        return zip(a, b, Pair::of);
    }

    private static <A, B, R> Spliterator<R> spliterator(
            Spliterator<A> a, Spliterator<B> b,
            BiFunction<A, B, R> fn
    ) {
        return new Spliterator<>() {
            @Override
            public boolean tryAdvance(Consumer<? super R> action) {
                Mutable<A> aBox = Mutable.of(null);
                Mutable<B> bBox = Mutable.of(null);
                boolean aHasNext = a.tryAdvance(aBox::set);
                boolean bHasNext = b.tryAdvance(bBox::set);
                if (aHasNext && bHasNext) {
                    action.accept(fn.apply(aBox.get(), bBox.get()));
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<R> trySplit() {
                return spliterator(
                    a.trySplit(), b.trySplit(), fn
                );
            }

            @Override
            public long estimateSize() {
                return Math.min(a.estimateSize(), b.estimateSize());
            }

            @Override
            public int characteristics() {
                return a.characteristics() & b.characteristics()
                    & ~(Spliterator.SORTED | Spliterator.DISTINCT);
            }
        };
    }
}
