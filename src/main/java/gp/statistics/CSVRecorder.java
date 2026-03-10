package gp.statistics;

import gp.Population;
import gp.utils.Preconditions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


/**
 * A Statistic that records values in a CSV file.
 * The columns of the CSV file are defined by a list of CSVColumn instances.
 * @param writeFile The file to write the CSV data to.
 *                  If the file already exists, it must be empty.
 * @param columns The columns to record in the CSV file.
 * @param <T> The type of the population individuals.
 */
public record CSVRecorder<T>(
        File writeFile, List<CSVColumn<Population<T>>> columns
) implements Statistic<T> {
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
        Preconditions.assertEquals(
                columns.size(),
                columns.stream().map(CSVColumn::name).distinct().count(),
                "Column names must be unique"
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
            throw new RuntimeException(
                    "Failed to set up CSV file: " + writeFile.getAbsolutePath(), e
            );
        }

    }

    /**
     * Creates the CSV file if it doesn't exist and writes the header.
     * @param writeFile The file to set up
     * @param columns The columns to write in the header
     * @throws IOException If an I/O error occurs
     */
    static <T> void setupFile(
            File writeFile, List<CSVColumn<T>> columns
    ) throws IOException {
        if (!writeFile.exists()) {
            Preconditions.assertTrue(
                    writeFile.createNewFile(),
                    "Unable to create file " + writeFile.getAbsolutePath()
            );
        }

        Files.writeString(
                writeFile.toPath(),
                String.join(
                        ", ",
                        columns.stream().map(CSVColumn::name).toList()
                )
        );
    }


    @Override
    public void log(Population<T> population) {
        try {

            String line = String.join(",", columns.stream()
                    .map(col -> col.statistic(population))
                    .toList());

            Files.writeString(writeFile.toPath(), "\n" + line);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to write to CSV file: " + writeFile.getAbsolutePath(),
                    e
            );
        }
    }
}

