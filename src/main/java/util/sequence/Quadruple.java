package util.sequence;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Quadruple<E> extends Sequence<E> {
    static <E> Quadruple<E> of(E first, E second, E third, E fourth) {
        return new QuadrupleImpl<>(first, second, third, fourth);
    }
    E first();
    E second();
    E third();
    E fourth();
    <T> Quadruple<T> map(Function<E, T> mapper);
}

record QuadrupleImpl<E>(E first, E second, E third, E fourth) implements Quadruple<E> {
    @Override
    public List<E> list() {
        return List.of(first, second, third, fourth);
    }

    @Override
    public Stream<E> stream() {
        return Stream.of(first, second, third, fourth);
    }

    @Override
    public void forEach(Consumer<E> consumer) {
        consumer.accept(first);
        consumer.accept(second);
        consumer.accept(third);
        consumer.accept(fourth);
    }

    @Override
    public <T> Quadruple<T> map(Function<E, T> mapper) {
        return new QuadrupleImpl<>(
                mapper.apply(first),
                mapper.apply(second),
                mapper.apply(third),
                mapper.apply(fourth)
        );
    }
}
