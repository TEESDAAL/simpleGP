# SimpleGP
SimpleGP is a genetic programming library written in Java.

It aims to achieve rich type safety avoiding uncertainty and runtime errors as much as possible.
Currently the library is bogged down by an overuse of generics to achieve type guarantees, hopefully this improves in the future.

It aims to be simple to use, enabling easy configuration and extension. It also aims to allow for "easy" parallelization.

## Usage
An example usage is defined in [FunctionApproximator](src/main/java/example/function_approximation/FunctionApproximator.java).

The basic workflow is defined using the fluent pattern in the [GPPipeLine](src/main/java/gp/GPPipeLine.java) class
```java
public Population<EvaluatedIndividual<...>> train(
        final int numGenerations
) {
    return GPPipeLine
            .start(initializer::initialize)
            .repeat(TerminationCriterion.nIters(numGenerations),
                    (i, pop) -> pop
                            .then(SideEffect.of((ignored) -> System.out.println(
                                    "Evaluating population for gen " + i)
                            )).then(trainEvaluator::evaluate)
                            .then(postEvaluationStatistics)
                            .then(breeder::breed)
            ).then(testEvaluator::evaluate)
            .then(postEvaluationStatistics)
            .finish();
}
```
This aims to create a clear and concise workflow, that is easy to interpret at first glance.

## Configuration and Running Experiments
This library aims to use the "code as configuation" paradigm.

This is accomplished by defining a base abstract class or interface needed to setup a GP run.

```java
public record FunctionApproximator(...) {
    ...

    /**
     * Load a Function Approximator from a given params object.
     * @param params The parameters to load from
     * @return A valid function approximator
     */
    public static FunctionApproximator fromParams(
            final FunctionApproximationParams<Double> params
    ) {
        return new FunctionApproximator(
                params.initializer(),
                params.trainEvaluator(),
                params.breeder(),
                params.testEvaluator(),
                params.scoreLogger()
        );
    }
    ...
}
```

And then components in that run can be defined by overriding methods in that class or interface.

```java
/**
 * The base-parameters for a Function Approximation GP run.
 * @param <T> The Terminal type for the model.
 */
public interface FunctionApproximationParams<T> {

    Initializer<SingleTreeIndividual<T, Double>> initializer();


    Breeder<EvaluatedIndividual<
            T, Double, SingleTreeIndividual<T, Double>,
            SingleObjectiveFitness
    >, SingleTreeIndividual<T, Double>> breeder();


    SingleObjectiveEvaluator<T> trainEvaluator();

    SingleObjectiveEvaluator<T> testEvaluator();

    Statistic<EvaluatedIndividual<
            T, Double, 
            SingleTreeIndividual<T, Double>,
            SingleObjectiveFitness
    >> scoreLogger();
}
```

## Extension
This library prefers to achieve code reuse and extension using composition over inheritance. 
This aims to avoid the complexities that deeply inherited code can cause.

This can be seen in [SingleObjectiveFitness.java](src/main/java/gp/fitness/SingleObjectiveFitness.java) and [MultiObjectiveFitness.java](src/main/java/gp/fitness/MultiObjectiveFitness.java)

Where they both implement the [Fitness](src/main/java/gp/fitness/Fitness.java) interface, but do not inherit from each other. Instead [MultiObjectiveFitness.java](src/main/java/gp/fitness/MultiObjectiveFitness.java) is defined as a collection of [SingleObjectiveFitness.java](src/main/java/gp/fitness/SingleObjectiveFitness.java) objects.

