package util;

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
    public void testAssertTrueErrorMessageIncluded() {
        final String customMessage = "Custom error message";
        final IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
            () -> Preconditions.assertTrue(false, customMessage));
        assertTrue(error.getMessage().contains(customMessage));
    }

    @Test
    public void testAssertTrueWithNullMessage() {
        // Null message should still work
        assertThrows(IllegalArgumentException.class,
            () -> Preconditions.assertTrue(false, null));
    }


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
    public void testAssertFalseErrorMessageIncluded() {
        final String customMessage = "Custom false message";
        final IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
            () -> Preconditions.assertFalse(true, customMessage));
        assertTrue(error.getMessage().contains(customMessage));
    }

    @Test
    public void testAssertFalseWithNullMessage() {
        // Null message should still work
        assertThrows(IllegalArgumentException.class,
            () -> Preconditions.assertFalse(true, null));
    }


    @Test
    public void testAssertTrueAndAssertFalseTogether() {
        Preconditions.assertTrue(true, "Should be true");
        Preconditions.assertFalse(false, "Negation should be false");
    }

}
