package utils;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public record DataSet<X, Y>(
    List<Pair<X, Y>> data
) {
    public Stream<X> xs() {
        return data.stream().map(Pair::first);
    }

    public Stream<Y> ys() {
        return data.stream().map(Pair::second);
    }

    public <R> Stream<R> zip(BiFunction<X, Y, R> zipper) {
        return data.stream().map(datum -> zipper.apply(
            datum.first(),
            datum.second()
        ));
    }
}
