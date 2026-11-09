package util.sequence;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Single<E> extends Sequence<E> {
    static <E> Single<E> of(E element) {
        return new SingleImpl<>(element);
    }

    E element();

    @Override
    <T> Single<T> map(Function<E, T> mapper);
}

record SingleImpl<E>(E element) implements Single<E> {
    @Override
    public List<E> list() {
        return List.of(element);
    }

    @Override
    public Stream<E> stream() {
        return Stream.of(element);
    }

    @Override
    public void forEach(Consumer<E> consumer) {
        consumer.accept(element);
    }

    @Override
    public <T> Single<T> map(Function<E, T> mapper) {
        return new SingleImpl<>(mapper.apply(element));
    }
}
