package gp.fitness;

public interface Fitness<Self> extends Comparable<Self> {
    @Override
    default int compareTo(Self other) {
        return this.compareWith(other).ord();
    }

    Comparison compareWith(Self other);
}