package gp;

/**
 * A function that takes a pipeline and returns a new pipeline,
 * used for iteration.
 * @param <T> The type of the population in the pipeline.
 */
public interface IterationPipeLine<T> {
    /**
     * Perform one iteration of the pipeline.
     * @param iterationNumber The current iteration number.
     * @param pipeline The current pipeline.
     * @return The new pipeline after applying the iteration step.
     */
    GPPipeLine<T> iterate(
            int iterationNumber,
            GPPipeLine<T> pipeline
    );
}

