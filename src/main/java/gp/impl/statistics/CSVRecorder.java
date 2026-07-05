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
 * A Statistic that records values in a CSV file.
 * The columns of the CSV file are defined by a list of CSVColumn instances.
 * @param writeFile The file to write the CSV data to.
 *                  If the file already exists, it must be empty.
 * @param columns The columns to record in the CSV file.
 * @param <T> The type of the population individuals.
 */
public record CSVRecorder<T>(
        File writeFile, Map<String, Function<Population<T>, String>> columns
) implements Statistic<T, String> {

    public static <T> Result<CSVRecorder<T>, Throwable> of(File writeFile, Map<String, Function<Population<T>, String>> columns) {
        return Result.fromFunction(() -> new CSVRecorder<>(writeFile, columns));
    }

    /**
     * Creates a new CSVRecorder with the given file and columns.
     * @param writeFile The file to write the CSV data to.
     *      The file must either not exist or be empty.
     * @param columns The columns to record in the CSV file.
     *      Column names must be unique and at least one column must be provided.
     */
    public CSVRecorder {
        Preconditions.assertFalse(
                columns::isEmpty,
                "At least one column must be provided"
        );

        if (writeFile.exists()) {
            Preconditions.assertEquals(
                    0, writeFile::length,
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
     * Creates the CSV file if it doesn't exist and writes the header.
     * @param writeFile The file to set up
     * @param columns The columns to write in the header
     */
    void setupFile(File writeFile, Map<String, Function<Population<T>, String>> columns) throws IOException {
        if (!writeFile.exists()) {
            Preconditions.assertTrue(
                    writeFile.createNewFile(),
                    "Unable to create file " + writeFile.getAbsolutePath()
            );
        }
        Preconditions.assertTrue(writeFile::canWrite, "Cannot write to file "+writeFile.getAbsolutePath());
        this.log().accept(String.join(",", columns.keySet()));
    }

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

    @Override
    public String statistic(Population<T> population) {
        return String.join(",", columns.values().stream()
                .map(col -> col.apply(population))
                .toList());
    }
}

