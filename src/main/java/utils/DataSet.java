package utils;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * A simple paired dataset with helpers for streaming x values, y values,
 * and zipped projections.
 *
 * @param <X> the x value type
 * @param <Y> the y value type
 * @param data the stored dataset entries
 */
public record DataSet<X, Y>(
    List<Pair<X, Y>> data
) {
    /**
     * Returns a stream of all x values in the dataset.
     *
     * @return a stream of x values
     */
    public Stream<X> xs() {
        return data.stream().map(Pair::first);
    }

    /**
     * Returns a stream of all y values in the dataset.
     *
     * @return a stream of y values
     */
    public Stream<Y> ys() {
        return data.stream().map(Pair::second);
    }

    /**
     * Applies a mapping function to each pair in the dataset.
     *
     * @param zipper the function that combines x and y values
     * @param <R> the result type
     * @return a stream of mapped values
     */
    public <R> Stream<R> zip(BiFunction<X, Y, R> zipper) {
        return data.stream().map(datum -> zipper.apply(
            datum.first(),
            datum.second()
        ));
    }
}
