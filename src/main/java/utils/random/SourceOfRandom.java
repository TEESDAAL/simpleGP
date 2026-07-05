package utils.random;


import java.util.function.Supplier;

public class SourceOfRandom implements Supplier<RandomSource> {
    long seed;
    final RandomSource randomSource;

    public SourceOfRandom(long seed) {
        this.seed = seed;
        this.randomSource = RandomSource.of(seed);
    }

    public void reseed(long seed) {
        this.seed = seed;
        this.randomSource.seed(seed);
    }

    @Override
    public RandomSource get() {
        return RandomSource.of(randomSource.nextLong(
            Long.MIN_VALUE, Long.MAX_VALUE
        ));
    }
}