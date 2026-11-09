package util.sequence;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * An interface for immutable collections of 1 or more elements.
 * @param <E> The element type
 */
public interface Sequence<E> {
    static <E> Pair<E> of(E first, E second) {
        return new PairImpl<>(first, second);
    }
    static <E> Triple<E> of(E first, E second, E third) {
        return new TripleImpl<>(first, second, third);
    }
    static <E> Quadruple<E> of(E first, E second, E third, E fourth) {
        return new QuadrupleImpl<>(first, second, third, fourth);
    }

    /**
     * @return This sequence as a list.
     */
    List<E> list();

    /**
     * @return this sequence as a stream.
     */
    Stream<E> stream();

    /**
     * Calls the consumer on all elements of this sequence in order.
     * @param consumer the Consumer to run over all elements.
     */
    void forEach(Consumer<E> consumer);

    /**
     * Maps this sequence using the mapper.
     * Note: All implementations should override this to preserve the type.
     * @param mapper The element mapper.
     * @return The new sequence produced by the mapper.
     * @param <T> The type of the returned sequence.
     */
    <T> Sequence<T> map(Function<E, T> mapper);
}