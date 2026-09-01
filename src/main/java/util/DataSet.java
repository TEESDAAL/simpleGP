package util;

import util.random.RandomSource;
import util.stream_util.StreamZipper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A helper interface for working with Labeled datasets.
 *
 * @param <X> the x value type
 * @param <Y> the y value type
 */
public interface DataSet<X, Y> {
    static <X, Y> DataSet<X, Y> of(List<Pair<X, Y>> data) {
        return new DataSetImpl<>(data);
    }

    static <X, Y> DataSet<X, Y> of(List<X> input, List<Y> labels) {
        return DataSet.of(StreamZipper.zip(
                input.stream(), labels.stream()
        ).toList());
    }

    /**
     * @return the full dataset.
     */
    List<Pair<X, Y>> data();

    /**
     * @return the number of entries in the dataset.
     */
    default int size() {
        return this.data().size();
    }

    /**
     * Returns a stream of all x values in the dataset.
     *
     * @return a stream of x values
     */
    default Stream<X> xs() {
        return this.data().stream().map(Pair::first);
    }

    /**
     * Returns a stream of all y values in the dataset.
     *
     * @return a stream of y values
     */
    default Stream<Y> ys() {
        return this.data().stream().map(Pair::second);
    }

    /**
     * Applies a mapping function to each pair in the dataset.
     *
     * @param zipper the function that combines x and y values
     * @param <R>    the result type
     * @return a stream of mapped values
     */
    default <R> Stream<R> zip(BiFunction<X, Y, R> zipper) {
        return this.data().stream().map(datum -> zipper.apply(
                datum.first(),
                datum.second()
        ));
    }

    /**
     * Randomly split a dataset accourding the the given split percentages.
     * Note the last value is inferred so, randomSplit(rand, 0.1, 0.8), will give you
     * splits of [0.1, 0.8, 0.1].
     * Warning: This means you should never provide the final double value:
     * As randomSplit(rand, 0.1, 0.8, 0.1) will give splits of [0.1,0.8,0.1,0.0]
     *
     * @param random           The source of randomness to do the split.
     * @param splitPercentages The % of the dataset each split should take up.
     * @return A list of split datasets, in the order of split percentages.
     */
    default List<DataSet<X, Y>> randomSplit(
            RandomSource random, double... splitPercentages
    ) {
        Preconditions.assertTrue(
                Arrays.stream(splitPercentages).allMatch(p -> 0.0 <= p && p <= 1.0),
                "Given split percentages "
                        + Arrays.toString(splitPercentages)
                        + " contains a probability not in [0, 1]"
        );
        Preconditions.assertTrue(
                Arrays.stream(splitPercentages).sum() <= 1.0,
                "Given split percentages sum to greater than 1"
        );
        return randomSplit(
                random,
                Arrays.stream(splitPercentages)
                        .mapToInt(p -> (int) (p * this.size()))
                        .toArray()
        );
    }

    default List<DataSet<X, Y>> randomSplit(RandomSource random, int... splitSizes) {
        Preconditions.assertTrue(
                Arrays.stream(splitSizes).allMatch(s -> 0 <= s && s <= this.size()),
                "Given split sizes "
                        + Arrays.toString(splitSizes)
                        + " contains a size not in [0, " + this.size() + "]"
        );
        Preconditions.assertTrue(
                Arrays.stream(splitSizes).sum() <= this.size(),
                "Given split percentages sum to "
                        + Arrays.stream(splitSizes).sum()
                        + " which is more than the data size: "
                        + this.size()
        );
        final List<Integer> indices = IntStream.range(0, this.size()).boxed().collect(
                Collectors.toCollection(ArrayList::new)
        );
        random.shuffleInPlace(indices);
        final int start = 0;

        final List<DataSet<X, Y>> data = new ArrayList<>();
        for (final int size : IntStream.concat(
                Arrays.stream(splitSizes),
                IntStream.of(this.size() - Arrays.stream(splitSizes).sum())
        ).toArray()) {
            data.add(
                    DataSet.of(
                            this.data().subList(start, start + size)
                    )
            );
        }

        return Collections.unmodifiableList(data);
    }
}

record DataSetImpl<X, Y>(
        List<Pair<X, Y>> data
) implements DataSet<X, Y> {
}
