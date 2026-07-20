package gp.impl.statistics;

import gp.Population;
import gp.core.statistics.Statistic;
import result.Result;
import utils.Preconditions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Records population values as CSV.
 *
 * @param writeFile the file to write to
 * @param columns the columns to record
 * @param <T> the population item type
 */
public record CSVRecorder<T>(
        File writeFile,
        Map<String, Function<Population<T>, String>> columns
) implements Statistic<T, String> {
    /**
     * Creates a new CSVRecorder with the given file and columns.
     *
     * @param writeFile the file to write the CSV data to
     * @param columns the columns to record in the CSV file
     */
    public CSVRecorder {
        Preconditions.assertFalse(
                columns::isEmpty,
                "At least one column must be provided"
        );

        if (writeFile.exists()) {
                Preconditions.assertEquals(
                    0,
                    writeFile::length,
                    "File " + writeFile.getPath()
                        + " exists and is not empty"
                );
        }

        try {
            setupFile(writeFile, columns);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new CSV recorder.
     *
     * @param writeFile the file to write the CSV data to
     * @param columns the columns to record in the CSV file
     * @param <T> the population item type
     * @return the result of creating the recorder
     */
    public static <T> Result<CSVRecorder<T>, Throwable> of(
            final File writeFile,
            final Map<String, Function<Population<T>, String>> columns
    ) {
        return Result.fromFunction(() -> new CSVRecorder<>(writeFile, columns));
    }

    /**
     * Creates the CSV file if it doesn't exist and writes the header.
     *
     * @param writeFile the file to set up
     * @param columns the columns to write in the header
     * @throws IOException if file creation fails
     */
    void setupFile(
            final File writeFile,
            final Map<String, Function<Population<T>, String>> columns
    ) throws IOException {
        if (!writeFile.exists()) {
            Preconditions.assertTrue(
                    writeFile.createNewFile(),
                    "Unable to create file " + writeFile.getAbsolutePath()
            );
        }
        Preconditions.assertTrue(
                writeFile::canWrite,
                "Cannot write to file " + writeFile.getAbsolutePath()
        );
        this.log().accept(String.join(",", columns.keySet()));
    }

    /**
     * Returns a sink that appends a CSV row to the output file.
     *
     * @return a consumer that writes strings to the CSV file
     */
    @Override
    public Consumer<String> log() {
        return s -> {
            try {
                Files.writeString(writeFile.toPath(), s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Produces a CSV row for the provided population.
     *
     * @param population the population to record
     * @return the CSV row
     */
    @Override
    public String statistic(Population<T> population) {
        return String.join(",", columns.values().stream()
                .map(col -> col.apply(population))
                .toList());
    }
}

