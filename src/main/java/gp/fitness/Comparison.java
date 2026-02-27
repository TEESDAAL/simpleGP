package gp.fitness;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Enumeration representing the result of a comparison.
 */
public enum Comparison {
    /** Indicates the left operand is better. */
    BETTER(1),
    /** Indicates the left operand is worse. */
    WORSE(-1),
    /** Indicates operands are equal. */
    EQUAL(0);

    /** Internal ordering value for comparisons. */
    private final int ord;

    Comparison(final int ordinalValue) {
        this.ord = ordinalValue;
    }

    /**
     * Gets the ordinal value of this comparison.
     * @return The ordinal value
     */
    public int ord() {
        return ord;
    }

    /**
     * Flips the comparison (BETTER becomes WORSE, etc).
     * @return The flipped comparison
     */
    public Comparison flip() {
        return switch (this) {
            case BETTER -> WORSE;
            case WORSE -> BETTER;
            case EQUAL -> EQUAL;
        };
    }

    /**
     * Returns this comparison if not EQUAL; otherwise tiebreaks
     * by evaluating the supplier.
     * @param nextResult The supplier for the next comparison
     * @return This comparison or the result of the supplier
     */
    public Comparison then(final Supplier<Comparison> nextResult) {
        return switch (this) {
            case EQUAL -> nextResult.get();
            case WORSE, BETTER -> this;
        };
    }

    /**
     * Compares two items using their natural ordering.
     * @param <T> The comparable type
     * @param item1 The first item
     * @param item2 The second item
     * @return The comparison result
     */
    public static <T extends Comparable<T>> Comparison of(
            final T item1,
            final T item2
    ) {
        int result = item1.compareTo(item2);
        if (result > 0) {
            return BETTER;
        }
        if (result < 0) {
            return WORSE;
        }
        return EQUAL;
    }


    /**
     * Compares two items using a custom comparator.
     * @param <T> The type
     * @param comparator The comparator
     * @param item1 The first item
     * @param item2 The second item
     * @return The comparison result
     */
    public static <T> Comparison of(
            final Comparator<T> comparator,
            final T item1,
            final T item2
    ) {
        int result = comparator.compare(item1, item2);
        if (result > 0) {
            return BETTER;
        }
        if (result < 0) {
            return WORSE;
        }
        return EQUAL;
    }


    /**
     * Returns this comparison if not EQUAL; otherwise compares
     * the maximum of two numbers.
     * @param <N> The numeric type
     * @param a The first number
     * @param b The second number
     * @return This comparison or the max comparison
     */
    public <N extends Number & Comparable<N>> Comparison thenMax(
            final N a, final N b) {
        if (this != EQUAL) {
            return this;
        }
        return compareMax(a, b);
    }

    /**
     * Returns this comparison if not EQUAL; otherwise compares
     * the minimum of two numbers.
     * @param <N> The numeric type
     * @param a The first number
     * @param b The second number
     * @return This comparison or the min comparison
     */
    public <N extends Number & Comparable<N>> Comparison thenMin(
            final N a, final N b) {
        if (this != EQUAL) {
            return this;
        }
        return compareMin(a, b);
    }

    /**
     * Compares the maximum of two numbers.
     * @param <N> The numeric type
     * @param a The first number
     * @param b The second number
     * @return The comparison result
     */
    public static <N extends Number & Comparable<N>>
            Comparison compareMax(
            final N a,
            final N b
    ) {
        return Comparison.of(a.doubleValue(), b.doubleValue());
    }

    /**
     * Compares the minimum of two numbers.
     * @param <N> The numeric type
     * @param a The first number
     * @param b The second number
     * @return The comparison result
     */
    public static <N extends Number & Comparable<N>>
            Comparison compareMin(
            final N a,
            final N b
    ) {
        return compareMax(a, b).flip();
    }

    /**
     * Maps this comparison using a function.
     * @param <U> The return type
     * @param mapper The mapping function
     * @return The mapped value
     */
    <U> U map(final Function<Comparison, U> mapper) {
        return mapper.apply(this);
    }
}
