import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IdeasTest {
    @Test
    void test() {
        Iterator<Integer> source = Stream.generate(() -> 4).iterator();
        Iterable<Integer> iterable = () -> source;
        StreamSupport.stream(iterable.spliterator(), true);
    }
}
