package gp;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class for performing chained GP reasoning.
 * @param population The thing that gets changed over the pipeline.
 * @param <T> Type of population.
 */
public record GPPipeLine<T>(T population) {

    /**
     * Create the initial Step in the pipeline.
     * @param initializer The thing to start the pipeline.
     * @return The first step of the pipeline.
     * @param <T> The initial type.
     */
    public static <T> GPPipeLine<T> start(final Supplier<T> initializer) {
        return new GPPipeLine<>(initializer.get());
    }

    /**
     * Create the initial Step in the pipeline.
     * @param value The value at the start the pipeline.
     * @return The first step of the pipeline.
     * @param <T> The initial type.
     */
    public static <T> GPPipeLine<T> of(final T value) {
        return new GPPipeLine<>(value);
    }

    /**
     * Create the next step in the pipeline.
     * @param step a function to take you from the one step to the next.
     * @return The new pipeline.
     * @param <U> The type after the step is applied.
     */
    public <U> GPPipeLine<U> then(final Function<T, U> step) {
        return GPPipeLine.of(step.apply(this.population));
    }

    /**
     * Repeat a pipeline until the termination criteria is satisfied.
     * @param until When to stop iterating
     * @param block The steps to take
     * @return The result after the iteration
     */
    public GPPipeLine<T> repeat(
            final TerminationCriterion<T> until,
            final IterationPipeLine<T> block
    ) {
        GPPipeLine<T> result = this;
        int iterations = 0;
        while (!until.shouldTerminate(iterations, this.population)) {
            iterations += 1;
            result = block.iterate(iterations, result);
        }
        return result;
    }

    /**
     * Finishes the pipeline and returns the final result.
     * @return The final population value
     */
    public T finish() {
        return this.population;
    }
}

