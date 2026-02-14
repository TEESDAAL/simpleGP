package gp;

import java.util.function.Function;
import java.util.function.Supplier;

public class GPPipeLine<T> {
    private final T population;

    public GPPipeLine(T population) {
        this.population = population;
    }

    public static <T> GPPipeLine<T> start(Supplier<T> initializer) {
        return new GPPipeLine<>(initializer.get());
    }

    public static <T> GPPipeLine<T> of(T value) {
        return new GPPipeLine<>(value);
    }

    public <U> GPPipeLine<U> then(Function<T, U> step) {
        return GPPipeLine.of(step.apply(this.population));
    }

    public GPPipeLine<T> repeat(TerminationCriterion<T> until, Function<GPPipeLine<T>, GPPipeLine<T>> block) {
        GPPipeLine<T> result = this;
        int iterations = 0;
        while (!until.shouldTerminate(iterations, this.population)) {
            iterations += 1;
            result = block.apply(this);
        }
        return result;
    }

    public T finish() {
        return this.population;
    }
}

