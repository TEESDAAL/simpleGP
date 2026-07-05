package utils.random;

import java.util.Random;

public interface RandomSource {
    static RandomSource of(Random random) {
        return new RandomSource() {
            /// Return an integer in the range [lowerBound, upperBound).
            @Override
            public int nextInt(int lowerBound, int upperBound) {
                return random.nextInt(lowerBound, upperBound);
            }

            /// Return a double in the range [lowerBound, upperBound).
            @Override
            public double nextDouble(double lowerBound, double upperBound) {
                return random.nextDouble(lowerBound, upperBound);
            }

            @Override
            public long nextLong(long lowerBound, long upperBound) {
                return random.nextLong(lowerBound, upperBound);
            }

            @Override
            public float nextFloat(float lowerBound, float upperBound) {
                return random.nextFloat(lowerBound, upperBound);
            }

            @Override
            public void seed(long seed) {
                random.setSeed(seed);
            }
        };
    }
    static RandomSource of(long seed) {
        return of(new Random(seed));
    }
    void seed(long seed);
    int nextInt(int lowerBound, int upperBound);
    default int nextInt() {
        return nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    double nextDouble(double lowerBound, double upperBound);
    default double nextDouble() {
        return nextDouble(Double.MIN_VALUE, Double.MAX_VALUE);
    }
    long nextLong(long lowerBound, long upperBound);
    default long nextLong() {
        return nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
    }
    float nextFloat(float lowerBound, float upperBound);
    default float nextFloat() {
        return nextFloat(Float.MIN_VALUE, Float.MAX_VALUE);
    }
}
