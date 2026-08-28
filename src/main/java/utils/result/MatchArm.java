package utils.result;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public sealed interface MatchArm<T, E, U> permits ErrArm, OkArm {
    static <T, E, U> Stream<OkArm<T, E, U>> okArms(List<MatchArm<T, E, U>> arms) {
        return arms.stream()
            .filter( arm -> arm instanceof OkArm<T,E,U>)
            .map(arm -> (OkArm<T, E, U>) arm);
    }

    static <T, E, U> Stream<ErrArm<T, E, U>> errArms(List<MatchArm<T, E, U>> arms) {
        return arms.stream()
            .filter( arm -> arm instanceof ErrArm<T,E,U>)
            .map(arm -> (ErrArm<T, E, U>) arm);
    }

    static <T, E, U> OkArm<T, E, U> ok(Predicate<T> shouldMap, Function<T, U> mapper) {
        return new OkArm<>(Objects.requireNonNull(shouldMap), Objects.requireNonNull(mapper));
    }

    static <T, E, U> ErrArm<T, E, U> err(Predicate<E> shouldMap, Function<E, U> mapper) {
        return new ErrArm<>(Objects.requireNonNull(shouldMap), Objects.requireNonNull(mapper));
    }

	static <T, E, U> OkArm<T, E, U> okay(T value, Function<T, U> mapper) {
		return MatchArm.ok(t -> t.equals(value), mapper);
    }

    static <T, E, U> ErrArm<T, E, U> error(E value, Function<E, U> mapper) {
        return MatchArm.err(e -> e.equals(value), mapper);
    }
}


