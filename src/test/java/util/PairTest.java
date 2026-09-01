package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PairTest {

    @Test
    public void testPairCreation() {
        final Pair<Integer, String> pair = Pair.of(1, "hello");
        assertEquals(1, pair.first());
        assertEquals("hello", pair.second());
    }

    @Test
    public void testPairGetFirst() {
        final Pair<Integer, String> pair = new Pair<>(42, "world");
        assertEquals(42, pair.first());
    }

    @Test
    public void testPairGetSecond() {
        final Pair<Integer, String> pair = new Pair<>(42, "world");
        assertEquals("world", pair.second());
    }

    @Test
    public void testPairMap() {
        final Pair<Integer, String> original = Pair.of(5, "test");
        final Pair<Integer, Integer> mapped = original.map(
            num -> num * 2,
            String::length
        );
        assertEquals(10, mapped.first());
        assertEquals(4, mapped.second());
    }

    @Test
    public void testPairMapFirst() {
        final Pair<Integer, String> original = Pair.of(3, "hello");
        final Pair<Integer, String> mapped = original.mapFirst(num -> num * 10);
        assertEquals(30, mapped.first());
        assertEquals("hello", mapped.second());
    }

    @Test
    public void testPairMapSecond() {
        final Pair<Integer, String> original = Pair.of(3, "hello");
        final Pair<Integer, String> mapped = original.mapSecond(String::toUpperCase);
        assertEquals(3, mapped.first());
        assertEquals("HELLO", mapped.second());
    }

    @Test
    public void testPairMapChaining() {
        final Pair<Integer, String> original = Pair.of(5, "test");
        final Pair<Integer, String> result = original
                .mapFirst(n -> n + 10)
                .mapSecond(s -> s + "!");
        assertEquals(15, result.first());
        assertEquals("test!", result.second());
    }

    @Test
    public void testPairWithNullValues() {
        final Pair<String, Integer> pair = Pair.of(null, null);
        assertNull(pair.first());
        assertNull(pair.second());
    }

    @Test
    public void testPairMapWithNullFirst() {
        final Pair<String, Integer> pair = Pair.of(null, 5);
        assertThrows(NullPointerException.class, 
                () -> pair.mapFirst(s -> s.toUpperCase()));
    }

    @Test
    public void testPairMapWithNullSecond() {
        final Pair<String, Integer> pair = Pair.of("hello", null);
        assertThrows(NullPointerException.class,
                () -> pair.mapSecond(i -> i + 1));
    }

    @Test
    public void testPairWithDifferentTypes() {
        final Pair<Double, Boolean> pair = Pair.of(3.14, true);
        assertEquals(3.14, pair.first());
        assertTrue(pair.second());
    }

    @Test
    public void testPairMapDifferentTypes() {
        final Pair<String, Integer> original = Pair.of("42", 10);
        final Pair<Integer, Double> mapped = original.map(
                Integer::parseInt,
                i -> i * 1.5
        );
        assertEquals(42, mapped.first());
        assertEquals(15.0, mapped.second());
    }

    @Test
    public void testPairEquality() {
        final Pair<Integer, String> p1 = Pair.of(1, "test");
        final Pair<Integer, String> p2 = Pair.of(1, "test");
        final Pair<Integer, String> p3 = Pair.of(2, "test");
        
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }

    @Test
    public void testPairHashCode() {
        final Pair<Integer, String> p1 = Pair.of(1, "test");
        final Pair<Integer, String> p2 = Pair.of(1, "test");

        // Equal objects should have equal hash codes
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testPairToString() {
        final Pair<Integer, String> pair = Pair.of(42, "hello");
        final String str = pair.toString();
        assertNotNull(str);
        assertTrue(str.contains("42") || str.contains("hello"));
    }

    @Test
    public void testPairWithComplexObjects() {
        record Person(String name, int age) {
        }

        final Person person = new Person("Alice", 30);
        final Pair<Person, Integer> pair = Pair.of(person, 100);
        assertEquals("Alice", pair.first().name);
        assertEquals(30, pair.first().age);
        assertEquals(100, pair.second());
    }

    @Test
    public void testPairMapComplexTransformation() {
        final Pair<Integer, Integer> original = Pair.of(3, 4);
        // Map to calculate hypotenuse components
        final Pair<Integer, Integer> mapped = original.map(
                first -> first * first,
                second -> second * second
        );
        assertEquals(9, mapped.first());
        assertEquals(16, mapped.second());
    }

    @Test
    public void testPairWithEmptyStrings() {
        final Pair<String, String> pair = Pair.of("", "test");
        assertEquals("", pair.first());
        assertEquals("test", pair.second());
    }

    @Test
    public void testPairMapFirstPreservesSecond() {
        final Pair<Integer, String> original = Pair.of(5, "preserve");
        final Pair<Integer, String> mapped = original.mapFirst(n -> n * 3);
        assertEquals("preserve", mapped.second());
    }

    @Test
    public void testPairMapSecondPreservesFirst() {
        final Pair<Integer, String> original = Pair.of(5, "change");
        final Pair<Integer, String> mapped = original.mapSecond(String::toUpperCase);
        assertEquals(5, mapped.first());
    }

    @Test
    public void testPairMapIdentity() {
        final Pair<Integer, String> original = Pair.of(10, "test");
        final Pair<Integer, String> mapped = original.map(n -> n, s -> s);
        assertEquals(original, mapped);
    }

    @Test
    public void testPairOfStaticMethod() {
        final Pair<String, Integer> pair = Pair.of("alpha", 42);
        assertNotNull(pair);
        assertEquals("alpha", pair.first());
        assertEquals(42, pair.second());
    }

    @Test
    public void testPairWithLists() {
        final var pair = Pair.of(
                java.util.List.of(1, 2, 3),
                java.util.List.of("a", "b", "c")
        );
        assertEquals(3, pair.first().size());
        assertEquals(3, pair.second().size());
    }

    @Test
    public void testPairMapPreservesType() {
        final Pair<Integer, Integer> original = Pair.of(2, 3);
        final Pair<Integer, Integer> mapped = original.map(
                n -> n * 2,
                n -> n * 3
        );
        // Result should maintain integer types
        assertEquals(4, mapped.first());
        assertEquals(9, mapped.second());
    }

    @Test
    public void testPairValueExchangeViaMap() {
        // Map can be used to swap or manipulate pairs
        final Pair<String, String> original = Pair.of("hello", "world");
        // Create a new pair with values transformed
        final Pair<String, String> transformed = original.map(
                String::toUpperCase,
                String::toUpperCase
        );
        assertEquals("HELLO", transformed.first());
        assertEquals("WORLD", transformed.second());
    }
}
