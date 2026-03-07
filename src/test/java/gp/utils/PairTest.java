package gp.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PairTest {

    @Test
    public void testPairCreation() {
        Pair<Integer, String> pair = Pair.of(1, "hello");
        assertEquals(1, pair.first());
        assertEquals("hello", pair.second());
    }

    @Test
    public void testPairGetFirst() {
        Pair<Integer, String> pair = new Pair<>(42, "world");
        assertEquals(42, pair.first());
    }

    @Test
    public void testPairGetSecond() {
        Pair<Integer, String> pair = new Pair<>(42, "world");
        assertEquals("world", pair.second());
    }

    @Test
    public void testPairMap() {
        Pair<Integer, String> original = Pair.of(5, "test");
        Pair<Integer, Integer> mapped = original.map(
                num -> num * 2,
                str -> str.length()
        );
        assertEquals(10, mapped.first());
        assertEquals(4, mapped.second());
    }

    @Test
    public void testPairMapFirst() {
        Pair<Integer, String> original = Pair.of(3, "hello");
        Pair<Integer, String> mapped = original.mapFirst(num -> num * 10);
        assertEquals(30, mapped.first());
        assertEquals("hello", mapped.second());
    }

    @Test
    public void testPairMapSecond() {
        Pair<Integer, String> original = Pair.of(3, "hello");
        Pair<Integer, String> mapped = original.mapSecond(String::toUpperCase);
        assertEquals(3, mapped.first());
        assertEquals("HELLO", mapped.second());
    }

    @Test
    public void testPairMapChaining() {
        Pair<Integer, String> original = Pair.of(5, "test");
        Pair<Integer, String> result = original
                .mapFirst(n -> n + 10)
                .mapSecond(s -> s + "!");
        assertEquals(15, result.first());
        assertEquals("test!", result.second());
    }

    @Test
    public void testPairWithNullValues() {
        Pair<String, Integer> pair = Pair.of(null, null);
        assertNull(pair.first());
        assertNull(pair.second());
    }

    @Test
    public void testPairMapWithNullFirst() {
        Pair<String, Integer> pair = Pair.of(null, 5);
        assertThrows(NullPointerException.class, 
                () -> pair.mapFirst(s -> s.toUpperCase()));
    }

    @Test
    public void testPairMapWithNullSecond() {
        Pair<String, Integer> pair = Pair.of("hello", null);
        assertThrows(NullPointerException.class,
                () -> pair.mapSecond(i -> i + 1));
    }

    @Test
    public void testPairWithDifferentTypes() {
        Pair<Double, Boolean> pair = Pair.of(3.14, true);
        assertEquals(3.14, pair.first());
        assertTrue(pair.second());
    }

    @Test
    public void testPairMapDifferentTypes() {
        Pair<String, Integer> original = Pair.of("42", 10);
        Pair<Integer, Double> mapped = original.map(
                Integer::parseInt,
                i -> i * 1.5
        );
        assertEquals(42, mapped.first());
        assertEquals(15.0, mapped.second());
    }

    @Test
    public void testPairEquality() {
        Pair<Integer, String> p1 = Pair.of(1, "test");
        Pair<Integer, String> p2 = Pair.of(1, "test");
        Pair<Integer, String> p3 = Pair.of(2, "test");
        
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }

    @Test
    public void testPairHashCode() {
        Pair<Integer, String> p1 = Pair.of(1, "test");
        Pair<Integer, String> p2 = Pair.of(1, "test");

        // Equal objects should have equal hash codes
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testPairToString() {
        Pair<Integer, String> pair = Pair.of(42, "hello");
        String str = pair.toString();
        assertNotNull(str);
        assertTrue(str.contains("42") || str.contains("hello"));
    }

    @Test
    public void testPairWithComplexObjects() {
        class Person {
            String name;
            int age;
            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }

        Person person = new Person("Alice", 30);
        Pair<Person, Integer> pair = Pair.of(person, 100);
        assertEquals("Alice", pair.first().name);
        assertEquals(30, pair.first().age);
        assertEquals(100, pair.second());
    }

    @Test
    public void testPairMapComplexTransformation() {
        Pair<Integer, Integer> original = Pair.of(3, 4);
        // Map to calculate hypotenuse components
        Pair<Integer, Integer> mapped = original.map(
                first -> first * first,
                second -> second * second
        );
        assertEquals(9, mapped.first());
        assertEquals(16, mapped.second());
    }

    @Test
    public void testPairWithEmptyStrings() {
        Pair<String, String> pair = Pair.of("", "test");
        assertEquals("", pair.first());
        assertEquals("test", pair.second());
    }

    @Test
    public void testPairMapFirstPreservesSecond() {
        Pair<Integer, String> original = Pair.of(5, "preserve");
        Pair<Integer, String> mapped = original.mapFirst(n -> n * 3);
        assertEquals("preserve", mapped.second());
    }

    @Test
    public void testPairMapSecondPreservesFirst() {
        Pair<Integer, String> original = Pair.of(5, "change");
        Pair<Integer, String> mapped = original.mapSecond(String::toUpperCase);
        assertEquals(5, mapped.first());
    }

    @Test
    public void testPairMapIdentity() {
        Pair<Integer, String> original = Pair.of(10, "test");
        Pair<Integer, String> mapped = original.map(n -> n, s -> s);
        assertEquals(original, mapped);
    }

    @Test
    public void testPairOfStaticMethod() {
        Pair<String, Integer> pair = Pair.of("alpha", 42);
        assertNotNull(pair);
        assertEquals("alpha", pair.first());
        assertEquals(42, pair.second());
    }

    @Test
    public void testPairWithLists() {
        var pair = Pair.of(
                java.util.List.of(1, 2, 3),
                java.util.List.of("a", "b", "c")
        );
        assertEquals(3, pair.first().size());
        assertEquals(3, pair.second().size());
    }

    @Test
    public void testPairMapPreservesType() {
        Pair<Integer, Integer> original = Pair.of(2, 3);
        Pair<Integer, Integer> mapped = original.map(
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
        Pair<String, String> original = Pair.of("hello", "world");
        // Create a new pair with values transformed
        Pair<String, String> transformed = original.map(
                String::toUpperCase,
                String::toUpperCase
        );
        assertEquals("HELLO", transformed.first());
        assertEquals("WORLD", transformed.second());
    }
}
