package gp.impl.selector.random;

import gp.core.selector.Sampler;
import util.Preconditions;
import util.random.RandomSource;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A random sampler that selects uniform random elements from a
 * collection.
 *
 * @param <T>    The type of element to sample
 * @param random The random number generator
 * @param data   The collection to sample from
 */
public record RandomSampler<T>(
        RandomSource random,
        List<T> data
) implements Sampler<T> {
    /**
     * Compact constructor ensuring the data is not empty.
     */
    public RandomSampler {
        assert !data.isEmpty() : "Cannot sample from empty data";
    }

    /**
     * Creates an optional random sampler.
     *
     * @param <T>    The element type
     * @param random The random number generator
     * @param data   The data to sample from
     * @return An optional containing a sampler if data is not empty
     */
    public static <T> Optional<RandomSampler<T>> of(
            RandomSource random,
            List<T> data
    ) {
        if (data.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new RandomSampler<>(random, data));
    }


    /**
     * Creates an optional random sampler.
     *
     * @param <T>    The element type
     * @param data   The data to sample from
     * @param random The random number generator
     * @return An optional containing a sampler if data is not empty
     */
    public static <T, R> R ofOr(
            RandomSource random,
            List<T> data,
            Function<RandomSampler<T>, R> onSuccess,
            Supplier<R> onFailure
    ) {
        if (data.isEmpty()) {
            return onFailure.get();
        }
        return onSuccess.apply(new RandomSampler<>(random, data));
    }

    /**
     * Create a random sampler from a non-empty collection.
     *
     * @param <T>    The type of the elements in the collection.
     * @param random The source of randomness.
     * @param data   The collection to sample from.
     * @return A random sampler.
     * @throws IllegalArgumentException If the collection is empty.
     */
    public static <T> RandomSampler<T> ofNonEmpty(
            RandomSource random,
            List<T> data
    ) {
        Preconditions.assertFalse(
                data.isEmpty(),
                "Cannot create a sampler from an empty collection"
        );
        return new RandomSampler<>(random, data);
    }

    /**
     * Return a randomly selected valid index from elements.
     *
     * @param elements The array of elements to get an index for.
     * @param random   The source of randomness.
     * @param <T>      The type of the array
     * @return A random valid index
     * @throws NoSuchElementException if `elements.length == 0`.
     */
    public static <T> Integer sampleIndex(T[] elements, RandomSource random) {
        if (elements.length == 0) {
            throw new NoSuchElementException("Cannot sample from an empty collection");
        }
        return random.nextInt(0, elements.length);
    }

    /**
     * Sample a random element from a non-empty array.
     *
     * @param elements The array to sample from.
     * @param random   The source of randomness.
     * @param <T>      The type of the elements in the array.
     * @return A randomly selected element.
     * @throws NoSuchElementException If the array is empty.
     */
    public static <T> T sampleOrThrow(T[] elements, RandomSource random) {
        return elements[sampleIndex(elements, random)];
    }

    /**
     * Sample a random element from an array.
     *
     * @param items  The array of items to sample from.
     * @param random The source of Randomness.
     * @param <T>    The type of the elements in the array.
     * @return An empty option if the array is empty otherwise some of a random element.
     */
    public static <T> Optional<T> sample(T[] items, RandomSource random) {
        if (items.length == 0) {
            return Optional.empty();
        }
        return Optional.of(items[random.nextInt(0, items.length)]);
    }


    /**
     * Samples a random element from the data.
     *
     * @return A randomly selected element
     */
    @Override
    public T sample() {
        return RandomSampler.sampleOrThrow(this.data, this.random);
    }

    /**
     * Gets a random index into the data.
     *
     * @return A random index
     */
    public int sampleIndex() {
        return random.nextInt(0, data.size());
    }

    /**
     * Gets an optional random index.
     *
     * @param data   The collection
     * @param random The random number generator
     * @return An optional containing a random index if data is not
     * empty
     */
    public static Optional<Integer> sampleIndex(
            List<?> data, RandomSource random
    ) {
        if (data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(random.nextInt(0, data.size()));
    }

    /**
     * Gets a random index from a non-empty collection.
     *
     * @param data   The collection
     * @param random The random number generator
     * @return An optional containing a random index if data is not
     * empty
     * @throws IllegalArgumentException if the list is non-empty.
     */
    public static int sampleIndexOrThrow(
            List<?> data, RandomSource random
    ) throws IllegalArgumentException {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Cannot sample from empty list.");
        }

        return random.nextInt(0, data.size());
    }

    /**
     * Samples an optional element from the collection.
     *
     * @param <T>    The element type
     * @param random The random number generator
     * @param data   The collection to sample from
     * @return An optional containing a random element if data is not
     * empty
     * @see #sampleOrThrow If you know the collection is non-empty
     */
    public static <T> Optional<T> sample(
            RandomSource random,
            List<T> data
    ) {
        if (data.isEmpty()) {
            return Optional.empty();
        }

        int randomIndex = random.nextInt(0, data.size());
        return Optional.of(data.get(randomIndex));
    }

    /**
     * Samples an optional element from the collection.
     *
     * @param <T>    The element type
     * @param data   The collection to sample from
     * @param random The random number generator
     * @return An optional containing a random element if data is not
     * empty
     * @throws NoSuchElementException If the data is empty.
     * @see #sampleOrThrow If you don't know if the collection is empty
     */
    public static <T> T sampleOrThrow(
            List<T> data,
            RandomSource random
    ) throws NoSuchElementException {
        if (data.isEmpty()) {
            throw new NoSuchElementException(
                    "Cannot sample from empty collection"
            );
        }

        int randomIndex = random.nextInt(0, data.size());
        return data.get(randomIndex);
    }


    /**
     * Samples an optional element from the collection.
     *
     * @param <T>    The element type
     * @param data   The collection to sample from
     * @param random The random number generator
     * @return An optional containing a random element if data is not
     * empty
     * @see #sampleOrThrow If you don't know if the collection is empty
     */
    public static <T> T potentiallyNonDeterministicSampleOrThrow(
            Collection<T> data,
            RandomSource random
    ) throws IllegalArgumentException {
        if (data.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot sample from empty collection."
            );
        }

        int randomIndex = random.nextInt(0, data.size());
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
