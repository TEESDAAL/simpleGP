package gp.random;

import gp.statistics.Selector;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

/**
 * A random sampler that selects uniform random elements from a
 * collection.
 * @param <T> The type of element to sample
 * @param data The collection to sample from
 * @param random The random number generator
 */
public record RandomSampler<T>(
        Collection<T> data, Random random) implements Selector<T> {
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
            final Collection<T> data, final Random random) {
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
                        "Should be impossible to sample from empty collection")
        );
    }

    /**
     * Gets a random index into the data.
     * @return A random index
     */
    public int sampleIndex() {
        return random.nextInt(data.size());
    }

    /**
     * Gets an optional random index.
     * @param data The collection
     * @param random The random number generator
     * @return An optional containing a random index if data is not
     *     empty
     */
    public static Optional<Integer> sampleIndex(
            final Collection<?> data, final Random random) {
        if (data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(random.nextInt(data.size()));
    }

    /**
     * Samples an optional element from the collection.
     * @param <T> The element type
     * @param data The collection to sample from
     * @param random The random number generator
     * @return An optional containing a random element if data is not
     *     empty
     */
    public static <T> Optional<T> sample(
            final Collection<T> data, final Random random) {
        int randomIndex = random.nextInt(data.size());
        int i = 0;
        for (T element : data) {
            if (i == randomIndex) {
                return Optional.of(element);
            }
            i++;
        }
        return Optional.empty();
    }
}
