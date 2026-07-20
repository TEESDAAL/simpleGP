package gp.impl.statistics;

import gp.Population;
import gp.core.initializers.Initialiser;
import gp.core.statistics.SideEffect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Persists populations to disk.
 *
 * @param outFile the target file
 * @param <I> the item type
 */
public record Checkpointer<I extends Serializable>(File outFile)
        implements SideEffect<Population<I>> {
    /**
     * Writes the population to disk.
     *
     * @param population the population to persist
     * @return the same population
     */
    @Override
    public Population<I> sideEffect(Population<I> population) {
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            final ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(population);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return population;
    }

    /**
     * Loads a population from disk.
     *
     * @param outFile the file to read
     * @param <I> the item type
     * @return the loaded population
     * @throws IOException if the file cannot be read
     * @throws ClassNotFoundException if the serialized type is missing
     */
    public static <I extends Serializable> Population<I> load(
            final File outFile
    ) throws IOException, ClassNotFoundException {
        final FileInputStream fileInputStream = new FileInputStream(outFile);
        final ObjectInputStream objectInputStream =
                new ObjectInputStream(fileInputStream);
        @SuppressWarnings("unchecked")
        final Population<I> p2 = (Population<I>) objectInputStream.readObject();

        //noinspection DataFlowIssue, this just checks if the cast is truely valid.
        assert p2.stream().allMatch(i -> i instanceof I);
        objectInputStream.close();
        return p2;
    }

    /**
     * Creates an initializer that loads a population from disk.
     *
     * @param outFile the file to load
     * @param <I> the item type
     * @return an initializer that loads from the file
     */
    public static <I extends Serializable> Initialiser<I> initialiseFrom(
            final File outFile
    ) {
        return () -> {
            try {
                return load(outFile);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(
                        "Failed to initialize population from file: " + e
                );
            }
        };
    }
}
