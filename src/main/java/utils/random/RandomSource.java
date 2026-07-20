package utils.random;

import java.util.Random;

/**
 * A source of random values over bounded ranges.
 */
public interface RandomSource {
    /**
     * Creates a random source backed by a {@link Random} instance.
     *
     * @param random the backing random instance
     * @return a random source backed by the given random instance
     */
    static RandomSource of(final Random random) {
        return new RandomSource() {
            @Override
            public int nextInt(final int lowerBound, final int upperBound) {
                return random.nextInt(lowerBound, upperBound);
            }

            /**
             * Returns a double in the range [lowerBound, upperBound).
             *
             * @param lowerBound the lower bound inclusive
             * @param upperBound the upper bound exclusive
             * @return a sampled double
             */
            @Override
            public double nextDouble(
                    final double lowerBound,
                    final double upperBound
            ) {
                return random.nextDouble(lowerBound, upperBound);
            }

            @Override
            public long nextLong(final long lowerBound, final long upperBound) {
                return random.nextLong(lowerBound, upperBound);
            }

            @Override
            public float nextFloat(final float lowerBound, final float upperBound) {
                return random.nextFloat(lowerBound, upperBound);
            }

            @Override
            public void seed(final long seed) {
                random.setSeed(seed);
            }
        };
    }

    /**
     * Creates a random source seeded with the provided value.
     *
     * @param seed the seed value
     * @return a seeded random source
     */
    static RandomSource of(final long seed) {
        return of(new Random(seed));
    }

    /**
     * Reseeds the source.
     *
     * @param seed the new seed value
     */
    void seed(long seed);

    /**
     * Returns an integer in the full int range.
     *
     * @return an integer sampled from the full int range
     */
    default int nextInt() {
        return nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Returns an integer randomly chosen between [lowerBound, upperBound).
     *
     * @param lowerBound the lower bound inclusive
     * @param upperBound the upper bound exclusive
     * @return an integer sampled from [lowerBound, upperBound)
     */
    int nextInt(int lowerBound, int upperBound);

    /**
     * Returns a double in the full double range.
     *
     * @return a double sampled from the full double range
     */
    default double nextDouble() {
        return nextDouble(Double.MIN_VALUE, Double.MAX_VALUE);
    }

    /**
     * Returns a double randomly chosen between [lowerBound, upperBound).
     *
     * @param lowerBound the lower bound inclusive
     * @param upperBound the upper bound exclusive
     * @return a double sampled from [lowerBound, upperBound)
     */
    double nextDouble(double lowerBound, double upperBound);

    /**
     * Returns a long in the full long range.
     *
     * @return a long sampled from the full long range
     */
    default long nextLong() {
        return nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * Returns a long randomly chosen between [lowerBound, upperBound).
     *
     * @param lowerBound the lower bound inclusive
     * @param upperBound the upper bound exclusive
     * @return a long sampled from [lowerBound, upperBound)
     */
    long nextLong(long lowerBound, long upperBound);

    /**
     * Returns a float in the full float range.
     *
     * @return a float sampled from the full float range
     */
    default float nextFloat() {
        return nextFloat(Float.MIN_VALUE, Float.MAX_VALUE);
    }

    /**
     * Returns a float randomly chosen between [lowerBound, upperBound).
     *
     * @param lowerBound the lower bound inclusive
     * @param upperBound the upper bound exclusive
     * @return a float sampled from [lowerBound, upperBound)
     */
    float nextFloat(float lowerBound, float upperBound);
}
