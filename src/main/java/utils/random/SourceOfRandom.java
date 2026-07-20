package utils.random;


import java.util.function.Supplier;

/**
 * Supplies mutable random sources from a shared seed.
 */
public class SourceOfRandom implements Supplier<RandomSource> {
    private final RandomSource randomSource;

    /**
     * Creates a new seeded random source supplier.
     *
     * @param seed the initial seed value
     */
    public SourceOfRandom(long seed) {
        this.randomSource = RandomSource.of(seed);
    }

    /**
     * Reseeds the underlying random source.
     *
     * @param seed the new seed value
     */
    public void reseed(long seed) {
        this.randomSource.seed(seed);
    }

    /**
     * Returns a new random source derived from the current state.
     *
     * @return a fresh random source
     */
    @Override
    public RandomSource get() {
        return RandomSource.of(randomSource.nextLong(
            Long.MIN_VALUE, Long.MAX_VALUE
        ));
    }
}
