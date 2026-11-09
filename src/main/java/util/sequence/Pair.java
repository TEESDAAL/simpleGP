package util.sequence;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Pair<E> extends Sequence<E> {
    static <E> Pair<E> of(E first, E second) {
        return new PairImpl<>(first, second);
    }
    E first();
    E second();

    @Override
    <T> Pair<T> map(Function<E, T> mapper);
}

record PairImpl<E>(E first, E second) implements Pair<E> {
    @Override
    public List<E> list() {
        return List.of(first, second);
    }

    @Override
    public Stream<E> stream() {
        return Stream.of(first, second);
    }

    @Override
    public void forEach(Consumer<E> consumer) {
        consumer.accept(first);
        consumer.accept(second);
    }

    @Override
    public <T> Pair<T> map(Function<E, T> mapper) {
        return new PairImpl<>(
                mapper.apply(first),
                mapper.apply(second)
        );
    }
}

