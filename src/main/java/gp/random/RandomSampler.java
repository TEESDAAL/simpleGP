package gp.random;

import gp.statistics.Selector;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

public record RandomSampler<T>(Collection<T> data, Random random) implements Selector<T> {
    public RandomSampler {
        assert !data.isEmpty(): "Cannot sample from empty data";
    }

    public static <T> Optional<RandomSampler<T>> of(Collection<T> data, Random random) {
        if (data.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new RandomSampler<>(data, random));
    }

    @Override
    public T sample() {
        return RandomSampler.sample(this.data, this.random).orElseThrow(
                () -> new IllegalStateException("Should be impossible to sample from empty collection")
        );
    }

    public int sampleIndex() {
        return random.nextInt(data.size());
    }

    public static Optional<Integer> sampleIndex(Collection<?> data, Random random) {
        if (data.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(random.nextInt(data.size()));
    }

    public static <T> Optional<T> sample(Collection<T> data, Random random) {
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
