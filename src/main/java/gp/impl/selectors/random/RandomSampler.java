package gp.impl.selectors.random;

import gp.core.selectors.Sampler;
import result.Result;
import utils.random.RandomSource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A random sampler that selects uniform random elements from a
 * collection.
 * @param <T> The type of element to sample
 * @param data The collection to sample from
 * @param random The random number generator
 */
public record RandomSampler<T>(
        List<T> data, RandomSource random
) implements Sampler<T> {
    /**
     * Compact constructor ensuring the data is not empty.
     */
    public RandomSampler {
        assert !data.isEmpty() : "Cannot sample from empty data";
    }

    /**
     * Creates an optional random sampler.
     * @param <T> The element type
     * @param data The data to sample from
     * @param random The random number generator
     * @return An optional containing a sampler if data is not empty
     */
    public static <T> Optional<RandomSampler<T>> of(
            final List<T> data, final RandomSource random
    ) {
        if (data.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new RandomSampler<>(data, random));
    }

    /**
     * Samples a random element from the data.
     * @return A randomly selected element
     */
    @Override
    public T sample() {
        return RandomSampler.sample(this.data, this.random)
                .orElseThrow(
                () -> new IllegalStateException(
                        "Should be impossible to sample from empty collection"
                )
        );
    }

    /**
     * Gets a random index into the data.
     * @return A random index
     */
    public int sampleIndex() {
        return random.nextInt(0, data.size());
    }

    /**
     * Gets an optional random index.
     * @param data The collection
     * @param random The random number generator
     * @return An optional containing a random index if data is not
     *     empty
     */
    public static Optional<Integer> sampleIndex(
            final List<?> data, final RandomSource random
    ) {
        if (data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(random.nextInt(0, data.size()));
    }

    /**
     * Samples an optional element from the collection.
     * @see #sampleOrThrow If you know the collection is non-empty
     * @param <T> The element type
     * @param data The collection to sample from
     * @param random The random number generator
     * @return An optional containing a random element if data is not
     *     empty
     */
    public static <T> Optional<T> sample(
            final List<T> data, final RandomSource random
    ) {
        if (data.isEmpty()) {
            return Optional.empty();
        }

        final int randomIndex = random.nextInt(0, data.size());
        return Optional.of(data.get(randomIndex));
    }

    /**
     * Samples an optional element from the collection.
     * @see #sample If you don't know if the collection is empty
     * @param <T> The element type
     * @param data The collection to sample from
     * @param random The random number generator
     * @return An optional containing a random element if data is not
     *     empty
     */
    public static <T> T sampleOrThrow(
        final List<T> data, final RandomSource random
    ) throws IllegalArgumentException {
        if (data.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot sample from empty collection"
            );
        }

        final int randomIndex = random.nextInt(0, data.size());
        return data.get(randomIndex);
    }


    /**
     * Samples an optional element from the collection.
     * @see #sample If you don't know if the collection is empty
     * @param <T> The element type
     * @param data The collection to sample from
     * @param random The random number generator
     * @return An optional containing a random element if data is not
     *     empty
     */
    public static <T> T potentiallyNonDeterministicSampleOrThrow(
        final Collection<T> data, final RandomSource random
    ) throws IllegalArgumentException {
        if (data.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot sample from empty collection."
            );
        }

        final int randomIndex = random.nextInt(0, data.size());
        if (data instanceof List<T> list) {
            return list.get(randomIndex);
        }
        int i = 0;
        for (T element : data) {
            if (i == randomIndex) {
                return element;
            }
            i++;
        }
        throw new IllegalStateException(
            "Should be impossible, if the random index is with data bounds. Index: "
                + randomIndex
        );
    }
}
