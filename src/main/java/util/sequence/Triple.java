package util.sequence;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Triple<E> extends Sequence<E> {
    static <E> Triple<E> of(E first, E second, E third) {
        return new TripleImpl<>(first, second, third);
    }
    E first();
    E second();
    E third();
    @Override
    <T> Triple<T> map(Function<E, T> mapper);
}

record TripleImpl<E>(E first, E second, E third) implements Triple<E> {
    @Override
    public List<E> list() {
        return List.of(first, second, third);
    }

    @Override
    public Stream<E> stream() {
        return Stream.of(first, second, third);
    }

    @Override
    public void forEach(Consumer<E> consumer) {
        consumer.accept(first);
        consumer.accept(second);
        consumer.accept(third);
    }

    @Override
    public <T> Triple<T> map(Function<E, T> mapper) {
        return new TripleImpl<>(
                mapper.apply(first),
                mapper.apply(second),
                mapper.apply(third)
        );
    }
}

