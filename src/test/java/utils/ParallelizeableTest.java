package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.result.Result;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ParallelizeableTest {
    @Test
    public void testParallelize() {
        final List<Integer> squares = IntStream.range(0, 10).mapToObj(i -> i * i).toList();

        for (int batchSize = 1; batchSize <= 2; batchSize++) {
            final Stream<Integer> numbers = IntStream.range(0, 10).boxed();
            final Parallelizeable parallelizeable = Parallelizeable.of(batchSize);

            final List<Integer> parallelSquares = parallelizeable.parallelize(numbers, i -> i * i)
                    .sorted(Comparator.comparingInt(i -> i)).toList();
            assertEquals(
                    squares,
                    parallelSquares
            );
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 100})
    public void testParallelizeWithVariousBatchSizes(int batchSize) {
        final Parallelizeable p = Parallelizeable.of(batchSize);
        final List<Integer> expected = IntStream.range(0, 10).map(i -> i * 2).boxed().toList();
        final List<Integer> actual = p.parallelize(IntStream.range(0, 10).boxed(), i -> i * 2)
                .sorted(Comparator.comparingInt(i -> i))
                .toList();
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateWithDefaultImplementation() {
        for (int batchSize = 1; batchSize <= 100; batchSize++) {
            final Parallelizeable parallelizeable = Parallelizeable.of(batchSize);
            assertEquals(
                    IntStream.range(0, 10).map(_ -> 4).boxed().toList(),
                    parallelizeable.generate(() -> 4).limit(10).toList()
            );
        }
    }

    @Test
    public void testGenerateN() {
        final Parallelizeable p = Parallelizeable.of(5);
        final List<Integer> result = p.generateN(() -> 42, 5).toList();
        assertEquals(List.of(42, 42, 42, 42, 42), result);
    }

    @Test
    public void testGenerateNWithZero() {
        final Parallelizeable p = Parallelizeable.of(1);
        final List<Integer> result = p.generateN(() -> 42, 0).toList();
        assertEquals(List.of(), result);
    }

    @Test
    public void testParallelizeEmptyStream() {
        final Parallelizeable p = Parallelizeable.of(1);
        final List<Integer> result = p.parallelize(Stream.<Integer>empty(), i -> i * 2).toList();
        assertEquals(List.of(), result);
    }

    @Test
    public void testParallelizeRetainsElements() {
        final Parallelizeable p = Parallelizeable.of(3);
        final List<String> input = List.of("a", "b", "c", "d", "e");
        final List<String> result = p.parallelize(input.stream(), String::toUpperCase)
                .sorted()
                .toList();
        final List<String> expected = List.of("A", "B", "C", "D", "E");
        assertEquals(expected, result);
    }


    @Test
    public void testParallelizeableNegativeBatchSize() {
        assertThrows(IllegalArgumentException.class, () -> Parallelizeable.of(-1));
    }

    @Test
    public void testParallelizeableZeroBatchSize() {
        assertThrows(IllegalArgumentException.class, () -> Parallelizeable.of(0));
    }

    @Test
    public void testParallelizeablePositiveBatchSize() {
        // Should not throw
        assertNotNull(Parallelizeable.of(1));
        assertNotNull(Parallelizeable.of(100));
    }


    @Test
    public void testParallelizePreservesCount() {
        final Parallelizeable p = Parallelizeable.of(3);
        final int count = 1000;
        final List<Integer> result = p.parallelize(IntStream.range(0, count).boxed(), i -> i)
                .toList();
        assertEquals(count, result.size());
    }

    @Test
    public void testGenerateProducesCorrectCount() {
        final Parallelizeable p = Parallelizeable.of(7);
        final int count = 100;
        final List<Integer> result = p.generate(() -> 0).limit(count).toList();
        assertEquals(count, result.size());
    }


    @Test
    public void testBatchSizeDoesStuff() {
        //JVM warmup
        final int n = 10_000;
        timeIt(() -> {
            final List<Integer> ignored = IntStream.range(0, n).boxed().map(ParallelizeableTest::slowFunction).toList();
        });
        final long sequentialTime = timeIt(() -> {
            final List<Integer> ignored = IntStream.range(0, n).boxed().map(ParallelizeableTest::slowFunction).toList();
        });

        System.out.println("Sequential time: " + sequentialTime + "ms");
        for (final int batchSize : List.of(1, 10, 100, 1000, 5_000, 10_000)) {
            final Parallelizeable p = Parallelizeable.of(batchSize);
            final long time = timeIt(() -> {
                final List<Integer> ignored = p.parallelize(IntStream.range(0, n).boxed(), ParallelizeableTest::slowFunction).toList();
            });
            System.out.println("Batch size " + batchSize + " time: " + time + "ms");
        }
    }

    static long timeIt(Runnable r) {
        final long start = System.currentTimeMillis();
        r.run();
        return System.currentTimeMillis() - start;
    }

    static int slowFunction(int n) {
        Result.fromFunction(() -> Thread.sleep(Duration.ofNanos(10_000L)))
                .get();
        return n;
    }
}
