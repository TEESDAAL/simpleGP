package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MutableTest {

    @Test
    public void testMutableCreation() {
        Mutable<Integer> mutable = Mutable.of(42);
        assertNotNull(mutable);
        assertEquals(42, mutable.get());
    }

    @Test
    public void testMutableGet() {
        Mutable<String> mutable = Mutable.of("hello");
        assertEquals("hello", mutable.get());
    }

    @Test
    public void testMutableSet() {
        Mutable<Integer> mutable = Mutable.of(10);
        assertEquals(10, mutable.get());
        mutable.set(20);
        assertEquals(20, mutable.get());
    }

    @Test
    public void testMutableSetMultipleTimes() {
        Mutable<String> mutable = Mutable.of("first");
        assertEquals("first", mutable.get());
        mutable.set("second");
        assertEquals("second", mutable.get());
        mutable.set("third");
        assertEquals("third", mutable.get());
    }

    @Test
    public void testMutableWithNull() {
        Mutable<Integer> mutable = Mutable.of(null);
        assertNull(mutable.get());
    }

    @Test
    public void testMutableSetToNull() {
        Mutable<String> mutable = Mutable.of("initial");
        assertEquals("initial", mutable.get());
        mutable.set(null);
        assertNull(mutable.get());
    }

    @Test
    public void testMutableWithDifferentTypes() {
        Mutable<Double> doubleMutable = Mutable.of(3.14);
        assertEquals(3.14, doubleMutable.get());

        Mutable<Boolean> booleanMutable = Mutable.of(true);
        assertTrue(booleanMutable.get());

        Mutable<Character> charMutable = Mutable.of('A');
        assertEquals('A', charMutable.get());
    }

    @Test
    public void testMutableWithObjects() {
        record TestObject(int value) {
        }

        TestObject obj1 = new TestObject(100);
        Mutable<TestObject> mutable = Mutable.of(obj1);
        assertEquals(obj1, mutable.get());
        assertEquals(100, mutable.get().value);

        TestObject obj2 = new TestObject(200);
        mutable.set(obj2);
        assertEquals(obj2, mutable.get());
        assertEquals(200, mutable.get().value);
    }

    @Test
    public void testMutablePreservesReference() {
        // Test that mutable preserves object reference (not copying)
        Integer[] array = {1, 2, 3};
        Mutable<Integer[]> mutable = Mutable.of(array);
        assertSame(array, mutable.get());
    }

    @Test
    public void testMutableWithZero() {
        Mutable<Integer> mutable = Mutable.of(0);
        assertEquals(0, mutable.get());
    }

    @Test
    public void testMutableWithNegative() {
        Mutable<Integer> mutable = Mutable.of(-42);
        assertEquals(-42, mutable.get());
        mutable.set(-100);
        assertEquals(-100, mutable.get());
    }

    @Test
    public void testMutableWithEmptyString() {
        Mutable<String> mutable = Mutable.of("");
        assertEquals("", mutable.get());
    }

    @Test
    public void testMutableChainedOperations() {
        Mutable<Integer> mutable = Mutable.of(1);
        mutable.set(mutable.get() + 1);
        assertEquals(2, mutable.get());
        mutable.set(mutable.get() * 10);
        assertEquals(20, mutable.get());
    }

    @Test
    public void testMutableIndependence() {
        // Test that multiple mutable instances are independent
        Mutable<Integer> m1 = Mutable.of(10);
        Mutable<Integer> m2 = Mutable.of(20);
        
        assertEquals(10, m1.get());
        assertEquals(20, m2.get());
        
        m1.set(99);
        assertEquals(99, m1.get());
        assertEquals(20, m2.get());
    }

    @Test
    public void testMutableLargeObject() {
        // Test with a large object
        int[] largeArray = new int[10000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = i;
        }
        Mutable<int[]> mutable = Mutable.of(largeArray);
        assertEquals(largeArray.length, mutable.get().length);
    }
}
