package util.result;

import java.util.function.Function;
import java.util.function.Predicate;

public record ErrArm<T, E, U>(Predicate<E> shouldMap, Function<E, U> mapper) implements MatchArm<T, E, U> {
    public boolean matches(E error) {
        return this.shouldMap.test(error);
    }

    public U map(E error) {
        return mapper.apply(error);
    }
}
