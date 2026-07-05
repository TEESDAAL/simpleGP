package utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RepeatTest {

    @Test
    public void testRepeatZeroTimes() {
        AtomicInteger counter = new AtomicInteger(0);
        Repeat.of(0, counter::incrementAndGet);
        assertEquals(0, counter.get());
    }

    @Test
    public void testRepeatOnce() {
        AtomicInteger counter = new AtomicInteger(0);
        Repeat.of(1, counter::incrementAndGet);
        assertEquals(1, counter.get());
    }

    @Test
    public void testRepeatMultipleTimes() {
        AtomicInteger counter = new AtomicInteger(0);
        Repeat.of(5, counter::incrementAndGet);
        assertEquals(5, counter.get());
    }

    @Test
    public void testRepeatLargeTimes() {
        AtomicInteger counter = new AtomicInteger(0);
        Repeat.of(1000, counter::incrementAndGet);
        assertEquals(1000, counter.get());
    }

    @Test
    public void testRepeatExecutesInOrder() {
        List<Integer> list = new ArrayList<>();
        Repeat.of(5, () -> list.add(list.size()));
        assertEquals(List.of(0, 1, 2, 3, 4), list);
    }

    @Test
    public void testRepeatWithSideEffects() {
        AtomicInteger[] counters = new AtomicInteger[3];
        for (int i = 0; i < counters.length; i++) {
            counters[i] = new AtomicInteger(0);
        }

        Repeat.of(100, () -> {
            for (AtomicInteger counter : counters) {
                counter.incrementAndGet();
            }
        });

        for (AtomicInteger counter : counters) {
            assertEquals(100, counter.get());
        }
    }

    @Test
    public void testRepeatWithException() {
        AtomicInteger counter = new AtomicInteger(0);
        assertThrows(RuntimeException.class, () -> {
            Repeat.of(5, () -> {
                counter.incrementAndGet();
                if (counter.get() == 3) {
                    throw new RuntimeException("Test exception");
                }
            });
        });
        // Counter should have been incremented before exception
        assertEquals(3, counter.get());
    }

    @Test
    public void testRepeatNegativeTimes() {
        // Negative times should result in 0 executions
        AtomicInteger counter = new AtomicInteger(0);
        Repeat.of(-1, counter::incrementAndGet);
        assertEquals(0, counter.get());
    }

    @Test
    public void testRepeatWithStatefulRunnable() {
        class Counter {
            int value = 0;
            void increment() {
                value++;
            }
        }

        Counter counter = new Counter();
        Repeat.of(10, counter::increment);
        assertEquals(10, counter.value);
    }

    @Test
    public void testRepeatWithStringBuilding() {
        StringBuilder sb = new StringBuilder();
        Repeat.of(5, () -> sb.append("x"));
        assertEquals("xxxxx", sb.toString());
    }

    @Test
    public void testRepeatAccumulatingResult() {
        List<Integer> results = new ArrayList<>();
        Repeat.of(3, () -> results.add((int) (Math.random() * 100)));
        assertEquals(3, results.size());
    }

    @Test
    public void testRepeatCallableMultipleTimes() {
        AtomicInteger value = new AtomicInteger(10);
        Repeat.of(5, value::decrementAndGet);
        assertEquals(5, value.get());
    }

    @Test
    public void testRepeatWithModifyingState() {
        class State {
            final int[] array = new int[5];
            void fill() {
                for (int i = 0; i < array.length; i++) {
                    array[i]++;
                }
            }
        }

        State state = new State();
        Repeat.of(3, state::fill);

        for (int value : state.array) {
            assertEquals(3, value);
        }
    }

    @Test
    public void testRepeatWithNoOpRunnable() {
        // Should complete without error
        Repeat.of(100, () -> {});
        // If we reach here, the test passed
        assertTrue(true);
    }

    @Test
    public void testRepeatBoundary() {
        AtomicInteger counter = new AtomicInteger(0);
        Repeat.of(Integer.MAX_VALUE / 1000000, counter::incrementAndGet);
        // Just ensure it completes without failing
        assertTrue(counter.get() > 0);
    }

    @Test
    public void testRepeatCumulative() {
        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            Repeat.of(5, counter::incrementAndGet);
        }
        assertEquals(50, counter.get());
    }

    @Test
    public void testRepeatPreservesOrder() {
        List<Integer> list = new ArrayList<>();
        Repeat.of(5, list::add);
        assertEquals("[0, 1, 2, 3, 4]", list.toString());
    }
}
