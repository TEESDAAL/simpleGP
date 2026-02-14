package gp.fitness;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;

public enum Comparison {
    BETTER(1),
    WORSE(-1),
    EQUAL(0);

    final int ord;
    Comparison(int ord) {
        this.ord = ord;
    }

    public int ord() {
        return ord;
    }

    public Comparison flip() {
        return switch (this) {
            case BETTER -> WORSE;
            case WORSE -> BETTER;
            case EQUAL -> EQUAL;
        };
    }

    public Comparison then(Supplier<Comparison> nextResult) {
        return switch (this) {
            case EQUAL ->  nextResult.get();
            case WORSE, BETTER -> this;
        };
    }

    public static <T extends Comparable<T>> Comparison of(T item1, T item2) {
        int result = item1.compareTo(item2);
        if (result > 0) {
            return BETTER;
        }
        if (result < 0) {
            return WORSE;
        }
        return EQUAL;
    }


    public static <T> Comparison of(Comparator<T> comparator, T item1, T item2) {
        int result = comparator.compare(item1, item2);
        if (result > 0) {
            return BETTER;
        }
        if (result < 0) {
            return WORSE;
        }
        return EQUAL;
    }


    public Comparison thenMax(Number a, Number b) {
        if (this != EQUAL) {
            return this;
        }
        return compareMax(a, b);
    }

    public Comparison thenMin(Number a, Number b) {
        if (this != EQUAL) {
            return this;
        }
        return compareMin(a, b);
    }

    public static Comparison compareMax(Number a, Number b) {
        if (a.doubleValue() == b.doubleValue()) {
            return EQUAL;
        }

        if (a.doubleValue() > b.doubleValue()) {
            return BETTER;
        }

        return WORSE;
    }

    public static Comparison compareMin(Number a, Number b) {
        return compareMax(a, b).flip();
    }

    <U> U map(Function<Comparison, U> mapper) {
        return mapper.apply(this);
    }
}