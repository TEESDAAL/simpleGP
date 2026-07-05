package example.function_approximation.parameters.initial;

import example.function_approximation.parameters.FunctionApproximationParameters;
import gp.Population;
import gp.core.breeder.Breeder;
import gp.core.evaluators.Evaluator;
import gp.core.fitness.SingleObjectiveFitness;
import gp.core.individual.EvaluatedIndividual;
import gp.core.initializers.Initializer;
import gp.core.statistics.Statistic;
import gp.impl.individual.SingleTreeIndividual;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ParameterBuilder<X, Y> {
    private Initializer<SingleTreeIndividual<X, Y>> initializer;
    private Breeder<
            EvaluatedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>,
            SingleTreeIndividual<X, Y>
    > breeder;
    private Evaluator<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> trainEvaluator;
    private Evaluator<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> testEvaluator;
    private Statistic<EvaluatedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>, ?> statistic = new Statistic<>() {
        @Override
        public Object statistic(Population<EvaluatedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>> population) {
            return "Hello";
        }

        @Override
        public Consumer<Object> log() {
            return e -> {
            };
        }
    };
    public static <X, Y> ParameterBuilder<X, Y> of() {
        return new ParameterBuilder<>();
    }

    public ParameterBuilder<X, Y> initializer(Initializer<SingleTreeIndividual<X, Y>> initializer) {
        this.initializer = initializer;
        return this;
    }

    public ParameterBuilder<X, Y> breeder(
            Breeder<EvaluatedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>, SingleTreeIndividual<X, Y>> breeder
    ) {
        this.breeder = breeder;
        return this;
    }

    public ParameterBuilder<X, Y> trainEvaluator(
        Evaluator<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> evaluator
    ) {
        this.trainEvaluator = evaluator;
        return this;
    }

    public ParameterBuilder<X, Y> testEvaluator(
        Evaluator<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> evaluator
    ) {
        this.testEvaluator = evaluator;
        return this;
    }
    public ParameterBuilder<X, Y> addStatistic(
        Statistic<EvaluatedIndividual<
            X, Y,
            SingleTreeIndividual<X, Y>,
            SingleObjectiveFitness
        >, ?> statistic
    ) {
        this.statistic = statistic;
//        var prev = this.statistic;
//        this.statistic = p -> statistic.sideEffect(prev.sideEffect(p));
        return this;
    }

    public FunctionApproximationParameters<
            X, Y,
            SingleTreeIndividual<X, Y>,
            Evaluator<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>
    > build() {
        Map.of(
                "Initializer", this.initializer,
                "Breeder", this.breeder,
                "Train Evaluator", this.trainEvaluator,
                "Test Evaluator", this.testEvaluator
        ).forEach((key, value) -> Objects.requireNonNull(
                value,
                "Cannot build parameters with unset" + key
        ));


        ParameterBuilder<X, Y> self = this;
        return new FunctionApproximationParameters<>() {
            @Override
            public Initializer<SingleTreeIndividual<X, Y>> initializer() {
                return self.initializer;
            }

            @Override
            public Breeder<EvaluatedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>, SingleTreeIndividual<X, Y>> breeder() {
                return self.breeder;
            }

            @Override
            public Evaluator<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> trainEvaluator() {
                return self.trainEvaluator;
            }

            @Override
            public Evaluator<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness> testEvaluator() {
                return self.testEvaluator;
            }

            @Override
            public Statistic<EvaluatedIndividual<X, Y, SingleTreeIndividual<X, Y>, SingleObjectiveFitness>, ?> scoreLogger() {
                return self.statistic;
            }

            @Override
            public int numGenerations() {
                return 50;
            }
        };
    }
}
