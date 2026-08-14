package utils.result;

import java.util.function.Function;
import java.util.function.Predicate;

public record ErrArm<T, E, U>(Predicate<E> shouldMap, Function<E, U> mapper) implements MatchArm<T, E, U> {}
