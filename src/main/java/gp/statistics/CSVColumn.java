package gp.statistics;

import java.util.function.Function;


/**
 * A column in a CSV output of statistics. Each column has a name and a function
 * @param <T>
 */
public interface CSVColumn<T> {
    /**
     * Creates a CSVColumn from a name and a function that computes the statistic.
     * @param name The name of the column
     * @param statistic A function from T to String that
     *                  computes the statistic for this column
     * @return A CSVColumn instance
     * @param <T> The type of the value to compute the statistic from
     */
    static <T> CSVColumn<T> of(String name, Function<T, String> statistic) {
        return new CSVColumn<>() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public String statistic(T value) {
                return statistic.apply(value);
            }
        };
    }

    /**
     * @return The name of this column.
     */
    String name();

    /**
     * Computes the statistic for this column from the given value.
     * @param value The value to compute the statistic from
     * @return The computed statistic as a String
     */
    String statistic(T value);
}
