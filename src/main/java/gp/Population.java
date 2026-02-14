package gp;

import java.util.ArrayList;
import java.util.List;

public record Population<I>(List<I> individuals) {
    public Population {
        individuals = List.copyOf(individuals);
    }

    public static <I> Population<I> of(List<I> individuals) {
        return new Population<>(individuals);
    }

    public Population<I> combine(Population<I> other) {
        List<I> newIndividuals = new ArrayList<>(individuals);
        newIndividuals.addAll(other.individuals);
        return new Population<>(newIndividuals);
    }

    public I getFirst() {
        return individuals.getFirst();
    }

    public I get(int index) {
        assert index >= 0 && index < individuals.size();
        return individuals.get(index);
    }

    public int size() {
        return individuals.size();
    }
}
