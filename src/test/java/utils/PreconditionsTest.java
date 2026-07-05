package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PreconditionsTest {
    @Test
    public void testAssertTrueWithTrueCondition() {
        // Should not throw
        Preconditions.assertTrue(true, "Condition should be true");
    }

    @Test
    public void testAssertTrueWithFalseCondition() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertTrue(false, "Expected true but got false"));
    }

    @Test
    public void testAssertTrueWithSupplierReturningTrue() {
        Preconditions.assertTrue(() -> true, "Supplier should return true");
    }

    @Test
    public void testAssertTrueWithSupplierReturningFalse() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertTrue(() -> false, "Supplier returned false"));
    }


    @Test
    public void testAssertTrueErrorMessageIncluded() {
        String customMessage = "Custom error message";
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertTrue(false, customMessage));
        assertTrue(error.getMessage().contains(customMessage));
    }

    @Test
    public void testAssertTrueWithNullMessage() {
        // Null message should still work
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertTrue(false, null));
    }

    // ==================== AssertFalse Tests ====================

    @Test
    public void testAssertFalseWithFalseCondition() {
        // Should not throw
        Preconditions.assertFalse(false, "Condition should be false");
    }

    @Test
    public void testAssertFalseWithTrueCondition() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertFalse(true, "Expected false but got true"));
    }

    @Test
    public void testAssertFalseWithSupplierReturningFalse() {
        Preconditions.assertFalse(() -> false, "Supplier should return false");
    }

    @Test
    public void testAssertFalseWithSupplierReturningTrue() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertFalse(() -> true, "Supplier returned true"));
    }


    @Test
    public void testAssertFalseErrorMessageIncluded() {
        String customMessage = "Custom false message";
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertFalse(true, customMessage));
        assertTrue(error.getMessage().contains(customMessage));
    }

    @Test
    public void testAssertFalseWithNullMessage() {
        // Null message should still work
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertFalse(true, null));
    }

    // ==================== AssertEquals Tests ====================

    @Test
    public void testAssertEqualsWithEqualValues() {
        // Should not throw
        Preconditions.assertEquals(42, () -> 42, "Values should be equal");
    }

    @Test
    public void testAssertEqualsWithUnequalValues() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertEquals(42, () -> 43, "Values should be equal"));
    }

    @Test
    public void testAssertEqualsWithStrings() {
        Preconditions.assertEquals("hello", () -> "hello", "Strings should be equal");
    }

    @Test
    public void testAssertEqualsWithUnequalStrings() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertEquals("hello", () -> "world", "Strings should be equal"));
    }

    @Test
    public void testAssertEqualsErrorMessageIncluded() {
        String customMessage = "Custom assert equals message";
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertEquals(10, () -> 20, customMessage));
        assertTrue(error.getMessage().contains(customMessage));
    }

    @Test
    public void testAssertEqualsWithNullActual() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertEquals("value", () -> null, "Value should equal null"));
    }

    @Test
    public void testAssertEqualsWithBothNull() {
        Preconditions.assertEquals(null, () -> null, "Null should equal null");
    }

    @Test
    public void testAssertEqualsWithObject() {
        Object obj = new Object();
        Preconditions.assertEquals(obj, () -> obj, "Objects should be equal");
    }

    @Test
    public void testAssertEqualsWithDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertEquals(obj1, () -> obj2, "Objects should be equal"));
    }

    // ==================== Integration Tests ====================

    @Test
    public void testAssertTrueAndAssertFalseTogether() {
        boolean condition = true;
        Preconditions.assertTrue(condition, "Should be true");
        Preconditions.assertFalse(!condition, "Negation should be false");
    }

    @Test
    public void testMultipleAssertions() {
        int x = 10;
        Preconditions.assertTrue(x > 5, "x should be greater than 5");
        Preconditions.assertTrue(x < 20, "x should be less than 20");
        Preconditions.assertEquals(10, () -> x, "x should equal 10");
    }

    @Test
    public void testAssertionsWithLambdas() {
        Preconditions.assertTrue(() -> "test".equals("test"), "Strings should match");
        Preconditions.assertFalse(() -> 5 > 10, "5 should not be greater than 10");
    }

    // ==================== Edge Cases ====================

    @Test
    public void testAssertTrueWithZero() {
        // 0 is not a boolean, but the condition 0 == 0 is true
        Preconditions.assertTrue(0 == 0, "Zero should equal zero");
    }

    @Test
    public void testAssertFalseWithZero() {
        Preconditions.assertFalse(0 != 0, "Zero should not not-equal zero");
    }

    @Test
    public void testAssertEqualsWithLargeLists() {
        java.util.List<Integer> list1 = java.util.List.of(1, 2, 3);
        java.util.List<Integer> list2 = java.util.List.of(1, 2, 3);
        Preconditions.assertEquals(list1, () -> list2, "Lists should be equal");
    }

    @Test
    public void testAssertEqualsWithDifferentLists() {
        java.util.List<Integer> list1 = java.util.List.of(1, 2, 3);
        java.util.List<Integer> list2 = java.util.List.of(1, 2, 4);
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.assertEquals(list1, () -> list2, "Lists should be equal"));
    }


    @Test
    public void testAssertTrueSupplierIsEvaluated() {
        // Verify that the supplier is actually called
        java.util.concurrent.atomic.AtomicInteger callCount = 
                new java.util.concurrent.atomic.AtomicInteger(0);
        
        Preconditions.assertTrue(() -> {
            callCount.incrementAndGet();
            return true;
        }, "Supplier should be called");
        
        assertEquals(1, callCount.get());
    }

    @Test
    public void testAssertFalseSupplierIsEvaluated() {
        // Verify that the supplier is actually called
        java.util.concurrent.atomic.AtomicInteger callCount = 
                new java.util.concurrent.atomic.AtomicInteger(0);
        
        Preconditions.assertFalse(() -> {
            callCount.incrementAndGet();
            return false;
        }, "Supplier should be called");
        
        assertEquals(1, callCount.get());
    }

    @Test
    public void testAssertEqualsSupplierIsEvaluated() {
        // Verify that the supplier is actually called
        java.util.concurrent.atomic.AtomicInteger callCount = 
                new java.util.concurrent.atomic.AtomicInteger(0);
        
        Preconditions.assertEquals(42, () -> {
            callCount.incrementAndGet();
            return 42;
        }, "Supplier should be called");
        
        assertEquals(1, callCount.get());
    }
}
