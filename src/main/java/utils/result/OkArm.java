package utils.result;

import java.util.function.Function;
import java.util.function.Predicate;

public record OkArm<T, E, U>(Predicate<T> shouldMap, Function<T, U> mapper) implements MatchArm<T, E, U> {}
