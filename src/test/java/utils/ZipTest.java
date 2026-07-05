package utils;

import utils.stream_utils.StreamZipper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZipTest {
    @Test
    public void zip() {
        assertEquals(
                List.of(
                        5,
                        1 + 6,
                        2 + 7,
                        3 + 8,
                        4 + 9
                ),
                StreamZipper.zip(
                        IntStream.range(0, 5).boxed(),
                        IntStream.range(5, 10).boxed(),
                        Integer::sum
                ).toList()
        );
    }

    @Test
    public void zipUnequalLengths() {
        assertEquals(
                List.of(
                        Pair.of(0, 5),
                        Pair.of(1, 6),
                        Pair.of(2, 7)
                ),
                StreamZipper.zip(
                        IntStream.range(0, 5).boxed(),
                        IntStream.range(5, 8).boxed()
                ).toList()
        );
    }

    @Test
    public void testParrallelZip() {
        assertEquals(
                List.of(
                        5,
                        1 + 6,
                        2 + 7,
                        3 + 8,
                        4 + 9
                ),
                StreamZipper.zip(
                        IntStream.range(0, 5).boxed().parallel(),
                        IntStream.range(5, 10).boxed().parallel(),
                        Integer::sum
                ).parallel().toList()
        );
    }
}
