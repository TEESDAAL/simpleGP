package util.result;

import java.util.function.Function;
import java.util.function.Predicate;

public record OkArm<T, E, U>(Predicate<T> shouldMap, Function<T, U> mapper) implements MatchArm<T, E, U> {
    public boolean matches(T value) {
        return this.shouldMap.test(value);
    }

    public U map(T value) {
        return this.mapper.apply(value);
    }
}
