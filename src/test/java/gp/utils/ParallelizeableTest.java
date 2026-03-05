package gp.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import result.Result;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ParallelizeableTest {
    @Test
    public void testGroupingParallel() {
        var ls = GroupingIterable.group(Stream.generate(() -> 0).limit(10), 3)
                .parallel()
                .map(Stream::toList)
                .toList();
        assertEquals(List.of(
                List.of(0, 0, 0),
                List.of(0, 0, 0),
                List.of(0, 0, 0),
                List.of(0)
        ), ls);
    }

    @Test
    public void testGroupingSequential() {
        var ls = GroupingIterable.group(Stream.generate(() -> 0).limit(10), 3)
                .map(Stream::toList)
                .toList();
        assertEquals(List.of(
                List.of(0, 0, 0),
                List.of(0, 0, 0),
                List.of(0, 0, 0),
                List.of(0)
        ), ls);
    }

    @Test
    public void testGrouping() {
        assertEquals(
                List.of(
                        List.of(0, 1, 2),
                        List.of(3, 4, 5),
                        List.of(6, 7, 8),
                        List.of(9)
                ),
                GroupingIterable.group(IntStream.range(0, 10).boxed(), 3)
                        .map(Stream::toList)
                        .toList()
        );
    }

    @Test
    public void testGroupingExactSize() {
        // Test when stream size is exactly divisible by group size
        assertEquals(
                List.of(
                        List.of(0, 1, 2),
                        List.of(3, 4, 5)
                ),
                GroupingIterable.group(IntStream.range(0, 6).boxed(), 3)
                        .map(Stream::toList)
                        .toList()
        );
    }

    @Test
    public void testGroupingSizeOne() {
        // Test grouping with size 1
        assertEquals(
                List.of(
                        List.of(0),
                        List.of(1),
                        List.of(2)
                ),
                GroupingIterable.group(IntStream.range(0, 3).boxed(), 1)
                        .map(Stream::toList)
                        .toList()
        );
    }

    @Test
    public void testGroupingLargeSize() {
        // Test grouping where size is larger than stream
        assertEquals(
                List.of(List.of(0, 1, 2)),
                GroupingIterable.group(IntStream.range(0, 3).boxed(), 10)
                        .map(Stream::toList)
                        .toList()
        );
    }

    @Test
    public void testGroupingEmptyStream() {
        // Test grouping an empty stream
        assertEquals(
                List.of(),
                GroupingIterable.group(Stream.empty(), 3)
                        .map(Stream::toList)
                        .toList()
        );
    }

    @Test
    @Timeout(1)
    public void groupWorksOnInfiniteStream() {
        List<List<Integer>> group = GroupingIterable.group(
                        Stream.generate(() -> 0),
                        4
                ).limit(2)
                .map(Stream::toList)
                .toList();
        assertEquals(
                List.of(
                        List.of(0, 0, 0, 0),
                        List.of(0, 0, 0, 0)
                ),
                group
        );
    }

    @Test
    public void testGroupingDifferentTypes() {
        // Test grouping with different types
        assertEquals(
                List.of(
                        List.of("a", "b"),
                        List.of("c", "d"),
                        List.of("e")
                ),
                GroupingIterable.group(
                        Stream.of("a", "b", "c", "d", "e"),
                        2
                ).map(Stream::toList).toList()
        );
    }


    @Test
    public void testParallelize() {
        List<Integer> squares = IntStream.range(0, 10).mapToObj(i -> i * i).toList();

        for (int batchSize = 1; batchSize <= 2; batchSize++) {
            Stream<Integer> numbers = IntStream.range(0, 10).boxed();
            Parallelizeable parallelizeable = Parallelizeable.of(batchSize);

            List<Integer> parallelSquares = parallelizeable.parallelize(numbers, i -> i * i)
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
        Parallelizeable p = Parallelizeable.of(batchSize);
        List<Integer> expected = IntStream.range(0, 10).map(i -> i * 2).boxed().toList();
        List<Integer> actual = p.parallelize(IntStream.range(0, 10).boxed(), i -> i * 2)
                .sorted(Comparator.comparingInt(i -> i))
                .toList();
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateWithDefaultImplementation() {
        for (int batchSize = 1; batchSize <= 100; batchSize++) {
            Parallelizeable parallelizeable = Parallelizeable.of(batchSize);
            assertEquals(
                    IntStream.range(0, 10).map(i -> 4).boxed().toList(),
                    parallelizeable.generate(() -> 4).limit(10).toList()
            );
        }
    }

    @Test
    public void testGenerateN() {
        Parallelizeable p = Parallelizeable.of(5);
        List<Integer> result = p.generateN(() -> 42, 5).toList();
        assertEquals(List.of(42, 42, 42, 42, 42), result);
    }

    @Test
    public void testGenerateNWithZero() {
        Parallelizeable p = Parallelizeable.of(1);
        List<Integer> result = p.generateN(() -> 42, 0).toList();
        assertEquals(List.of(), result);
    }

    @Test
    public void testParallelizeEmptyStream() {
        Parallelizeable p = Parallelizeable.of(1);
        List<Integer> result = p.parallelize(Stream.<Integer>empty(), i -> i * 2).toList();
        assertEquals(List.of(), result);
    }

    @Test
    public void testParallelizeRetainsElements() {
        Parallelizeable p = Parallelizeable.of(3);
        List<String> input = List.of("a", "b", "c", "d", "e");
        List<String> result = p.parallelize(input.stream(), String::toUpperCase)
                .sorted()
                .toList();
        List<String> expected = List.of("A", "B", "C", "D", "E");
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

    // ==================== Stream Characteristics Tests ====================

    @Test
    public void testGroupingPreservesOrder() {
        List<Integer> input = IntStream.range(0, 20).boxed().toList();
        List<Integer> result = GroupingIterable.group(input.stream(), 5)
                .flatMap(s -> s)
                .toList();
        assertEquals(input, result);
    }

    @Test
    public void testParallelizePreservesCount() {
        Parallelizeable p = Parallelizeable.of(3);
        int count = 1000;
        List<Integer> result = p.parallelize(IntStream.range(0, count).boxed(), i -> i)
                .toList();
        assertEquals(count, result.size());
    }

    @Test
    public void testGenerateProducesCorrectCount() {
        Parallelizeable p = Parallelizeable.of(7);
        int count = 100;
        List<Integer> result = p.generate(() -> 0).limit(count).toList();
        assertEquals(count, result.size());
    }


    @Test
    public void testBatchSizeDoesStuff() {
        //JVM warmup
        int n = 10_000;
        timeIt(() -> {
            List<Integer> ignored = IntStream.range(0, n).boxed().map(ParallelizeableTest::slowFunction).toList();
        });
        long sequentialTime = timeIt(() -> {
            List<Integer> ignored = IntStream.range(0, n).boxed().map(ParallelizeableTest::slowFunction).toList();
        });

        System.out.println("Sequential time: " + sequentialTime + "ms");
        for (int batchSize : List.of(1, 10, 100, 1000, 5_000, 10_000)) {
            Parallelizeable p = Parallelizeable.of(batchSize);
            long time = timeIt(() -> {
                List<Integer> ignored = p.parallelize(IntStream.range(0, n).boxed(), ParallelizeableTest::slowFunction).toList();
            });
            System.out.println("Batch size " + batchSize + " time: " + time + "ms");
        }
    }

    static long timeIt(Runnable r) {
        long start = System.currentTimeMillis();
        r.run();
        return System.currentTimeMillis() - start;
    }

    static int slowFunction(int n) {
        Result.fromFunction(() -> Thread.sleep(Duration.ofNanos(10_000L)))
                .get();
        return n;
    }
}
